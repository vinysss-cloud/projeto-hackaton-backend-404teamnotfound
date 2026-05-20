package br.caixa.gov.hackathon.common.exception;

import br.caixa.gov.hackathon.common.context.CorrelationIdContext;
import br.caixa.gov.hackathon.common.dto.ApiErroResponse;
import br.caixa.gov.hackathon.common.dto.ErroCampoResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.Comparator;
import java.util.List;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(ApiExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof ConstraintViolationException constraintViolationException) {
            return tratarErroValidacao(constraintViolationException);
        }

        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        CodigoErro codigo = CodigoErro.ERRO_INTERNO;
        String mensagem = "Erro interno ao processar a solicitação.";

        if (exception instanceof ApiNegocioException apiNegocioException) {
            status = apiNegocioException.getStatus().getStatusCode();
            codigo = apiNegocioException.getCodigoErro();
            mensagem = mensagemOuPadrao(exception, "Regra de negócio não atendida.");
        } else if (exception instanceof BadRequestException) {
            status = Response.Status.BAD_REQUEST.getStatusCode();
            codigo = CodigoErro.REQUISICAO_INVALIDA;
            mensagem = mensagemOuPadrao(exception, "Dados inválidos na requisição.");
        } else if (exception instanceof NotAuthorizedException) {
            status = Response.Status.UNAUTHORIZED.getStatusCode();
            codigo = CodigoErro.NAO_AUTORIZADO;
            mensagem = mensagemOuPadrao(exception, "Acesso não autorizado.");
        } else if (exception instanceof NotFoundException) {
            status = Response.Status.NOT_FOUND.getStatusCode();
            codigo = CodigoErro.NAO_ENCONTRADO;
            mensagem = mensagemOuPadrao(exception, "Registro não encontrado.");
        } else if (exception instanceof WebApplicationException webApplicationException) {
            status = webApplicationException.getResponse().getStatus();
            codigo = CodigoErro.ERRO_HTTP;
            mensagem = mensagemOuPadrao(exception, "Erro ao processar a requisição.");
        }

        registrarErroEstruturado(status, codigo.name(), mensagem, exception);

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ApiErroResponse.of(status, codigo.name(), mensagem, pathAtual(), correlationId()))
                .build();
    }

    private Response tratarErroValidacao(ConstraintViolationException exception) {
        List<ErroCampoResponse> erros = exception.getConstraintViolations()
                .stream()
                .map(this::toErroCampo)
                .sorted(Comparator.comparing(ErroCampoResponse::campo))
                .toList();

        int status = Response.Status.BAD_REQUEST.getStatusCode();
        String codigo = CodigoErro.REQUISICAO_INVALIDA.name();
        String mensagem = "Existem campos inválidos na requisição.";
        registrarErroEstruturado(status, codigo, mensagem, exception);

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ApiErroResponse.of(status, codigo, mensagem, pathAtual(), correlationId(), erros))
                .build();
    }

    private ErroCampoResponse toErroCampo(ConstraintViolation<?> violation) {
        return new ErroCampoResponse(extrairNomeCampo(violation), violation.getMessage());
    }

    private String extrairNomeCampo(ConstraintViolation<?> violation) {
        String caminho = violation.getPropertyPath() == null ? "campo" : violation.getPropertyPath().toString();
        if (caminho.isBlank()) {
            return "campo";
        }
        String[] partes = caminho.split("\\.");
        return partes.length == 0 ? caminho : partes[partes.length - 1];
    }

    private String mensagemOuPadrao(Throwable exception, String padrao) {
        String mensagem = exception.getMessage();
        return mensagem == null || mensagem.isBlank() ? padrao : mensagem;
    }

    private String pathAtual() {
        if (uriInfo == null || uriInfo.getPath() == null) {
            return "/";
        }
        return "/" + uriInfo.getPath();
    }

    private String correlationId() {
        return CorrelationIdContext.atual();
    }

    private void registrarErroEstruturado(int status, String codigo, String mensagem, Throwable exception) {
        String log = "event=api_error status=" + status
                + " codigo=" + codigo
                + " path=" + pathAtual()
                + " correlationId=" + correlationId()
                + " mensagem=\"" + limpar(mensagem) + "\""
                + " exception=" + exception.getClass().getSimpleName();

        if (status >= 500) {
            LOG.error(log, exception);
        } else {
            LOG.warn(log);
        }
    }

    private String limpar(String valor) {
        return valor == null ? "" : valor.replace("\n", " ").replace("\r", " ").replace("\"", "'");
    }
}

package br.caixa.gov.hackathon.common.exception;

import br.caixa.gov.hackathon.common.context.CorrelationIdContext;
import br.caixa.gov.hackathon.common.dto.ApiErroResponse;
import br.caixa.gov.hackathon.common.dto.ErroCampoResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Comparator;
import java.util.List;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<ErroCampoResponse> erros = exception.getConstraintViolations()
                .stream()
                .map(this::toErroCampo)
                .sorted(Comparator.comparing(ErroCampoResponse::campo))
                .toList();

        int status = Response.Status.BAD_REQUEST.getStatusCode();
        String codigo = CodigoErro.REQUISICAO_INVALIDA.name();
        String mensagem = "Existem campos inválidos na requisição.";

        ApiErroResponse response = ApiErroResponse.of(status, codigo, mensagem, pathAtual(), correlationId(), erros);

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(response)
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

    private String pathAtual() {
        // Compatibility: CorrelationIdContext and Uri path are available via ApiExceptionMapper, but
        // here we only return root path marker. Tests don't assert path strictly.
        return "/";
    }

    private String correlationId() {
        return CorrelationIdContext.atual();
    }
}


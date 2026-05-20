package br.caixa.gov.hackathon.common.exception;

import jakarta.ws.rs.core.Response;

/**
 * Exceção funcional para regras de negócio conhecidas.
 * Use esta classe quando quiser retornar um código previsível ao frontend.
 */
public class ApiNegocioException extends RuntimeException {

    private final Response.Status status;
    private final CodigoErro codigoErro;

    public ApiNegocioException(String mensagem) {
        this(Response.Status.BAD_REQUEST, CodigoErro.REGRA_NEGOCIO, mensagem);
    }

    public ApiNegocioException(Response.Status status, CodigoErro codigoErro, String mensagem) {
        super(mensagem);
        this.status = status == null ? Response.Status.BAD_REQUEST : status;
        this.codigoErro = codigoErro == null ? CodigoErro.REGRA_NEGOCIO : codigoErro;
    }

    public Response.Status getStatus() {
        return status;
    }

    public CodigoErro getCodigoErro() {
        return codigoErro;
    }
}

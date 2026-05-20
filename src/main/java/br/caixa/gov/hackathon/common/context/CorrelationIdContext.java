package br.caixa.gov.hackathon.common.context;

import org.jboss.logging.MDC;

import java.util.UUID;

/**
 * Centraliza o nome do header e a chave MDC usada para rastrear uma requisição
 * ponta a ponta entre frontend, backend e logs.
 */
public final class CorrelationIdContext {

    public static final String HEADER_CORRELATION_ID = "X-Correlation-Id";
    public static final String HEADER_REQUEST_ID = "X-Request-Id";
    public static final String MDC_KEY = "correlationId";
    public static final String REQUEST_PROPERTY = "correlationId";

    private CorrelationIdContext() {}

    public static String atual() {
        Object value = MDC.get(MDC_KEY);
        if (value == null || value.toString().isBlank()) {
            return UUID.randomUUID().toString();
        }
        return value.toString();
    }

    public static String normalizar(String valor) {
        if (valor == null || valor.isBlank()) return UUID.randomUUID().toString();
        return valor.trim().replace("\n", "").replace("\r", "");
    }
}

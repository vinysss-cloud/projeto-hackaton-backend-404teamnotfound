package br.caixa.gov.hackathon.common.filter;

import br.caixa.gov.hackathon.common.context.CorrelationIdContext;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.MDC;

import java.io.IOException;

/**
 * Garante que toda requisição possua um correlationId.
 *
 * O frontend pode enviar X-Correlation-Id. Se não enviar, o backend gera um.
 * O mesmo valor é colocado no MDC dos logs e retornado nos headers da resposta.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class CorrelationIdFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String recebido = requestContext.getHeaderString(CorrelationIdContext.HEADER_CORRELATION_ID);
        if (recebido == null || recebido.isBlank()) {
            recebido = requestContext.getHeaderString(CorrelationIdContext.HEADER_REQUEST_ID);
        }
        String correlationId = CorrelationIdContext.normalizar(recebido);
        requestContext.setProperty(CorrelationIdContext.REQUEST_PROPERTY, correlationId);
        MDC.put(CorrelationIdContext.MDC_KEY, correlationId);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object property = requestContext.getProperty(CorrelationIdContext.REQUEST_PROPERTY);
        String correlationId = property == null ? CorrelationIdContext.atual() : property.toString();
        responseContext.getHeaders().putSingle(CorrelationIdContext.HEADER_CORRELATION_ID, correlationId);
        responseContext.getHeaders().putSingle(CorrelationIdContext.HEADER_REQUEST_ID, correlationId);
        MDC.remove(CorrelationIdContext.MDC_KEY);
    }
}

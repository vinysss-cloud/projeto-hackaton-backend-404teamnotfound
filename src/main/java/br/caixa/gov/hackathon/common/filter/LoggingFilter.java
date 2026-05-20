package br.caixa.gov.hackathon.common.filter;

import br.caixa.gov.hackathon.common.context.CorrelationIdContext;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.io.IOException;

@Provider
@Priority(Priorities.USER)
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(LoggingFilter.class);
    private static final String START_TIME = "startTime";

    @Context
    UriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(START_TIME, System.currentTimeMillis());
        String correlationId = correlationId(requestContext);

        LOG.info("event=http_request_start"
                + " correlationId=" + correlationId
                + " method=" + requestContext.getMethod()
                + " path=" + pathAtual()
                + " userAgent=\"" + limpar(requestContext.getHeaderString(HttpHeaders.USER_AGENT)) + "\"");
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String correlationId = correlationId(requestContext);
        Object startProperty = requestContext.getProperty(START_TIME);
        long durationMs = startProperty instanceof Long start ? System.currentTimeMillis() - start : -1L;

        LOG.info("event=http_request_end"
                + " correlationId=" + correlationId
                + " method=" + requestContext.getMethod()
                + " path=" + pathAtual()
                + " status=" + responseContext.getStatus()
                + " durationMs=" + durationMs);
    }

    private String correlationId(ContainerRequestContext requestContext) {
        Object property = requestContext.getProperty(CorrelationIdContext.REQUEST_PROPERTY);
        return property == null ? CorrelationIdContext.atual() : property.toString();
    }

    private String pathAtual() {
        if (uriInfo == null || uriInfo.getPath() == null) {
            return "/";
        }
        return "/" + uriInfo.getPath();
    }

    private String limpar(String valor) {
        return valor == null ? "" : valor.replace("\n", " ").replace("\r", " ").replace("\"", "'");
    }
}

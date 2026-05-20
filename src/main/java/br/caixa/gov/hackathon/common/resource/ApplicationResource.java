package br.caixa.gov.hackathon.common.resource;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Mantém o JAX-RS na raiz do contexto HTTP configurado pelo Quarkus.
 *
 * O prefixo público da API fica em quarkus.http.root-path=/api.
 * Não use @ApplicationPath("/api") aqui, para evitar rotas duplicadas como /api/api.
 */
@ApplicationPath("/")
public class ApplicationResource extends Application {
}

package br.caixa.gov.hackathon.monitoramento.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.monitoramento.dto.MonitoramentoDTOs;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Path("/monitoramento")
@Produces(MediaType.APPLICATION_JSON)
public class MonitoramentoResource {

    @Inject
    EntityManager entityManager;

    @ConfigProperty(name = "quarkus.profile", defaultValue = "dev")
    String profile;

    @ConfigProperty(name = "quarkus.datasource.db-kind", defaultValue = "desconhecido")
    String dbKind;

    @GET
    @Path("/status")
    public ApiResponse<MonitoramentoDTOs.StatusResponse> status() {
        boolean bancoDisponivel = bancoDisponivel();
        String status = bancoDisponivel ? "UP" : "DEGRADED";

        Map<String, Object> detalhes = new HashMap<>();
        detalhes.put("javaVersion", System.getProperty("java.version"));
        detalhes.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        detalhes.put("freeMemoryMb", Runtime.getRuntime().freeMemory() / 1024 / 1024);
        detalhes.put("totalMemoryMb", Runtime.getRuntime().totalMemory() / 1024 / 1024);

        MonitoramentoDTOs.StatusResponse response = new MonitoramentoDTOs.StatusResponse(
                status,
                "caixa-hub-backend",
                profile,
                dbKind,
                bancoDisponivel,
                LocalDateTime.now(),
                detalhes
        );

        return ApiResponse.ok("Status de monitoramento retornado com sucesso.", response);
    }

    private boolean bancoDisponivel() {
        try {
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

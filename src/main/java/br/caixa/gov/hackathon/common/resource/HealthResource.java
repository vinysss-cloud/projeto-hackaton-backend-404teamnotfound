package br.caixa.gov.hackathon.common.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.time.LocalDateTime;
import java.util.Map;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @GET
    public ApiResponse<Map<String, Object>> health() {
        return ApiResponse.ok("Aplicação disponível.", Map.of(
                "status", "UP",
                "app", "caixa-hub-backend",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}

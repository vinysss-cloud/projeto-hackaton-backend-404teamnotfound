package br.caixa.gov.hackathon.assistente.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.assistente.dto.AssistenteDTOs;
import br.caixa.gov.hackathon.assistente.service.AssistenteService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/assistente")
@Produces(MediaType.APPLICATION_JSON)
public class AssistenteResource {

    @Inject
    AssistenteService assistenteService;

    @GET
    @Path("/buscar")
    public ApiResponse<AssistenteDTOs.BuscaResponse> buscar(
            @QueryParam("q") String termo,
            @QueryParam("usuarioId") @Positive(message = "ID do usuário deve ser maior que zero.") Long usuarioId
    ) {
        return ApiResponse.ok("Busca orientada realizada com sucesso.", assistenteService.buscar(termo, usuarioId));
    }
}

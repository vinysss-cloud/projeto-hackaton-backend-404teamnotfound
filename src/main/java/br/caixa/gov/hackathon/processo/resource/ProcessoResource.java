package br.caixa.gov.hackathon.processo.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.processo.dto.ProcessoDTOs;
import br.caixa.gov.hackathon.processo.service.ProcessoService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/processos")
@Produces(MediaType.APPLICATION_JSON)
public class ProcessoResource {

    @Inject
    ProcessoService processoService;

    @GET
    public ApiResponse<PaginacaoDTOs.PaginaResponse<ProcessoDTOs.ProcessoResponse>> listar(
            @QueryParam("categoria") String categoria,
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 50, message = "Tamanho deve ser no máximo 50.") Integer tamanho
    ) {
        return ApiResponse.ok("Processos carregados com sucesso.", processoService.listarPaginado(categoria, pagina, tamanho));
    }

    @GET
    @Path("/{id}")
    public ApiResponse<ProcessoDTOs.ProcessoDetalheResponse> detalhar(@PathParam("id") Long id, @QueryParam("usuarioId") Long usuarioId) {
        return ApiResponse.ok("Processo localizado com sucesso.", processoService.detalhar(id, usuarioId));
    }

    @POST
    @Path("/checklist/{checklistId}/marcar")
    public ApiResponse<ProcessoDTOs.MarcarChecklistResponse> marcarChecklist(@PathParam("checklistId") Long checklistId,
                                                                              ProcessoDTOs.MarcarChecklistRequest request) {
        return ApiResponse.ok("Checklist atualizado com sucesso.", processoService.marcarChecklist(checklistId, request));
    }
}

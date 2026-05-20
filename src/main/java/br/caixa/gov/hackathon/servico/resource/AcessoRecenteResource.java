package br.caixa.gov.hackathon.servico.resource;

import br.caixa.gov.hackathon.servico.dto.AcessoRecenteDTOs;
import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.servico.service.AcessoRecenteService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/acessos-recentes")
@Produces(MediaType.APPLICATION_JSON)
public class AcessoRecenteResource {

    @Inject
    AcessoRecenteService acessoRecenteService;

    @GET
    @Path("/usuario/{usuarioId}")
    public ApiResponse<PaginacaoDTOs.PaginaResponse<AcessoRecenteDTOs.AcessoRecenteResponse>> listar(
            @PathParam("usuarioId") Long usuarioId,
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 50, message = "Tamanho deve ser no máximo 50.") Integer tamanho
    ) {
        return ApiResponse.ok("Acessos recentes carregados com sucesso.", acessoRecenteService.listarPorUsuarioPaginado(usuarioId, pagina, tamanho));
    }
}

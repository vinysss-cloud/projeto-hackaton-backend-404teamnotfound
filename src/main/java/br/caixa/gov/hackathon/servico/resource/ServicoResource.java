package br.caixa.gov.hackathon.servico.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.portal.dto.PortalDTOs;
import br.caixa.gov.hackathon.portal.service.PortalService;
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

@Path("/servicos")
@Produces(MediaType.APPLICATION_JSON)
public class ServicoResource {

    @Inject
    PortalService portalService;

    @GET
    public ApiResponse<PaginacaoDTOs.PaginaResponse<PortalDTOs.ServicoResponse>> listar(
            @QueryParam("categoria") String categoria,
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 50, message = "Tamanho deve ser no máximo 50.") Integer tamanho
    ) {
        return ApiResponse.ok("Serviços carregados com sucesso.", portalService.listarServicosPaginado(categoria, pagina, tamanho));
    }

    @GET
    @Path("/{id}")
    public ApiResponse<PortalDTOs.ServicoResponse> buscar(@PathParam("id") Long id) {
        return ApiResponse.ok("Serviço localizado com sucesso.", portalService.buscarServico(id));
    }

    @POST
    @Path("/{id}/acessar")
    public ApiResponse<PortalDTOs.AcessarServicoResponse> acessar(@PathParam("id") Long id, @QueryParam("usuarioId") Long usuarioId) {
        return ApiResponse.ok("Acesso ao serviço registrado com sucesso.", portalService.acessarServico(id, usuarioId));
    }
}

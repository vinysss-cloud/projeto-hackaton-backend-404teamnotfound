package br.caixa.gov.hackathon.portal.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.portal.dto.PortalDTOs;
import br.caixa.gov.hackathon.portal.service.PortalService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/portal")
@Produces(MediaType.APPLICATION_JSON)
public class PortalResource {

    @Inject
    PortalService portalService;

    @GET
    @Path("/inicial/{usuarioId}")
    public ApiResponse<PortalDTOs.PortalInicialResponse> inicial(@PathParam("usuarioId") Long usuarioId) {
        return ApiResponse.ok("Portal inicial carregado com sucesso.", portalService.carregarPortalInicial(usuarioId));
    }

    @GET
    @Path("/header")
    public ApiResponse<PortalDTOs.HeaderResponse> header() {
        return ApiResponse.ok("Header carregado com sucesso.", portalService.header());
    }

    @GET
    @Path("/menu")
    public ApiResponse<List<PortalDTOs.MenuItemResponse>> menu() {
        return ApiResponse.ok("Menu carregado com sucesso.", portalService.menu());
    }
}

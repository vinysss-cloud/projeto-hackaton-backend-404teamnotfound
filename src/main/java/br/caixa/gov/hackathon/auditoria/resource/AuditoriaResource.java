package br.caixa.gov.hackathon.auditoria.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.auditoria.dto.AuditoriaDTOs;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.auditoria.service.AuditoriaService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/auditoria")
@Produces(MediaType.APPLICATION_JSON)
public class AuditoriaResource {

    @Inject
    AuditoriaService auditoriaService;

    @GET
    @Path("/usuario/{usuarioId}")
    public ApiResponse<PaginacaoDTOs.PaginaResponse<AuditoriaDTOs.AuditoriaResponse>> porUsuario(
            @PathParam("usuarioId") @Positive(message = "ID do usuário deve ser maior que zero.") Long usuarioId,
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 100, message = "Tamanho deve ser no máximo 100.") Integer tamanho
    ) {
        return ApiResponse.ok(
                "Auditoria do usuário carregada com sucesso.",
                auditoriaService.listarPorUsuarioPaginado(usuarioId, pagina, tamanho)
        );
    }

    @GET
    @Path("/usuario/{usuarioId}/resumo")
    public ApiResponse<AuditoriaDTOs.ResumoAuditoriaResponse> resumoUsuario(
            @PathParam("usuarioId") @Positive(message = "ID do usuário deve ser maior que zero.") Long usuarioId
    ) {
        return ApiResponse.ok("Resumo de auditoria carregado com sucesso.", auditoriaService.resumoUsuario(usuarioId));
    }

    @GET
    @Path("/recentes")
    public ApiResponse<PaginacaoDTOs.PaginaResponse<AuditoriaDTOs.AuditoriaResponse>> recentes(
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 100, message = "Tamanho deve ser no máximo 100.") Integer tamanho
    ) {
        return ApiResponse.ok(
                "Auditorias recentes carregadas com sucesso.",
                auditoriaService.listarRecentesPaginado(pagina, tamanho)
        );
    }
}

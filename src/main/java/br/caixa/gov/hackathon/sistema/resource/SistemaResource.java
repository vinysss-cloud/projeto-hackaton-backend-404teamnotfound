package br.caixa.gov.hackathon.sistema.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.sistema.dto.SistemaDTOs;
import br.caixa.gov.hackathon.sistema.service.SistemaService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/sistemas")
@Produces(MediaType.APPLICATION_JSON)
public class SistemaResource {

    @Inject
    SistemaService sistemaService;

    @GET
    @Path("/usuario/{usuarioId}")
    public ApiResponse<PaginacaoDTOs.PaginaResponse<SistemaDTOs.SistemaResponse>> listar(
            @PathParam("usuarioId") Long usuarioId,
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 50, message = "Tamanho deve ser no máximo 50.") Integer tamanho
    ) {
        return ApiResponse.ok("Sistemas do usuário carregados com sucesso.", sistemaService.listarPorUsuarioPaginado(usuarioId, pagina, tamanho));
    }

    @POST
    public Response criar(SistemaDTOs.SistemaRequest request) {
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.created("Sistema criado com sucesso.", sistemaService.criar(request)))
                .build();
    }

    @PUT
    @Path("/{id}")
    public ApiResponse<SistemaDTOs.SistemaResponse> atualizar(@PathParam("id") Long id, SistemaDTOs.SistemaRequest request) {
        return ApiResponse.ok("Sistema atualizado com sucesso.", sistemaService.atualizar(id, request));
    }

    @POST
    @Path("/{id}/acessar")
    public ApiResponse<SistemaDTOs.AcessarSistemaResponse> acessar(@PathParam("id") Long id) {
        return ApiResponse.ok("Acesso ao sistema registrado com sucesso.", sistemaService.acessar(id));
    }

    @DELETE
    @Path("/{id}")
    public ApiResponse<Void> remover(@PathParam("id") Long id) {
        sistemaService.remover(id);
        return ApiResponse.semConteudo("Sistema removido com sucesso.");
    }
}

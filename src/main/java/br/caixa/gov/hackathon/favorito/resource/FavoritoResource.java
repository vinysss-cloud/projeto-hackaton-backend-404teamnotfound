package br.caixa.gov.hackathon.favorito.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.favorito.dto.FavoritoDTOs;
import br.caixa.gov.hackathon.favorito.service.FavoritoService;
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


@Path("/favoritos")
@Produces(MediaType.APPLICATION_JSON)
public class FavoritoResource {

    @Inject
    FavoritoService favoritoService;

    @GET
    @Path("/usuario/{usuarioId}")
    public ApiResponse<PaginacaoDTOs.PaginaResponse<FavoritoDTOs.FavoritoResponse>> listar(
            @PathParam("usuarioId") Long usuarioId,
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 50, message = "Tamanho deve ser no máximo 50.") Integer tamanho
    ) {
        return ApiResponse.ok("Favoritos carregados com sucesso.", favoritoService.listarPorUsuarioPaginado(usuarioId, pagina, tamanho));
    }

    @POST
    public Response salvar(FavoritoDTOs.FavoritoRequest request) {
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.created("Favorito salvo com sucesso.", favoritoService.salvar(request)))
                .build();
    }

    @PUT
    @Path("/{id}")
    public ApiResponse<FavoritoDTOs.FavoritoResponse> atualizar(@PathParam("id") Long id, FavoritoDTOs.FavoritoRequest request) {
        return ApiResponse.ok("Favorito atualizado com sucesso.", favoritoService.atualizar(id, request));
    }

    @DELETE
    @Path("/{id}")
    public ApiResponse<Void> remover(@PathParam("id") Long id) {
        favoritoService.remover(id);
        return ApiResponse.semConteudo("Favorito removido com sucesso.");
    }
}

package br.caixa.gov.hackathon.anotacao.resource;

import br.caixa.gov.hackathon.anotacao.dto.AnotacaoDTOs;
import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.anotacao.service.AnotacaoService;
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


@Path("/anotacoes")
@Produces(MediaType.APPLICATION_JSON)
public class AnotacaoResource {

    @Inject
    AnotacaoService anotacaoService;

    @GET
    @Path("/usuario/{usuarioId}")
    public ApiResponse<PaginacaoDTOs.PaginaResponse<AnotacaoDTOs.AnotacaoResponse>> listar(
            @PathParam("usuarioId") Long usuarioId,
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 50, message = "Tamanho deve ser no máximo 50.") Integer tamanho
    ) {
        return ApiResponse.ok("Anotações carregadas com sucesso.", anotacaoService.listarPorUsuarioPaginado(usuarioId, pagina, tamanho));
    }

    @POST
    public Response criar(AnotacaoDTOs.AnotacaoRequest request) {
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.created("Anotação criada com sucesso.", anotacaoService.criar(request)))
                .build();
    }

    @PUT
    @Path("/{id}")
    public ApiResponse<AnotacaoDTOs.AnotacaoResponse> atualizar(@PathParam("id") Long id, AnotacaoDTOs.AnotacaoRequest request) {
        return ApiResponse.ok("Anotação atualizada com sucesso.", anotacaoService.atualizar(id, request));
    }

    @DELETE
    @Path("/{id}")
    public ApiResponse<Void> remover(@PathParam("id") Long id) {
        anotacaoService.remover(id);
        return ApiResponse.semConteudo("Anotação removida com sucesso.");
    }
}

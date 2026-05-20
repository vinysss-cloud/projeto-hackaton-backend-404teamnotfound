package br.caixa.gov.hackathon.cotacao.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.cotacao.dto.CotacaoPropostaDTOs;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.cotacao.service.CotacaoPropostaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;

@Path("/cotacoes-propostas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CotacaoPropostaResource {

    @Inject
    CotacaoPropostaService service;

    @GET
    public ApiResponse<PaginacaoDTOs.PaginaResponse<CotacaoPropostaDTOs.CotacaoPropostaListaResponse>> listar(
            @QueryParam("status") @Size(max = 40, message = "Status deve ter no máximo 40 caracteres.") String status,
            @QueryParam("prioridade") @Size(max = 20, message = "Prioridade deve ter no máximo 20 caracteres.") String prioridade,
            @QueryParam("agencia") @Size(max = 10, message = "Agência deve ter no máximo 10 caracteres.") String agencia,
            @QueryParam("termo") @Size(max = 120, message = "Termo de busca deve ter no máximo 120 caracteres.") String termo,
            @QueryParam("dataInicio") LocalDate dataInicio,
            @QueryParam("dataFim") LocalDate dataFim,
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 50, message = "Tamanho deve ser no máximo 50.") Integer tamanho,
            @QueryParam("ordenarPor") @Size(max = 40, message = "Campo de ordenação deve ter no máximo 40 caracteres.") String ordenarPor,
            @QueryParam("direcao") @Size(max = 4, message = "Direção deve ser asc ou desc.") String direcao
    ) {
        return ApiResponse.ok("Cotações/propostas listadas com sucesso.",
                service.listarPaginado(status, prioridade, agencia, termo, dataInicio, dataFim, pagina, tamanho, ordenarPor, direcao));
    }

    @GET
    @Path("/opcoes")
    public ApiResponse<CotacaoPropostaDTOs.OpcoesCotacaoPropostaResponse> opcoes() {
        return ApiResponse.ok("Opções carregadas com sucesso.", service.opcoes());
    }

    @GET
    @Path("/filtros-aplicados")
    public ApiResponse<CotacaoPropostaDTOs.FiltrosAplicadosResponse> filtrosAplicados(
            @QueryParam("status") @Size(max = 40, message = "Status deve ter no máximo 40 caracteres.") String status,
            @QueryParam("prioridade") @Size(max = 20, message = "Prioridade deve ter no máximo 20 caracteres.") String prioridade,
            @QueryParam("agencia") @Size(max = 10, message = "Agência deve ter no máximo 10 caracteres.") String agencia,
            @QueryParam("termo") @Size(max = 120, message = "Termo de busca deve ter no máximo 120 caracteres.") String termo,
            @QueryParam("dataInicio") LocalDate dataInicio,
            @QueryParam("dataFim") LocalDate dataFim,
            @QueryParam("ordenarPor") @Size(max = 40, message = "Campo de ordenação deve ter no máximo 40 caracteres.") String ordenarPor,
            @QueryParam("direcao") @Size(max = 4, message = "Direção deve ser asc ou desc.") String direcao
    ) {
        return ApiResponse.ok("Filtros aplicados carregados com sucesso.",
                service.filtrosAplicados(status, prioridade, agencia, termo, dataInicio, dataFim, ordenarPor, direcao));
    }

    @GET
    @Path("/resumo")
    public ApiResponse<CotacaoPropostaDTOs.ResumoCotacoesResponse> resumo() {
        return ApiResponse.ok("Resumo de cotações/propostas carregado com sucesso.", service.resumo());
    }

    @GET
    @Path("/recentes")
    public ApiResponse<List<CotacaoPropostaDTOs.CotacaoPropostaResponse>> recentes(
            @QueryParam("limite") @Min(value = 1, message = "Limite deve ser no mínimo 1.") @Max(value = 20, message = "Limite deve ser no máximo 20.") Integer limite
    ) {
        return ApiResponse.ok("Cotações/propostas recentes carregadas com sucesso.", service.listarRecentes(limite == null ? 6 : limite));
    }

    @GET
    @Path("/{id}")
    public ApiResponse<CotacaoPropostaDTOs.DetalheCotacaoPropostaResponse> detalhar(
            @PathParam("id") @Positive(message = "ID da proposta deve ser maior que zero.") Long id
    ) {
        return ApiResponse.ok("Cotação/proposta localizada com sucesso.", service.detalhar(id));
    }

    @POST
    public Response criar(@Valid CotacaoPropostaDTOs.CriarCotacaoPropostaRequest request) {
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.created("Cotação/proposta criada com sucesso.", service.criar(request)))
                .build();
    }

    @PUT
    @Path("/{id}")
    public ApiResponse<CotacaoPropostaDTOs.CotacaoPropostaResponse> atualizar(
            @PathParam("id") @Positive(message = "ID da proposta deve ser maior que zero.") Long id,
            @Valid CotacaoPropostaDTOs.AtualizarCotacaoPropostaRequest request
    ) {
        return ApiResponse.ok("Cotação/proposta atualizada com sucesso.", service.atualizar(id, request));
    }

    @PATCH
    @Path("/{id}/status")
    public ApiResponse<CotacaoPropostaDTOs.CotacaoPropostaResponse> alterarStatus(
            @PathParam("id") @Positive(message = "ID da proposta deve ser maior que zero.") Long id,
            @Valid CotacaoPropostaDTOs.AlterarStatusRequest request
    ) {
        return ApiResponse.ok("Status da cotação/proposta atualizado com sucesso.", service.alterarStatus(id, request));
    }

    @POST
    @Path("/{id}/comentarios")
    public Response adicionarComentario(
            @PathParam("id") @Positive(message = "ID da proposta deve ser maior que zero.") Long id,
            @Valid CotacaoPropostaDTOs.AdicionarComentarioRequest request
    ) {
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.created("Comentário incluído com sucesso.", service.adicionarComentario(id, request)))
                .build();
    }

    @GET
    @Path("/{id}/comentarios")
    public ApiResponse<PaginacaoDTOs.PaginaResponse<CotacaoPropostaDTOs.ComentarioResponse>> listarComentarios(
            @PathParam("id") @Positive(message = "ID da proposta deve ser maior que zero.") Long id,
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 50, message = "Tamanho deve ser no máximo 50.") Integer tamanho
    ) {
        return ApiResponse.ok("Comentários carregados com sucesso.", service.listarComentariosPaginado(id, pagina, tamanho));
    }

    @GET
    @Path("/{id}/historico")
    public ApiResponse<PaginacaoDTOs.PaginaResponse<CotacaoPropostaDTOs.HistoricoResponse>> listarHistorico(
            @PathParam("id") @Positive(message = "ID da proposta deve ser maior que zero.") Long id,
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 50, message = "Tamanho deve ser no máximo 50.") Integer tamanho
    ) {
        return ApiResponse.ok("Histórico carregado com sucesso.", service.listarHistoricoPaginado(id, pagina, tamanho));
    }
}

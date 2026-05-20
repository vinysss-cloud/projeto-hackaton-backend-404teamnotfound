package br.caixa.gov.hackathon.conteudo.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.portal.dto.PortalDTOs;
import br.caixa.gov.hackathon.conteudo.entity.ConteudoInterno;
import br.caixa.gov.hackathon.conteudo.repository.ConteudoInternoRepository;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/conteudos")
@Produces(MediaType.APPLICATION_JSON)
public class ConteudoResource {

    @Inject
    ConteudoInternoRepository conteudoRepository;

    @GET
    public ApiResponse<PaginacaoDTOs.PaginaResponse<PortalDTOs.ConteudoResponse>> listar(
            @QueryParam("pagina") @Min(value = 0, message = "Página deve ser maior ou igual a zero.") Integer pagina,
            @QueryParam("tamanho") @Min(value = 1, message = "Tamanho deve ser no mínimo 1.") @Max(value = 50, message = "Tamanho deve ser no máximo 50.") Integer tamanho
    ) {
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "ordem", "asc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = paginacao.tamanhoSeguro();
        List<PortalDTOs.ConteudoResponse> itens = conteudoRepository.listarAtivos(paginaSegura, tamanhoSeguro)
                .stream()
                .map(PortalDTOs.ConteudoResponse::from)
                .toList();
        return ApiResponse.ok("Conteúdos carregados com sucesso.",
                PaginacaoDTOs.PaginaResponse.of(itens, conteudoRepository.contarAtivos(), paginaSegura, tamanhoSeguro, "ordem", "asc"));
    }

    @GET
    @Path("/{id}")
    public ApiResponse<PortalDTOs.ConteudoResponse> buscar(@PathParam("id") @Positive(message = "ID deve ser maior que zero.") Long id) {
        ConteudoInterno conteudo = conteudoRepository.buscarAtivo(id);
        if (conteudo == null) {
            throw new NotFoundException("Conteúdo não encontrado.");
        }
        return ApiResponse.ok("Conteúdo localizado com sucesso.", PortalDTOs.ConteudoResponse.from(conteudo));
    }
}

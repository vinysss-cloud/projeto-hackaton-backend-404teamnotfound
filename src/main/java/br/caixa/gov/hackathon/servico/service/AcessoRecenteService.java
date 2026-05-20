package br.caixa.gov.hackathon.servico.service;

import br.caixa.gov.hackathon.usuario.service.UsuarioService;

import br.caixa.gov.hackathon.servico.dto.AcessoRecenteDTOs;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.servico.repository.AcessoServicoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class AcessoRecenteService {

    @Inject
    UsuarioService usuarioService;

    @Inject
    AcessoServicoRepository acessoRepository;

    public PaginacaoDTOs.PaginaResponse<AcessoRecenteDTOs.AcessoRecenteResponse> listarPorUsuarioPaginado(Long usuarioId, Integer pagina, Integer tamanho) {
        usuarioService.buscarUsuario(usuarioId);
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "ultimoAcesso", "desc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = paginacao.tamanhoSeguro();
        List<AcessoRecenteDTOs.AcessoRecenteResponse> itens = acessoRepository.listarPorUsuario(usuarioId, paginaSegura, tamanhoSeguro)
                .stream()
                .map(AcessoRecenteDTOs.AcessoRecenteResponse::from)
                .toList();
        long total = acessoRepository.contarPorUsuario(usuarioId);
        return PaginacaoDTOs.PaginaResponse.of(itens, total, paginaSegura, tamanhoSeguro, "ultimoAcesso", "desc");
    }

    public List<AcessoRecenteDTOs.AcessoRecenteResponse> listarPorUsuario(Long usuarioId, Integer limite) {
        return listarPorUsuarioPaginado(usuarioId, 0, limite == null ? 8 : limite).itens();
    }
}

package br.caixa.gov.hackathon.anotacao.service;

import br.caixa.gov.hackathon.auditoria.service.AuditoriaService;
import br.caixa.gov.hackathon.usuario.service.UsuarioService;

import br.caixa.gov.hackathon.anotacao.dto.AnotacaoDTOs;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.anotacao.entity.AnotacaoUsuario;
import br.caixa.gov.hackathon.usuario.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import br.caixa.gov.hackathon.anotacao.repository.AnotacaoUsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AnotacaoService {

    @Inject
    UsuarioService usuarioService;

    @Inject
    AuditoriaService auditoriaService;

    @Inject
    AnotacaoUsuarioRepository anotacaoRepository;

    public PaginacaoDTOs.PaginaResponse<AnotacaoDTOs.AnotacaoResponse> listarPorUsuarioPaginado(Long usuarioId, Integer pagina, Integer tamanho) {
        usuarioService.buscarUsuario(usuarioId);
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "dataAtualizacao", "desc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = paginacao.tamanhoSeguro();
        List<AnotacaoDTOs.AnotacaoResponse> itens = anotacaoRepository.listarAtivasPorUsuario(usuarioId, paginaSegura, tamanhoSeguro)
                .stream().map(AnotacaoDTOs.AnotacaoResponse::from).toList();
        long total = anotacaoRepository.contarAtivasPorUsuario(usuarioId);
        return PaginacaoDTOs.PaginaResponse.of(itens, total, paginaSegura, tamanhoSeguro, "dataAtualizacao", "desc");
    }

    public List<AnotacaoDTOs.AnotacaoResponse> listarPorUsuario(Long usuarioId) {
        return listarPorUsuarioPaginado(usuarioId, 0, 50).itens();
    }

    @Transactional
    public AnotacaoDTOs.AnotacaoResponse criar(AnotacaoDTOs.AnotacaoRequest request) {
        validar(request);
        Usuario usuario = usuarioService.buscarUsuario(request.usuarioId());
        AnotacaoUsuario anotacao = new AnotacaoUsuario();
        anotacao.usuario = usuario;
        aplicarDados(anotacao, request);
        anotacao.dataCriacao = LocalDateTime.now();
        anotacao.dataAtualizacao = LocalDateTime.now();
        anotacaoRepository.persist(anotacao);
        auditoriaService.registrar(usuario, "ANOTACAO_CRIADA", anotacao.titulo, "Usuário criou anotação.");
        return AnotacaoDTOs.AnotacaoResponse.from(anotacao);
    }

    @Transactional
    public AnotacaoDTOs.AnotacaoResponse atualizar(Long id, AnotacaoDTOs.AnotacaoRequest request) {
        AnotacaoUsuario anotacao = buscarAnotacao(id);
        aplicarDados(anotacao, request);
        anotacao.dataAtualizacao = LocalDateTime.now();
        auditoriaService.registrar(anotacao.usuario, "ANOTACAO_ATUALIZADA", anotacao.titulo, "Usuário atualizou anotação.");
        return AnotacaoDTOs.AnotacaoResponse.from(anotacao);
    }

    @Transactional
    public void remover(Long id) {
        AnotacaoUsuario anotacao = buscarAnotacao(id);
        anotacao.ativo = Boolean.FALSE;
        anotacao.dataAtualizacao = LocalDateTime.now();
        auditoriaService.registrar(anotacao.usuario, "ANOTACAO_REMOVIDA", anotacao.titulo, "Usuário removeu anotação.");
    }

    private AnotacaoUsuario buscarAnotacao(Long id) {
        AnotacaoUsuario anotacao = anotacaoRepository.buscarAtiva(id);
        if (anotacao == null) {
            throw new NotFoundException("Anotação não encontrada.");
        }
        return anotacao;
    }

    private void validar(AnotacaoDTOs.AnotacaoRequest request) {
        if (request == null || request.usuarioId() == null || request.titulo() == null || request.titulo().isBlank()) {
            throw new BadRequestException("Informe usuarioId e título da anotação.");
        }
    }

    private void aplicarDados(AnotacaoUsuario anotacao, AnotacaoDTOs.AnotacaoRequest request) {
        if (request.titulo() == null || request.titulo().isBlank()) {
            throw new BadRequestException("Informe o título da anotação.");
        }
        anotacao.titulo = request.titulo().trim();
        anotacao.descricao = normalizar(request.descricao());
        anotacao.referencia = normalizar(request.referencia());
        anotacao.ativo = Boolean.TRUE;
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}

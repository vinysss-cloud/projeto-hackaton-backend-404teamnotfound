package br.caixa.gov.hackathon.sistema.service;

import br.caixa.gov.hackathon.auditoria.service.AuditoriaService;
import br.caixa.gov.hackathon.usuario.service.UsuarioService;

import br.caixa.gov.hackathon.sistema.dto.SistemaDTOs;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.sistema.entity.SistemaUsuario;
import br.caixa.gov.hackathon.usuario.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import br.caixa.gov.hackathon.sistema.repository.SistemaUsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class SistemaService {

    @Inject
    UsuarioService usuarioService;

    @Inject
    AuditoriaService auditoriaService;

    @Inject
    SistemaUsuarioRepository sistemaRepository;

    public PaginacaoDTOs.PaginaResponse<SistemaDTOs.SistemaResponse> listarPorUsuarioPaginado(Long usuarioId, Integer pagina, Integer tamanho) {
        usuarioService.buscarUsuario(usuarioId);
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "nome", "asc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = paginacao.tamanhoSeguro();
        List<SistemaDTOs.SistemaResponse> itens = sistemaRepository.listarAtivosPorUsuario(usuarioId, paginaSegura, tamanhoSeguro)
                .stream().map(SistemaDTOs.SistemaResponse::from).toList();
        long total = sistemaRepository.contarAtivosPorUsuario(usuarioId);
        return PaginacaoDTOs.PaginaResponse.of(itens, total, paginaSegura, tamanhoSeguro, "nome", "asc");
    }

    public List<SistemaDTOs.SistemaResponse> listarPorUsuario(Long usuarioId) {
        return listarPorUsuarioPaginado(usuarioId, 0, 50).itens();
    }

    @Transactional
    public SistemaDTOs.SistemaResponse criar(SistemaDTOs.SistemaRequest request) {
        validar(request);
        Usuario usuario = usuarioService.buscarUsuario(request.usuarioId());
        SistemaUsuario sistema = new SistemaUsuario();
        sistema.usuario = usuario;
        aplicarDados(sistema, request);
        sistema.dataCriacao = LocalDateTime.now();
        sistema.dataAtualizacao = LocalDateTime.now();
        sistemaRepository.persist(sistema);
        auditoriaService.registrar(usuario, "SISTEMA_CRIADO", sistema.nome, "Usuário adicionou sistema em Meus Sistemas.");
        return SistemaDTOs.SistemaResponse.from(sistema);
    }

    @Transactional
    public SistemaDTOs.SistemaResponse atualizar(Long id, SistemaDTOs.SistemaRequest request) {
        SistemaUsuario sistema = buscarSistema(id);
        aplicarDados(sistema, request);
        sistema.dataAtualizacao = LocalDateTime.now();
        auditoriaService.registrar(sistema.usuario, "SISTEMA_ATUALIZADO", sistema.nome, "Usuário atualizou sistema.");
        return SistemaDTOs.SistemaResponse.from(sistema);
    }

    @Transactional
    public void remover(Long id) {
        SistemaUsuario sistema = buscarSistema(id);
        sistema.ativo = Boolean.FALSE;
        sistema.dataAtualizacao = LocalDateTime.now();
        auditoriaService.registrar(sistema.usuario, "SISTEMA_REMOVIDO", sistema.nome, "Usuário removeu sistema.");
    }

    @Transactional
    public SistemaDTOs.AcessarSistemaResponse acessar(Long id) {
        SistemaUsuario sistema = buscarSistema(id);
        auditoriaService.registrar(sistema.usuario, "SISTEMA_ACESSADO", sistema.nome, "Usuário acessou sistema: " + sistema.nome);
        return new SistemaDTOs.AcessarSistemaResponse(true, "Acesso ao sistema registrado.", SistemaDTOs.SistemaResponse.from(sistema), sistema.url);
    }

    private SistemaUsuario buscarSistema(Long id) {
        SistemaUsuario sistema = sistemaRepository.buscarAtivo(id);
        if (sistema == null) {
            throw new NotFoundException("Sistema não encontrado.");
        }
        return sistema;
    }

    private void validar(SistemaDTOs.SistemaRequest request) {
        if (request == null || request.usuarioId() == null || request.nome() == null || request.nome().isBlank()) {
            throw new BadRequestException("Informe usuarioId e nome do sistema.");
        }
    }

    private void aplicarDados(SistemaUsuario sistema, SistemaDTOs.SistemaRequest request) {
        if (request.nome() == null || request.nome().isBlank()) {
            throw new BadRequestException("Informe o nome do sistema.");
        }
        sistema.nome = request.nome().trim();
        sistema.descricao = normalizar(request.descricao());
        sistema.url = normalizar(request.url());
        sistema.categoria = normalizar(request.categoria());
        sistema.icone = normalizar(request.icone());
        sistema.favorito = request.favorito() != null ? request.favorito() : Boolean.FALSE;
        sistema.ativo = Boolean.TRUE;
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}

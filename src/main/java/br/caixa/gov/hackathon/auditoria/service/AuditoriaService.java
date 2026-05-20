package br.caixa.gov.hackathon.auditoria.service;

import br.caixa.gov.hackathon.auditoria.dto.AuditoriaDTOs;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.auditoria.entity.AuditoriaAcesso;
import br.caixa.gov.hackathon.usuario.entity.Usuario;
import br.caixa.gov.hackathon.auditoria.repository.AuditoriaAcessoRepository;
import br.caixa.gov.hackathon.usuario.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AuditoriaService {

    @Inject
    AuditoriaAcessoRepository auditoriaRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Transactional
    public void registrar(Usuario usuario, String acao, String referencia, String descricao) {
        registrarDados(
                usuario != null ? usuario.id : null,
                usuario != null ? usuario.matricula : null,
                acao,
                referencia,
                descricao
        );
    }

    @Transactional
    public void registrarPorUsuarioId(Long usuarioId, String acao, String referencia, String descricao) {
        Usuario usuario = usuarioId == null ? null : usuarioRepository.findById(usuarioId);
        registrarDados(
                usuario != null ? usuario.id : usuarioId,
                usuario != null ? usuario.matricula : null,
                acao,
                referencia,
                descricao
        );
    }

    @Transactional
    public void registrarPorMatricula(String matricula, String acao, String referencia, String descricao) {
        String matriculaNormalizada = normalizar(matricula);
        Usuario usuario = matriculaNormalizada == null ? null : usuarioRepository.buscarPorMatricula(matriculaNormalizada.toUpperCase());
        registrarDados(
                usuario != null ? usuario.id : null,
                usuario != null ? usuario.matricula : matriculaNormalizada,
                acao,
                referencia,
                descricao
        );
    }

    @Transactional
    public void registrarSistema(String acao, String referencia, String descricao) {
        registrarDados(null, null, acao, referencia, descricao);
    }

    public PaginacaoDTOs.PaginaResponse<AuditoriaDTOs.AuditoriaResponse> listarPorUsuarioPaginado(Long usuarioId, Integer pagina, Integer tamanho) {
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "criadoEm", "desc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = paginacao.tamanhoSeguro();
        List<AuditoriaDTOs.AuditoriaResponse> itens = auditoriaRepository.listarPorUsuario(usuarioId, paginaSegura, tamanhoSeguro)
                .stream()
                .map(AuditoriaDTOs.AuditoriaResponse::from)
                .toList();
        long total = auditoriaRepository.contarPorUsuario(usuarioId);
        return PaginacaoDTOs.PaginaResponse.of(itens, total, paginaSegura, tamanhoSeguro, "criadoEm", "desc");
    }

    public PaginacaoDTOs.PaginaResponse<AuditoriaDTOs.AuditoriaResponse> listarRecentesPaginado(Integer pagina, Integer tamanho) {
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "criadoEm", "desc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = Math.min(paginacao.tamanhoSeguro(), 100);
        List<AuditoriaDTOs.AuditoriaResponse> itens = auditoriaRepository.listarRecentes(paginaSegura, tamanhoSeguro)
                .stream()
                .map(AuditoriaDTOs.AuditoriaResponse::from)
                .toList();
        long total = auditoriaRepository.contarTodos();
        return PaginacaoDTOs.PaginaResponse.of(itens, total, paginaSegura, tamanhoSeguro, "criadoEm", "desc");
    }

    /** Mantido para compatibilidade com chamadas internas antigas. */
    public List<AuditoriaDTOs.AuditoriaResponse> listarPorUsuario(Long usuarioId, int limite) {
        return listarPorUsuarioPaginado(usuarioId, 0, limite).itens();
    }

    /** Mantido para compatibilidade com chamadas internas antigas. */
    public List<AuditoriaDTOs.AuditoriaResponse> listarRecentes(int limite) {
        return listarRecentesPaginado(0, limite).itens();
    }

    public AuditoriaDTOs.ResumoAuditoriaResponse resumoUsuario(Long usuarioId) {
        long total = auditoriaRepository.contarPorUsuario(usuarioId);
        LocalDateTime desde = LocalDateTime.now().minusDays(7);
        long ultimosSeteDias = auditoriaRepository.contarPorUsuarioDesde(usuarioId, desde);
        AuditoriaAcesso ultimo = auditoriaRepository.buscarUltimaPorUsuario(usuarioId);

        return new AuditoriaDTOs.ResumoAuditoriaResponse(
                usuarioId,
                total,
                ultimosSeteDias,
                ultimo != null ? ultimo.acao : null,
                ultimo != null ? ultimo.referencia : null,
                ultimo != null ? ultimo.criadoEm : null
        );
    }

    private void registrarDados(Long usuarioId, String matricula, String acao, String referencia, String descricao) {
        AuditoriaAcesso auditoria = new AuditoriaAcesso();
        auditoria.usuarioId = usuarioId;
        auditoria.matricula = limitar(normalizar(matricula), 30);
        auditoria.acao = limitar(normalizar(acao), 50);
        auditoria.referencia = limitar(normalizar(referencia), 120);
        auditoria.descricao = limitar(normalizar(descricao), 500);
        auditoria.criadoEm = LocalDateTime.now();
        auditoriaRepository.persist(auditoria);
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private String limitar(String valor, int tamanho) {
        if (valor == null) return null;
        return valor.length() <= tamanho ? valor : valor.substring(0, tamanho);
    }
}

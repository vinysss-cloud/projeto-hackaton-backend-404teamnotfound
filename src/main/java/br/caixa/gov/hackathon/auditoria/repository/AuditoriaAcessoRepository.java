package br.caixa.gov.hackathon.auditoria.repository;

import br.caixa.gov.hackathon.auditoria.entity.AuditoriaAcesso;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class AuditoriaAcessoRepository implements PanacheRepositoryBase<AuditoriaAcesso, Long> {
    public List<AuditoriaAcesso> listarPorUsuario(Long usuarioId, int pagina, int tamanho) {
        return find("usuarioId = ?1 order by criadoEm desc", usuarioId).page(Page.of(pagina, tamanho)).list();
    }

    public long contarPorUsuario(Long usuarioId) {
        return count("usuarioId", usuarioId);
    }

    public long contarPorUsuarioDesde(Long usuarioId, LocalDateTime desde) {
        return count("usuarioId = ?1 and criadoEm >= ?2", usuarioId, desde);
    }

    public AuditoriaAcesso buscarUltimaPorUsuario(Long usuarioId) {
        return find("usuarioId = ?1 order by criadoEm desc", usuarioId).firstResult();
    }

    public List<AuditoriaAcesso> listarRecentes(int pagina, int tamanho) {
        return find("order by criadoEm desc").page(Page.of(pagina, tamanho)).list();
    }

    public long contarTodos() {
        return count();
    }
}

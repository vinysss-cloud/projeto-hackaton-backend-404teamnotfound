package br.caixa.gov.hackathon.sistema.repository;

import br.caixa.gov.hackathon.sistema.entity.SistemaUsuario;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class SistemaUsuarioRepository implements PanacheRepositoryBase<SistemaUsuario, Long> {
    public List<SistemaUsuario> listarAtivosPorUsuario(Long usuarioId, int pagina, int tamanho) {
        return find("usuario.id = ?1 and ativo = true order by favorito desc, nome asc", usuarioId).page(Page.of(pagina, tamanho)).list();
    }
    public long contarAtivosPorUsuario(Long usuarioId) { return count("usuario.id = ?1 and ativo = true", usuarioId); }
    public SistemaUsuario buscarAtivo(Long id) {
        SistemaUsuario sistema = findById(id);
        return sistema == null || !Boolean.TRUE.equals(sistema.ativo) ? null : sistema;
    }
}

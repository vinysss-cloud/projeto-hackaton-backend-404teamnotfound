package br.caixa.gov.hackathon.favorito.repository;

import br.caixa.gov.hackathon.favorito.entity.FavoritoUsuario;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FavoritoUsuarioRepository implements PanacheRepositoryBase<FavoritoUsuario, Long> {
    public FavoritoUsuario buscar(Long usuarioId, Long servicoId) {
        return find("usuario.id = ?1 and servico.id = ?2", usuarioId, servicoId).firstResult();
    }
    public List<FavoritoUsuario> listarAtivosPorUsuario(Long usuarioId, int pagina, int tamanho) {
        return find("usuario.id = ?1 and ativo = true order by dataCriacao desc, id desc", usuarioId).page(Page.of(pagina, tamanho)).list();
    }
    public long contarAtivosPorUsuario(Long usuarioId) { return count("usuario.id = ?1 and ativo = true", usuarioId); }
    public FavoritoUsuario buscarAtivo(Long id) {
        FavoritoUsuario favorito = findById(id);
        return favorito == null || !Boolean.TRUE.equals(favorito.ativo) ? null : favorito;
    }
}

package br.caixa.gov.hackathon.anotacao.repository;

import br.caixa.gov.hackathon.anotacao.entity.AnotacaoUsuario;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AnotacaoUsuarioRepository implements PanacheRepositoryBase<AnotacaoUsuario, Long> {
    public List<AnotacaoUsuario> listarAtivasPorUsuario(Long usuarioId, int pagina, int tamanho) {
        return find("usuario.id = ?1 and ativo = true order by dataAtualizacao desc, dataCriacao desc", usuarioId).page(Page.of(pagina, tamanho)).list();
    }
    public long contarAtivasPorUsuario(Long usuarioId) { return count("usuario.id = ?1 and ativo = true", usuarioId); }
    public AnotacaoUsuario buscarAtiva(Long id) {
        AnotacaoUsuario anotacao = findById(id);
        return anotacao == null || !Boolean.TRUE.equals(anotacao.ativo) ? null : anotacao;
    }
}

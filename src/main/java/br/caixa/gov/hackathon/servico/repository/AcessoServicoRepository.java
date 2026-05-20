package br.caixa.gov.hackathon.servico.repository;

import br.caixa.gov.hackathon.servico.entity.AcessoServico;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AcessoServicoRepository implements PanacheRepositoryBase<AcessoServico, Long> {
    public AcessoServico buscar(Long usuarioId, Long servicoId) {
        return find("usuario.id = ?1 and servico.id = ?2", usuarioId, servicoId).firstResult();
    }
    public List<AcessoServico> listarPorUsuario(Long usuarioId, int pagina, int tamanho) {
        return find("usuario.id = ?1 order by ultimoAcesso desc", usuarioId).page(Page.of(pagina, tamanho)).list();
    }
    public long contarPorUsuario(Long usuarioId) { return count("usuario.id", usuarioId); }
}

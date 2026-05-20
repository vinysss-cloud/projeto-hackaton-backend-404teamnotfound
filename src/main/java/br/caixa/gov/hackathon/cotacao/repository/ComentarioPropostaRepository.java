package br.caixa.gov.hackathon.cotacao.repository;

import br.caixa.gov.hackathon.cotacao.entity.ComentarioProposta;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ComentarioPropostaRepository implements PanacheRepositoryBase<ComentarioProposta, Long> {
    public List<ComentarioProposta> listarAtivosPorProposta(Long propostaId, int pagina, int tamanho) {
        return find("propostaId = ?1 and ativo = true order by criadoEm desc", propostaId).page(Page.of(pagina, tamanho)).list();
    }

    public long contarAtivosPorProposta(Long propostaId) {
        return count("propostaId = ?1 and ativo = true", propostaId);
    }
}

package br.caixa.gov.hackathon.cotacao.repository;

import br.caixa.gov.hackathon.cotacao.entity.HistoricoProposta;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class HistoricoPropostaRepository implements PanacheRepositoryBase<HistoricoProposta, Long> {
    public List<HistoricoProposta> listarPorProposta(Long propostaId, int pagina, int tamanho) {
        return find("propostaId = ?1 order by criadoEm desc", propostaId).page(Page.of(pagina, tamanho)).list();
    }

    public long contarPorProposta(Long propostaId) {
        return count("propostaId", propostaId);
    }
}

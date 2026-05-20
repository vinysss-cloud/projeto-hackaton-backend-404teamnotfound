package br.caixa.gov.hackathon.processo.repository;

import br.caixa.gov.hackathon.processo.entity.ChecklistProcesso;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ChecklistProcessoRepository implements PanacheRepositoryBase<ChecklistProcesso, Long> {
    public List<ChecklistProcesso> listarAtivosPorProcesso(Long processoId) {
        return list("processo.id = ?1 and ativo = true order by ordem asc, id asc", processoId);
    }
    public ChecklistProcesso buscarAtivo(Long id) {
        ChecklistProcesso item = findById(id);
        return item == null || !Boolean.TRUE.equals(item.ativo) ? null : item;
    }
}

package br.caixa.gov.hackathon.processo.repository;

import br.caixa.gov.hackathon.processo.entity.UsuarioChecklistProcesso;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioChecklistProcessoRepository implements PanacheRepositoryBase<UsuarioChecklistProcesso, Long> {
    public UsuarioChecklistProcesso buscar(Long usuarioId, Long checklistId) {
        return find("usuario.id = ?1 and checklist.id = ?2", usuarioId, checklistId).firstResult();
    }
}

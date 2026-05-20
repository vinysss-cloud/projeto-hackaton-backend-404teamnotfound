package br.caixa.gov.hackathon.usuario.repository;

import br.caixa.gov.hackathon.usuario.entity.PreferenciaUsuario;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PreferenciaUsuarioRepository implements PanacheRepositoryBase<PreferenciaUsuario, Long> {
    public PreferenciaUsuario buscarPorUsuario(Long usuarioId) {
        return find("usuario.id", usuarioId).firstResult();
    }
}

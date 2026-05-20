package br.caixa.gov.hackathon.usuario.repository;

import br.caixa.gov.hackathon.usuario.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepositoryBase<Usuario, Long> {
    public Usuario buscarPorMatricula(String matricula) {
        if (matricula == null || matricula.isBlank()) return null;
        return find("lower(matricula) = ?1", matricula.trim().toLowerCase()).firstResult();
    }
}

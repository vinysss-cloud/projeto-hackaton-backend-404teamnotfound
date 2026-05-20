package br.caixa.gov.hackathon.processo.entity;

import br.caixa.gov.hackathon.usuario.entity.Usuario;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario_checklist_processo", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_checklist", columnNames = {"usuario_id", "checklist_id"})
})
public class UsuarioChecklistProcesso extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    public Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "checklist_id", nullable = false)
    public ChecklistProcesso checklist;

    @Column(nullable = false)
    public Boolean concluido = Boolean.FALSE;

    @Column(name = "data_conclusao")
    public LocalDateTime dataConclusao;

}

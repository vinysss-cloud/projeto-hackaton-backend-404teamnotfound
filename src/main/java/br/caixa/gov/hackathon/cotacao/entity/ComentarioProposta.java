package br.caixa.gov.hackathon.cotacao.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios_proposta")
public class ComentarioProposta extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "proposta_id", nullable = false)
    public Long propostaId;

    @Column(nullable = false, length = 120)
    public String autor;

    @Column(name = "matricula_autor", length = 30)
    public String matriculaAutor;

    @Column(nullable = false, length = 1000)
    public String comentario;

    @Column(name = "criado_em", nullable = false)
    public LocalDateTime criadoEm;

    @Column(nullable = false)
    public Boolean ativo = Boolean.TRUE;
}

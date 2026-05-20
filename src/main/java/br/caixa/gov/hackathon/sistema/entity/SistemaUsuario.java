package br.caixa.gov.hackathon.sistema.entity;

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

import java.time.LocalDateTime;

@Entity
@Table(name = "sistemas_usuario")
public class SistemaUsuario extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    public Usuario usuario;

    @Column(nullable = false, length = 120)
    public String nome;

    @Column(length = 500)
    public String descricao;

    @Column(length = 300)
    public String url;

    @Column(length = 80)
    public String categoria;

    @Column(length = 80)
    public String icone;

    @Column(nullable = false)
    public Boolean favorito = Boolean.FALSE;

    @Column(nullable = false)
    public Boolean ativo = Boolean.TRUE;

    @Column(name = "data_criacao", nullable = false)
    public LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    public LocalDateTime dataAtualizacao;
}

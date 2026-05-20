package br.caixa.gov.hackathon.conteudo.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "conteudos_internos")
public class ConteudoInterno extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true, length = 60)
    public String codigo;

    @Column(nullable = false, length = 150)
    public String titulo;

    @Column(length = 200)
    public String subtitulo;

    @Lob
    public String descricao;

    @Column(nullable = false, length = 40)
    public String tipo;

    @Column(length = 120)
    public String slug;

    @Column(nullable = false)
    public Integer ordem = 0;

    @Column(nullable = false)
    public Boolean ativo = Boolean.TRUE;
}

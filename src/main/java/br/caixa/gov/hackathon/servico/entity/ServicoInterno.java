package br.caixa.gov.hackathon.servico.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "servicos_internos")
public class ServicoInterno extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true, length = 50)
    public String codigo;

    @Column(nullable = false, length = 120)
    public String titulo;

    @Column(length = 500)
    public String descricao;

    @Column(nullable = false, length = 80)
    public String categoria;

    @Column(name = "rota_frontend", length = 150)
    public String rotaFrontend;

    @Column(length = 80)
    public String icone;

    @Column(nullable = false)
    public Integer ordem = 0;

    @Column(nullable = false)
    public Boolean destaque = Boolean.FALSE;

    @Column(nullable = false)
    public Boolean ativo = Boolean.TRUE;
}

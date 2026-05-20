package br.caixa.gov.hackathon.processo.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "processos_internos")
public class ProcessoInterno extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true, length = 80)
    public String codigo;

    @Column(nullable = false, length = 150)
    public String titulo;

    @Lob
    public String descricao;

    @Column(length = 80)
    public String categoria;

    @Column(name = "rota_frontend", length = 150)
    public String rotaFrontend;

    @Column(length = 80)
    public String icone;

    @Column(nullable = false)
    public Integer ordem = 0;

    @Column(nullable = false)
    public Boolean ativo = Boolean.TRUE;
}

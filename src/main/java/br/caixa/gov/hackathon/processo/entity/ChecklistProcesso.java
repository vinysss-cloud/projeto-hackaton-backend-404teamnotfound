package br.caixa.gov.hackathon.processo.entity;

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

@Entity
@Table(name = "checklist_processo")
public class ChecklistProcesso extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "processo_id", nullable = false)
    public ProcessoInterno processo;

    @Column(nullable = false, length = 400)
    public String descricao;

    @Column(nullable = false)
    public Integer ordem = 0;

    @Column(nullable = false)
    public Boolean obrigatorio = Boolean.TRUE;

    @Column(nullable = false)
    public Boolean ativo = Boolean.TRUE;
}

package br.caixa.gov.hackathon.cotacao.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_proposta")
public class HistoricoProposta extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "proposta_id", nullable = false)
    public Long propostaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior", length = 30)
    public StatusProposta statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false, length = 30)
    public StatusProposta statusNovo;

    @Column(nullable = false, length = 120)
    public String usuario;

    @Column(name = "matricula_usuario", length = 30)
    public String matriculaUsuario;

    @Column(length = 500)
    public String descricao;

    @Column(name = "criado_em", nullable = false)
    public LocalDateTime criadoEm;
}

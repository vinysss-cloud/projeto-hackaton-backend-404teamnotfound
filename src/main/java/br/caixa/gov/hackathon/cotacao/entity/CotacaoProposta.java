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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cotacoes_propostas")
public class CotacaoProposta extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true, length = 40)
    public String numero;

    @Column(nullable = false, length = 20)
    public String agencia;

    @Column(name = "nome_cliente", nullable = false, length = 160)
    public String nomeCliente;

    @Column(name = "cpf_cnpj", length = 20)
    public String cpfCnpj;

    @Column(nullable = false, length = 120)
    public String produto;

    @Column(length = 120)
    public String modalidade;

    @Column(nullable = false, precision = 15, scale = 2)
    public BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    public StatusProposta status = StatusProposta.RASCUNHO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    public PrioridadeProposta prioridade = PrioridadeProposta.MEDIA;

    @Column(nullable = false, length = 120)
    public String responsavel;

    @Column(name = "matricula_responsavel", length = 30)
    public String matriculaResponsavel;

    @Column(name = "data_criacao", nullable = false)
    public LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    public LocalDateTime dataAtualizacao;

    @Column(name = "prazo_resposta")
    public LocalDate prazoResposta;

    @Column(length = 1000)
    public String observacao;

    @Column(nullable = false)
    public Boolean ativo = Boolean.TRUE;
}

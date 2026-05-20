package br.caixa.gov.hackathon.auditoria.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_acessos")
public class AuditoriaAcesso extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "usuario_id")
    public Long usuarioId;

    @Column(length = 30)
    public String matricula;

    @Column(nullable = false, length = 50)
    public String acao;

    @Column(length = 120)
    public String referencia;

    @Column(length = 500)
    public String descricao;

    @Column(name = "criado_em", nullable = false)
    public LocalDateTime criadoEm;
}

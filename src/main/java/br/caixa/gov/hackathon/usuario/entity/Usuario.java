package br.caixa.gov.hackathon.usuario.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true, length = 30)
    public String matricula;

    @Column(name = "nome_exibicao", length = 120)
    public String nomeExibicao;

    @Column(name = "senha_hash", nullable = false, length = 128)
    public String senhaHash;

    @Column(name = "senha_salt", nullable = false, length = 64)
    public String senhaSalt;

    @Column(nullable = false)
    public Boolean ativo = Boolean.TRUE;

    @Column(name = "primeiro_acesso", nullable = false)
    public Boolean primeiroAcesso = Boolean.TRUE;

    @Column(name = "data_criacao", nullable = false)
    public LocalDateTime dataCriacao;

    @Column(name = "ultimo_acesso")
    public LocalDateTime ultimoAcesso;

}

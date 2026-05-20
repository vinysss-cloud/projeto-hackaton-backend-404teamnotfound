package br.caixa.gov.hackathon.usuario.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "preferencias_usuario")
public class PreferenciaUsuario extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    public Usuario usuario;

    @Column(name = "tema", nullable = false, length = 30)
    public String tema = "PADRAO";

    @Column(name = "menu_compacto", nullable = false)
    public Boolean menuCompacto = Boolean.FALSE;

    @Column(name = "ultima_rota", length = 120)
    public String ultimaRota;

    @Column(name = "atualizado_em", nullable = false)
    public LocalDateTime atualizadoEm;

}

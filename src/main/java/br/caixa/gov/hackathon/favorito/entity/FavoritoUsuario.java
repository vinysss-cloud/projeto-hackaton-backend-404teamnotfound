package br.caixa.gov.hackathon.favorito.entity;

import br.caixa.gov.hackathon.servico.entity.ServicoInterno;
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
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(name = "favoritos_usuario", uniqueConstraints = {
        @UniqueConstraint(name = "uk_favorito_usuario_servico", columnNames = {"usuario_id", "servico_id"})
})
public class FavoritoUsuario extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    public Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servico_id", nullable = false)
    public ServicoInterno servico;

    @Column(name = "titulo_customizado", length = 120)
    public String tituloCustomizado;

    @Column(length = 500)
    public String observacao;

    @Column(nullable = false)
    public Boolean ativo = Boolean.TRUE;

    @Column(name = "data_criacao", nullable = false)
    public LocalDateTime dataCriacao;

}

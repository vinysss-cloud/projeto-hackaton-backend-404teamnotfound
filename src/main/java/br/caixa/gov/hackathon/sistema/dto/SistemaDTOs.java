package br.caixa.gov.hackathon.sistema.dto;

import br.caixa.gov.hackathon.sistema.entity.SistemaUsuario;

import java.time.LocalDateTime;

public class SistemaDTOs {

    public record SistemaRequest(
            Long usuarioId,
            String nome,
            String descricao,
            String url,
            String categoria,
            String icone,
            Boolean favorito
    ) {}

    public record SistemaResponse(
            Long id,
            Long usuarioId,
            String nome,
            String descricao,
            String url,
            String categoria,
            String icone,
            Boolean favorito,
            Boolean ativo,
            LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao
    ) {
        public static SistemaResponse from(SistemaUsuario sistema) {
            return new SistemaResponse(
                    sistema.id,
                    sistema.usuario.id,
                    sistema.nome,
                    sistema.descricao,
                    sistema.url,
                    sistema.categoria,
                    sistema.icone,
                    sistema.favorito,
                    sistema.ativo,
                    sistema.dataCriacao,
                    sistema.dataAtualizacao
            );
        }
    }

    public record AcessarSistemaResponse(
            Boolean registrado,
            String mensagem,
            SistemaResponse sistema,
            String url
    ) {}
}

package br.caixa.gov.hackathon.anotacao.dto;

import br.caixa.gov.hackathon.anotacao.entity.AnotacaoUsuario;

import java.time.LocalDateTime;

public class AnotacaoDTOs {

    public record AnotacaoRequest(
            Long usuarioId,
            String titulo,
            String descricao,
            String referencia
    ) {}

    public record AnotacaoResponse(
            Long id,
            Long usuarioId,
            String titulo,
            String descricao,
            String referencia,
            Boolean ativo,
            LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao
    ) {
        public static AnotacaoResponse from(AnotacaoUsuario anotacao) {
            return new AnotacaoResponse(
                    anotacao.id,
                    anotacao.usuario.id,
                    anotacao.titulo,
                    anotacao.descricao,
                    anotacao.referencia,
                    anotacao.ativo,
                    anotacao.dataCriacao,
                    anotacao.dataAtualizacao
            );
        }
    }
}

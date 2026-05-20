package br.caixa.gov.hackathon.favorito.dto;

import br.caixa.gov.hackathon.favorito.entity.FavoritoUsuario;

import java.time.LocalDateTime;

public class FavoritoDTOs {

    public record FavoritoRequest(
            Long usuarioId,
            Long servicoId,
            String tituloCustomizado,
            String observacao
    ) {}

    public record FavoritoResponse(
            Long id,
            Long usuarioId,
            Long servicoId,
            String codigoServico,
            String titulo,
            String descricao,
            String categoria,
            String rotaFrontend,
            String icone,
            String observacao,
            Boolean ativo,
            LocalDateTime dataCriacao
    ) {
        public static FavoritoResponse from(FavoritoUsuario favorito) {
            String titulo = favorito.tituloCustomizado != null && !favorito.tituloCustomizado.isBlank()
                    ? favorito.tituloCustomizado
                    : favorito.servico.titulo;
            return new FavoritoResponse(
                    favorito.id,
                    favorito.usuario.id,
                    favorito.servico.id,
                    favorito.servico.codigo,
                    titulo,
                    favorito.servico.descricao,
                    favorito.servico.categoria,
                    favorito.servico.rotaFrontend,
                    favorito.servico.icone,
                    favorito.observacao,
                    favorito.ativo,
                    favorito.dataCriacao
            );
        }
    }
}

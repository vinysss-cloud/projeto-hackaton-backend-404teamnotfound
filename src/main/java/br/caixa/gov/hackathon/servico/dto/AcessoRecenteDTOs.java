package br.caixa.gov.hackathon.servico.dto;

import br.caixa.gov.hackathon.servico.entity.AcessoServico;

import java.time.LocalDateTime;

public class AcessoRecenteDTOs {

    public record AcessoRecenteResponse(
            Long id,
            Long servicoId,
            String codigoServico,
            String titulo,
            String descricao,
            String categoria,
            String rotaFrontend,
            String icone,
            Integer quantidadeAcessos,
            LocalDateTime ultimoAcesso
    ) {
        public static AcessoRecenteResponse from(AcessoServico acesso) {
            return new AcessoRecenteResponse(
                    acesso.id,
                    acesso.servico.id,
                    acesso.servico.codigo,
                    acesso.servico.titulo,
                    acesso.servico.descricao,
                    acesso.servico.categoria,
                    acesso.servico.rotaFrontend,
                    acesso.servico.icone,
                    acesso.quantidadeAcessos,
                    acesso.ultimoAcesso
            );
        }
    }
}

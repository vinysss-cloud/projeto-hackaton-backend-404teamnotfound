package br.caixa.gov.hackathon.assistente.dto;

import java.util.List;

public class AssistenteDTOs {

    public record ResultadoBuscaResponse(
            String tipo,
            Long id,
            String titulo,
            String descricao,
            String categoria,
            String rotaFrontend,
            String icone
    ) {}

    public record BuscaResponse(
            String termo,
            Integer quantidade,
            List<ResultadoBuscaResponse> resultados
    ) {}
}

package br.caixa.gov.hackathon.monitoramento.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class MonitoramentoDTOs {

    public record StatusResponse(
            String status,
            String aplicacao,
            String ambiente,
            String banco,
            boolean bancoDisponivel,
            LocalDateTime timestamp,
            Map<String, Object> detalhes
    ) {}
}

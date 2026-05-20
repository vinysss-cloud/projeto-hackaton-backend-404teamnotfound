package br.caixa.gov.hackathon.common.dto;

import java.time.LocalDateTime;

/**
 * Envelope padronizado para respostas de sucesso da API.
 *
 * Objetivo: facilitar o consumo por qualquer frontend externo,
 * mantendo sempre os mesmos campos de controle.
 */
public record ApiResponse<T>(
        boolean sucesso,
        String mensagem,
        T dados,
        LocalDateTime timestamp
) {

    public static <T> ApiResponse<T> ok(T dados) {
        return new ApiResponse<>(true, "Operação realizada com sucesso.", dados, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> ok(String mensagem, T dados) {
        return new ApiResponse<>(true, mensagem, dados, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> created(T dados) {
        return new ApiResponse<>(true, "Registro criado com sucesso.", dados, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> created(String mensagem, T dados) {
        return new ApiResponse<>(true, mensagem, dados, LocalDateTime.now());
    }

    public static ApiResponse<Void> semConteudo(String mensagem) {
        return new ApiResponse<>(true, mensagem, null, LocalDateTime.now());
    }
}

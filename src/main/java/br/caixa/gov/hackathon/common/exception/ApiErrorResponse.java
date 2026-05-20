package br.caixa.gov.hackathon.common.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        String mensagem,
        int status,
        String erro,
        LocalDateTime timestamp
) {
    public static ApiErrorResponse of(String mensagem, int status, String erro) {
        return new ApiErrorResponse(mensagem, status, erro, LocalDateTime.now());
    }
}

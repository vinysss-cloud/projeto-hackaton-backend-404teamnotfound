package br.caixa.gov.hackathon.common.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Envelope padronizado para respostas de erro da API.
 *
 * Inclui path e correlationId para facilitar diagnóstico no frontend e backend.
 */
public record ApiErroResponse(
        boolean sucesso,
        int status,
        String codigo,
        String mensagem,
        String path,
        String correlationId,
        List<ErroCampoResponse> erros,
        LocalDateTime timestamp
) {

    public static ApiErroResponse of(int status, String codigo, String mensagem, String path, String correlationId) {
        return new ApiErroResponse(false, status, codigo, mensagem, path, correlationId, List.of(), LocalDateTime.now());
    }

    public static ApiErroResponse of(int status, String codigo, String mensagem, String path, String correlationId, List<ErroCampoResponse> erros) {
        return new ApiErroResponse(false, status, codigo, mensagem, path, correlationId, erros == null ? List.of() : erros, LocalDateTime.now());
    }

    /**
     * Compatibilidade com chamadas antigas internas. Prefira informar correlationId.
     */
    public static ApiErroResponse of(int status, String codigo, String mensagem, String path) {
        return of(status, codigo, mensagem, path, (String) null);
    }

    public static ApiErroResponse of(int status, String codigo, String mensagem, String path, List<ErroCampoResponse> erros) {
        return of(status, codigo, mensagem, path, (String) null, erros);
    }
}

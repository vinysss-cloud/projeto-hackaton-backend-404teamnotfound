package br.caixa.gov.hackathon.common.dto;

/**
 * Representa erro de validação em campo específico.
 */
public record ErroCampoResponse(
        String campo,
        String mensagem
) {
}

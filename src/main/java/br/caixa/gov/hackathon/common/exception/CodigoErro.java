package br.caixa.gov.hackathon.common.exception;

/**
 * Códigos funcionais estáveis para o frontend tratar erros sem depender do texto da mensagem.
 */
public enum CodigoErro {
    REQUISICAO_INVALIDA,
    NAO_AUTORIZADO,
    NAO_ENCONTRADO,
    CONFLITO,
    REGRA_NEGOCIO,
    ERRO_HTTP,
    ERRO_INTERNO
}

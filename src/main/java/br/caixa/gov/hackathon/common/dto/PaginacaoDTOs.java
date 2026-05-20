package br.caixa.gov.hackathon.common.dto;

import java.util.List;

/**
 * Contratos de paginação padrão para endpoints de listagem.
 *
 * Mantém o mesmo formato para o frontend externo consumir qualquer grid/lista
 * sem tratar cada endpoint de uma forma diferente.
 */
public final class PaginacaoDTOs {

    private PaginacaoDTOs() {}

    public static final int PAGINA_PADRAO = 0;
    public static final int TAMANHO_PADRAO = 10;
    public static final int TAMANHO_MAXIMO = 50;

    public record PaginacaoRequest(
            Integer pagina,
            Integer tamanho,
            String ordenarPor,
            String direcao
    ) {
        public int paginaSegura() {
            return pagina == null || pagina < 0 ? PAGINA_PADRAO : pagina;
        }

        public int tamanhoSeguro() {
            if (tamanho == null || tamanho <= 0) return TAMANHO_PADRAO;
            return Math.min(tamanho, TAMANHO_MAXIMO);
        }

        public String direcaoSegura() {
            if (direcao == null || direcao.isBlank()) return "desc";
            String valor = direcao.trim().toLowerCase();
            return valor.equals("asc") ? "asc" : "desc";
        }
    }

    public record PaginaResponse<T>(
            List<T> itens,
            long totalElementos,
            int totalPaginas,
            int pagina,
            int tamanho,
            boolean primeira,
            boolean ultima,
            boolean possuiProxima,
            boolean possuiAnterior,
            String ordenarPor,
            String direcao
    ) {
        public static <T> PaginaResponse<T> of(
                List<T> itens,
                long totalElementos,
                int pagina,
                int tamanho,
                String ordenarPor,
                String direcao
        ) {
            int totalPaginas = totalElementos == 0 ? 0 : (int) Math.ceil((double) totalElementos / tamanho);
            boolean primeira = pagina == 0;
            boolean ultima = totalPaginas == 0 || pagina >= totalPaginas - 1;
            return new PaginaResponse<>(
                    itens == null ? List.of() : itens,
                    totalElementos,
                    totalPaginas,
                    pagina,
                    tamanho,
                    primeira,
                    ultima,
                    !ultima,
                    !primeira,
                    ordenarPor,
                    direcao
            );
        }
    }
}

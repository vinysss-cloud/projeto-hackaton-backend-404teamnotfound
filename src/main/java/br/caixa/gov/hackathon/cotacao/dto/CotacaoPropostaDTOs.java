package br.caixa.gov.hackathon.cotacao.dto;

import br.caixa.gov.hackathon.cotacao.entity.ComentarioProposta;
import br.caixa.gov.hackathon.cotacao.entity.CotacaoProposta;
import br.caixa.gov.hackathon.cotacao.entity.HistoricoProposta;
import br.caixa.gov.hackathon.cotacao.entity.PrioridadeProposta;
import br.caixa.gov.hackathon.cotacao.entity.StatusProposta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CotacaoPropostaDTOs {

    public record CriarCotacaoPropostaRequest(
            @Size(max = 40, message = "Número da proposta deve ter no máximo 40 caracteres.")
            @Pattern(regexp = "^$|^[A-Za-z0-9._/-]+$", message = "Número da proposta contém caracteres inválidos.")
            String numero,

            @NotBlank(message = "Agência é obrigatória.")
            @Size(max = 10, message = "Agência deve ter no máximo 10 caracteres.")
            @Pattern(regexp = "^[0-9]+$", message = "Agência deve conter apenas números.")
            String agencia,

            @NotBlank(message = "Nome do cliente é obrigatório.")
            @Size(min = 3, max = 150, message = "Nome do cliente deve ter entre 3 e 150 caracteres.")
            String nomeCliente,

            @Size(max = 18, message = "CPF/CNPJ deve ter no máximo 18 caracteres.")
            @Pattern(regexp = "^$|^[0-9./-]{11,18}$", message = "CPF/CNPJ deve conter apenas números, ponto, barra ou hífen.")
            String cpfCnpj,

            @NotBlank(message = "Produto é obrigatório.")
            @Size(max = 120, message = "Produto deve ter no máximo 120 caracteres.")
            String produto,

            @Size(max = 120, message = "Modalidade deve ter no máximo 120 caracteres.")
            String modalidade,

            @DecimalMin(value = "0.00", message = "Valor não pode ser negativo.")
            BigDecimal valor,

            StatusProposta status,

            PrioridadeProposta prioridade,

            @NotBlank(message = "Responsável é obrigatório.")
            @Size(max = 120, message = "Responsável deve ter no máximo 120 caracteres.")
            String responsavel,

            @Size(max = 30, message = "Matrícula do responsável deve ter no máximo 30 caracteres.")
            @Pattern(regexp = "^$|^[A-Za-z0-9._-]+$", message = "Matrícula do responsável contém caracteres inválidos.")
            String matriculaResponsavel,

            LocalDate prazoResposta,

            @Size(max = 1000, message = "Observação deve ter no máximo 1000 caracteres.")
            String observacao
    ) {}

    public record AtualizarCotacaoPropostaRequest(
            @Size(max = 10, message = "Agência deve ter no máximo 10 caracteres.")
            @Pattern(regexp = "^$|^[0-9]+$", message = "Agência deve conter apenas números.")
            String agencia,

            @Size(min = 3, max = 150, message = "Nome do cliente deve ter entre 3 e 150 caracteres.")
            String nomeCliente,

            @Size(max = 18, message = "CPF/CNPJ deve ter no máximo 18 caracteres.")
            @Pattern(regexp = "^$|^[0-9./-]{11,18}$", message = "CPF/CNPJ deve conter apenas números, ponto, barra ou hífen.")
            String cpfCnpj,

            @Size(max = 120, message = "Produto deve ter no máximo 120 caracteres.")
            String produto,

            @Size(max = 120, message = "Modalidade deve ter no máximo 120 caracteres.")
            String modalidade,

            @DecimalMin(value = "0.00", message = "Valor não pode ser negativo.")
            BigDecimal valor,

            PrioridadeProposta prioridade,

            @Size(max = 120, message = "Responsável deve ter no máximo 120 caracteres.")
            String responsavel,

            @Size(max = 30, message = "Matrícula do responsável deve ter no máximo 30 caracteres.")
            @Pattern(regexp = "^$|^[A-Za-z0-9._-]+$", message = "Matrícula do responsável contém caracteres inválidos.")
            String matriculaResponsavel,

            LocalDate prazoResposta,

            @Size(max = 1000, message = "Observação deve ter no máximo 1000 caracteres.")
            String observacao,

            Boolean ativo
    ) {}

    public record AlterarStatusRequest(
            @NotNull(message = "Status é obrigatório.")
            StatusProposta status,

            @Size(max = 120, message = "Usuário deve ter no máximo 120 caracteres.")
            String usuario,

            @Size(max = 30, message = "Matrícula do usuário deve ter no máximo 30 caracteres.")
            @Pattern(regexp = "^$|^[A-Za-z0-9._-]+$", message = "Matrícula do usuário contém caracteres inválidos.")
            String matriculaUsuario,

            @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres.")
            String descricao
    ) {}

    public record AdicionarComentarioRequest(
            @NotBlank(message = "Autor é obrigatório.")
            @Size(max = 120, message = "Autor deve ter no máximo 120 caracteres.")
            String autor,

            @Size(max = 30, message = "Matrícula do autor deve ter no máximo 30 caracteres.")
            @Pattern(regexp = "^$|^[A-Za-z0-9._-]+$", message = "Matrícula do autor contém caracteres inválidos.")
            String matriculaAutor,

            @NotBlank(message = "Comentário é obrigatório.")
            @Size(max = 1000, message = "Comentário deve ter no máximo 1000 caracteres.")
            String comentario
    ) {}

    public record CotacaoPropostaResponse(
            Long id,
            String numero,
            String agencia,
            String nomeCliente,
            String cpfCnpj,
            String produto,
            String modalidade,
            BigDecimal valor,
            StatusProposta status,
            PrioridadeProposta prioridade,
            String responsavel,
            String matriculaResponsavel,
            LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao,
            LocalDate prazoResposta,
            String observacao,
            Boolean ativo
    ) {
        public static CotacaoPropostaResponse from(CotacaoProposta proposta) {
            return new CotacaoPropostaResponse(
                    proposta.id,
                    proposta.numero,
                    proposta.agencia,
                    proposta.nomeCliente,
                    proposta.cpfCnpj,
                    proposta.produto,
                    proposta.modalidade,
                    proposta.valor,
                    proposta.status,
                    proposta.prioridade,
                    proposta.responsavel,
                    proposta.matriculaResponsavel,
                    proposta.dataCriacao,
                    proposta.dataAtualizacao,
                    proposta.prazoResposta,
                    proposta.observacao,
                    proposta.ativo
            );
        }
    }

    public record CotacaoPropostaListaResponse(
            Long id,
            String numero,
            String agencia,
            String nomeCliente,
            String produto,
            String modalidade,
            BigDecimal valor,
            StatusProposta status,
            PrioridadeProposta prioridade,
            String responsavel,
            LocalDate prazoResposta,
            LocalDateTime dataAtualizacao
    ) {
        public static CotacaoPropostaListaResponse from(CotacaoProposta proposta) {
            return new CotacaoPropostaListaResponse(
                    proposta.id,
                    proposta.numero,
                    proposta.agencia,
                    proposta.nomeCliente,
                    proposta.produto,
                    proposta.modalidade,
                    proposta.valor,
                    proposta.status,
                    proposta.prioridade,
                    proposta.responsavel,
                    proposta.prazoResposta,
                    proposta.dataAtualizacao
            );
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
    ) {}

    public record HistoricoResponse(
            Long id,
            Long propostaId,
            StatusProposta statusAnterior,
            StatusProposta statusNovo,
            String usuario,
            String matriculaUsuario,
            String descricao,
            LocalDateTime criadoEm
    ) {
        public static HistoricoResponse from(HistoricoProposta historico) {
            return new HistoricoResponse(
                    historico.id,
                    historico.propostaId,
                    historico.statusAnterior,
                    historico.statusNovo,
                    historico.usuario,
                    historico.matriculaUsuario,
                    historico.descricao,
                    historico.criadoEm
            );
        }
    }

    public record ComentarioResponse(
            Long id,
            Long propostaId,
            String autor,
            String matriculaAutor,
            String comentario,
            LocalDateTime criadoEm
    ) {
        public static ComentarioResponse from(ComentarioProposta comentario) {
            return new ComentarioResponse(
                    comentario.id,
                    comentario.propostaId,
                    comentario.autor,
                    comentario.matriculaAutor,
                    comentario.comentario,
                    comentario.criadoEm
            );
        }
    }

    public record DetalheCotacaoPropostaResponse(
            CotacaoPropostaResponse proposta,
            List<HistoricoResponse> historico,
            List<ComentarioResponse> comentarios
    ) {}

    public record ResumoCotacoesResponse(
            long total,
            long rascunho,
            long emAnalise,
            long pendenteDocumentacao,
            long aprovadas,
            long reprovadas,
            long canceladas,
            BigDecimal valorTotal,
            BigDecimal valorAprovado
    ) {}

    public record FiltrosAplicadosResponse(
            String status,
            String prioridade,
            String agencia,
            String termo,
            LocalDate dataInicio,
            LocalDate dataFim,
            String ordenarPor,
            String direcao
    ) {}

    public record OpcoesCotacaoPropostaResponse(
            List<String> status,
            List<String> prioridades,
            List<String> camposOrdenacao,
            List<String> direcoes
    ) {}
}

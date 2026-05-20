package br.caixa.gov.hackathon.processo.dto;

import br.caixa.gov.hackathon.processo.entity.ChecklistProcesso;
import br.caixa.gov.hackathon.processo.entity.ProcessoInterno;
import br.caixa.gov.hackathon.processo.entity.UsuarioChecklistProcesso;

import java.time.LocalDateTime;
import java.util.List;

public class ProcessoDTOs {

    public record ProcessoResponse(
            Long id,
            String codigo,
            String titulo,
            String descricao,
            String categoria,
            String rotaFrontend,
            String icone,
            Integer ordem,
            Boolean ativo
    ) {
        public static ProcessoResponse from(ProcessoInterno processo) {
            return new ProcessoResponse(
                    processo.id,
                    processo.codigo,
                    processo.titulo,
                    processo.descricao,
                    processo.categoria,
                    processo.rotaFrontend,
                    processo.icone,
                    processo.ordem,
                    processo.ativo
            );
        }
    }

    public record ChecklistItemResponse(
            Long id,
            String descricao,
            Integer ordem,
            Boolean obrigatorio,
            Boolean concluido,
            LocalDateTime dataConclusao
    ) {
        public static ChecklistItemResponse from(ChecklistProcesso item, UsuarioChecklistProcesso estado) {
            boolean concluido = estado != null && Boolean.TRUE.equals(estado.concluido);
            return new ChecklistItemResponse(
                    item.id,
                    item.descricao,
                    item.ordem,
                    item.obrigatorio,
                    concluido,
                    estado == null ? null : estado.dataConclusao
            );
        }
    }

    public record ProcessoDetalheResponse(
            ProcessoResponse processo,
            List<ChecklistItemResponse> checklist,
            Integer totalItens,
            Integer itensConcluidos,
            Integer percentualConclusao
    ) {}

    public record MarcarChecklistRequest(
            Long usuarioId,
            Boolean concluido
    ) {}

    public record MarcarChecklistResponse(
            Boolean atualizado,
            String mensagem,
            ChecklistItemResponse item
    ) {}
}

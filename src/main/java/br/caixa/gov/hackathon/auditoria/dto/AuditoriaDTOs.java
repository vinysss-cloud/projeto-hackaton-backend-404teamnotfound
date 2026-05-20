package br.caixa.gov.hackathon.auditoria.dto;

import br.caixa.gov.hackathon.auditoria.entity.AuditoriaAcesso;

import java.time.LocalDateTime;

public class AuditoriaDTOs {
    public record AuditoriaResponse(
            Long id,
            Long usuarioId,
            String matricula,
            String acao,
            String referencia,
            String descricao,
            LocalDateTime criadoEm
    ) {
        public static AuditoriaResponse from(AuditoriaAcesso item) {
            return new AuditoriaResponse(item.id, item.usuarioId, item.matricula, item.acao, item.referencia, item.descricao, item.criadoEm);
        }
    }

    public record ResumoAuditoriaResponse(
            Long usuarioId,
            long totalAcoes,
            long acoesUltimosSeteDias,
            String ultimaAcao,
            String ultimaReferencia,
            LocalDateTime ultimaData
    ) {}
}

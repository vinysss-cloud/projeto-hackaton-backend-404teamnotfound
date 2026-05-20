package br.caixa.gov.hackathon.cotacao.repository;

import br.caixa.gov.hackathon.cotacao.entity.CotacaoProposta;
import br.caixa.gov.hackathon.cotacao.entity.PrioridadeProposta;
import br.caixa.gov.hackathon.cotacao.entity.StatusProposta;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class CotacaoPropostaRepository implements PanacheRepositoryBase<CotacaoProposta, Long> {

    private static final Set<String> CAMPOS_ORDENACAO_PERMITIDOS = Set.of(
            "id", "numero", "agencia", "nomeCliente", "produto", "status", "prioridade",
            "responsavel", "prazoResposta", "dataCriacao", "dataAtualizacao", "valor"
    );

    public ResultadoPaginado<CotacaoProposta> listar(
            String status,
            String prioridade,
            String agencia,
            String termo,
            LocalDate dataInicio,
            LocalDate dataFim,
            int pagina,
            int tamanho,
            String ordenarPor,
            String direcao
    ) {
        ConsultaDinamica consulta = montarConsulta(status, prioridade, agencia, termo, dataInicio, dataFim);
        long total = count(consulta.where(), consulta.params());
        PanacheQuery<CotacaoProposta> query = find(
                consulta.where() + " order by " + ordenarPor + " " + direcao + ", id desc",
                consulta.params()
        );
        List<CotacaoProposta> itens = query.page(Page.of(pagina, tamanho)).list();
        return new ResultadoPaginado<>(itens, total);
    }

    public List<CotacaoProposta> listarRecentes(int limite) {
        return find("ativo = true order by dataAtualizacao desc, id desc")
                .page(0, limite)
                .list();
    }

    public List<CotacaoProposta> listarAtivas() {
        return list("ativo = true");
    }

    public CotacaoProposta buscarAtiva(Long id) {
        CotacaoProposta proposta = findById(id);
        return proposta == null || Boolean.FALSE.equals(proposta.ativo) ? null : proposta;
    }

    public boolean existeNumero(String numero) {
        return count("numero", numero) > 0;
    }

    public String validarCampoOrdenacao(String ordenarPor) {
        if (ordenarPor == null || ordenarPor.isBlank()) return "dataAtualizacao";
        String campo = ordenarPor.trim();
        if (!CAMPOS_ORDENACAO_PERMITIDOS.contains(campo)) {
            throw new IllegalArgumentException("Campo de ordenação inválido: " + ordenarPor);
        }
        return campo;
    }

    public List<String> camposOrdenacaoPermitidos() {
        return CAMPOS_ORDENACAO_PERMITIDOS.stream().sorted().toList();
    }

    private ConsultaDinamica montarConsulta(String status, String prioridade, String agencia, String termo, LocalDate dataInicio, LocalDate dataFim) {
        StringBuilder where = new StringBuilder("ativo = true");
        Map<String, Object> params = new HashMap<>();

        if (status != null && !status.isBlank()) {
            where.append(" and status = :status");
            params.put("status", StatusProposta.valueOf(status.trim().toUpperCase(Locale.ROOT)));
        }

        if (prioridade != null && !prioridade.isBlank()) {
            where.append(" and prioridade = :prioridade");
            params.put("prioridade", PrioridadeProposta.valueOf(prioridade.trim().toUpperCase(Locale.ROOT)));
        }

        if (agencia != null && !agencia.isBlank()) {
            where.append(" and agencia = :agencia");
            params.put("agencia", agencia.trim());
        }

        if (termo != null && !termo.isBlank()) {
            where.append(" and (")
                    .append("lower(numero) like :termo or ")
                    .append("lower(nomeCliente) like :termo or ")
                    .append("lower(cpfCnpj) like :termo or ")
                    .append("lower(produto) like :termo or ")
                    .append("lower(modalidade) like :termo or ")
                    .append("lower(responsavel) like :termo")
                    .append(")");
            params.put("termo", "%" + termo.trim().toLowerCase(Locale.ROOT) + "%");
        }

        if (dataInicio != null) {
            where.append(" and dataCriacao >= :dataInicio");
            params.put("dataInicio", dataInicio.atStartOfDay());
        }

        if (dataFim != null) {
            where.append(" and dataCriacao < :dataFimExclusivo");
            params.put("dataFimExclusivo", dataFim.plusDays(1).atStartOfDay());
        }

        return new ConsultaDinamica(where.toString(), params);
    }

    private record ConsultaDinamica(String where, Map<String, Object> params) {}
    public record ResultadoPaginado<T>(List<T> itens, long total) {}
}

package br.caixa.gov.hackathon.processo.repository;

import br.caixa.gov.hackathon.processo.entity.ProcessoInterno;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProcessoInternoRepository implements PanacheRepositoryBase<ProcessoInterno, Long> {
    public List<ProcessoInterno> listarAtivos(String categoria, int pagina, int tamanho) {
        if (categoria != null && !categoria.isBlank()) {
            return find("ativo = true and lower(categoria) = ?1 order by ordem asc, titulo asc", categoria.trim().toLowerCase())
                    .page(Page.of(pagina, tamanho)).list();
        }
        return find("ativo = true order by ordem asc, titulo asc").page(Page.of(pagina, tamanho)).list();
    }
    public long contarAtivos(String categoria) {
        if (categoria != null && !categoria.isBlank()) return count("ativo = true and lower(categoria) = ?1", categoria.trim().toLowerCase());
        return count("ativo = true");
    }
    public ProcessoInterno buscarAtivo(Long id) {
        ProcessoInterno processo = findById(id);
        return processo == null || !Boolean.TRUE.equals(processo.ativo) ? null : processo;
    }
    public List<ProcessoInterno> buscarPorTermo(String like, int limite) {
        // Avoid applying lower() on CLOB fields (descricao) which causes errors in some DB dialects.
        // Search in titulo, categoria and codigo which are VARCHAR and should match the tests.
        return find("ativo = true and (lower(titulo) like ?1 or lower(categoria) like ?1 or lower(codigo) like ?1) order by ordem asc, titulo asc", like)
                .page(0, limite).list();
    }
}

package br.caixa.gov.hackathon.servico.repository;

import br.caixa.gov.hackathon.servico.entity.ServicoInterno;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ServicoInternoRepository implements PanacheRepositoryBase<ServicoInterno, Long> {
    public List<ServicoInterno> listarAtivos(String categoria, int pagina, int tamanho) {
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
    public List<ServicoInterno> listarDestaques(int pagina, int tamanho) {
        return find("ativo = true and destaque = true order by ordem asc, titulo asc").page(Page.of(pagina, tamanho)).list();
    }
    public ServicoInterno buscarAtivo(Long id) {
        ServicoInterno servico = findById(id);
        return servico == null || !Boolean.TRUE.equals(servico.ativo) ? null : servico;
    }
    public List<ServicoInterno> buscarPorTermo(String like, int limite) {
        return find("ativo = true and (lower(titulo) like ?1 or lower(descricao) like ?1 or lower(categoria) like ?1 or lower(codigo) like ?1) order by destaque desc, ordem asc, titulo asc", like)
                .page(0, limite).list();
    }
}

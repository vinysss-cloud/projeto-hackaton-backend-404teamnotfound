package br.caixa.gov.hackathon.conteudo.repository;

import br.caixa.gov.hackathon.conteudo.entity.ConteudoInterno;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ConteudoInternoRepository implements PanacheRepositoryBase<ConteudoInterno, Long> {
    public List<ConteudoInterno> listarAtivos(int pagina, int tamanho) {
        return find("ativo = true order by ordem asc, id asc").page(Page.of(pagina, tamanho)).list();
    }
    public long contarAtivos() { return count("ativo = true"); }
    public List<ConteudoInterno> listarConteudosNaoNormativos(int pagina, int tamanho) {
        return find("ativo = true and tipo <> 'NORMATIVO' order by ordem asc, id asc").page(Page.of(pagina, tamanho)).list();
    }
    public List<ConteudoInterno> listarNormativos(int pagina, int tamanho) {
        return find("ativo = true and tipo = 'NORMATIVO' order by ordem asc, id asc").page(Page.of(pagina, tamanho)).list();
    }
    public long contarNormativos() { return count("ativo = true and tipo = 'NORMATIVO'"); }
    public ConteudoInterno buscarAtivo(Long id) {
        ConteudoInterno conteudo = findById(id);
        return conteudo == null || !Boolean.TRUE.equals(conteudo.ativo) ? null : conteudo;
    }
    public List<ConteudoInterno> buscarPorTermo(String like, int limite) {
        // Avoid applying lower() on CLOB fields (descricao). Search in varchar fields instead.
        return find("ativo = true and (lower(titulo) like ?1 or lower(tipo) like ?1 or lower(codigo) like ?1) order by ordem asc, titulo asc", like)
                .page(0, limite).list();
    }
}

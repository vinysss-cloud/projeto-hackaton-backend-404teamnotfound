package br.caixa.gov.hackathon.portal.dto;

import br.caixa.gov.hackathon.anotacao.dto.AnotacaoDTOs;
import br.caixa.gov.hackathon.cotacao.dto.CotacaoPropostaDTOs;
import br.caixa.gov.hackathon.favorito.dto.FavoritoDTOs;
import br.caixa.gov.hackathon.processo.dto.ProcessoDTOs;
import br.caixa.gov.hackathon.servico.dto.AcessoRecenteDTOs;

import br.caixa.gov.hackathon.conteudo.entity.ConteudoInterno;
import br.caixa.gov.hackathon.servico.entity.ServicoInterno;
import br.caixa.gov.hackathon.usuario.entity.Usuario;

import java.util.List;

public class PortalDTOs {

    public record HeaderResponse(
            String produto,
            String modulo,
            String descricao,
            String tag,
            String versao
    ) {}

    public record MenuItemResponse(
            String label,
            String rota,
            String icone,
            Integer ordem
    ) {}

    public record ServicoResponse(
            Long id,
            String codigo,
            String titulo,
            String descricao,
            String categoria,
            String rotaFrontend,
            String icone,
            Integer ordem,
            Boolean destaque,
            Boolean ativo
    ) {
        public static ServicoResponse from(ServicoInterno servico) {
            return new ServicoResponse(
                    servico.id,
                    servico.codigo,
                    servico.titulo,
                    servico.descricao,
                    servico.categoria,
                    servico.rotaFrontend,
                    servico.icone,
                    servico.ordem,
                    servico.destaque,
                    servico.ativo
            );
        }
    }

    public record ConteudoResponse(
            Long id,
            String codigo,
            String titulo,
            String subtitulo,
            String descricao,
            String tipo,
            String slug,
            Integer ordem
    ) {
        public static ConteudoResponse from(ConteudoInterno conteudo) {
            return new ConteudoResponse(
                    conteudo.id,
                    conteudo.codigo,
                    conteudo.titulo,
                    conteudo.subtitulo,
                    conteudo.descricao,
                    conteudo.tipo,
                    conteudo.slug,
                    conteudo.ordem
            );
        }
    }

    public record ResumoUsuarioResponse(
            Long id,
            String matricula,
            String nomeExibicao,
            Boolean primeiroAcesso
    ) {
        public static ResumoUsuarioResponse from(Usuario usuario) {
            return new ResumoUsuarioResponse(usuario.id, usuario.matricula, usuario.nomeExibicao, usuario.primeiroAcesso);
        }
    }

    public record PortalInicialResponse(
            HeaderResponse header,
            ResumoUsuarioResponse usuario,
            List<MenuItemResponse> menu,
            CotacaoPropostaDTOs.ResumoCotacoesResponse resumoCotacoesPropostas,
            List<CotacaoPropostaDTOs.CotacaoPropostaResponse> cotacoesPropostasRecentes,
            List<ConteudoResponse> conteudos,
            List<ConteudoResponse> normativos,
            List<ServicoResponse> servicosDestaque,
            List<ServicoResponse> servicos,
            List<FavoritoDTOs.FavoritoResponse> favoritos,
            List<AcessoRecenteDTOs.AcessoRecenteResponse> acessosRecentes,
            List<AnotacaoDTOs.AnotacaoResponse> anotacoes,
            List<ProcessoDTOs.ProcessoResponse> processos
    ) {}

    public record AcessarServicoResponse(
            Boolean registrado,
            String mensagem,
            ServicoResponse servico,
            String rotaFrontend
    ) {}
}

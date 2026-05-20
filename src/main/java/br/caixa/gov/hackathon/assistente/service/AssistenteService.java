package br.caixa.gov.hackathon.assistente.service;

import br.caixa.gov.hackathon.auditoria.service.AuditoriaService;

import br.caixa.gov.hackathon.assistente.dto.AssistenteDTOs;
import br.caixa.gov.hackathon.conteudo.repository.ConteudoInternoRepository;
import br.caixa.gov.hackathon.processo.repository.ProcessoInternoRepository;
import br.caixa.gov.hackathon.servico.repository.ServicoInternoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class AssistenteService {

    @Inject
    AuditoriaService auditoriaService;

    @Inject
    ServicoInternoRepository servicoRepository;

    @Inject
    ProcessoInternoRepository processoRepository;

    @Inject
    ConteudoInternoRepository conteudoRepository;

    public AssistenteDTOs.BuscaResponse buscar(String termo) {
        return buscar(termo, null);
    }

    public AssistenteDTOs.BuscaResponse buscar(String termo, Long usuarioId) {
        String termoLimpo = termo == null ? "" : termo.trim().toLowerCase();
        if (termoLimpo.length() < 2) {
            auditoriaService.registrarPorUsuarioId(usuarioId, "ASSISTENTE_BUSCA_INVALIDA", "ASSISTENTE", "Busca orientada ignorada por termo insuficiente.");
            return new AssistenteDTOs.BuscaResponse(termoLimpo, 0, List.of());
        }

        String like = "%" + termoLimpo + "%";
        List<AssistenteDTOs.ResultadoBuscaResponse> resultados = new ArrayList<>();

        servicoRepository.buscarPorTermo(like, 6).forEach(servico -> resultados.add(new AssistenteDTOs.ResultadoBuscaResponse(
                "SERVICO", servico.id, servico.titulo, servico.descricao, servico.categoria, servico.rotaFrontend, servico.icone
        )));

        processoRepository.buscarPorTermo(like, 6).forEach(processo -> resultados.add(new AssistenteDTOs.ResultadoBuscaResponse(
                "PROCESSO", processo.id, processo.titulo, processo.descricao, processo.categoria, processo.rotaFrontend, processo.icone
        )));

        conteudoRepository.buscarPorTermo(like, 6).forEach(conteudo -> resultados.add(new AssistenteDTOs.ResultadoBuscaResponse(
                conteudo.tipo, conteudo.id, conteudo.titulo, conteudo.subtitulo, conteudo.tipo, "/conteudos/" + conteudo.slug, "file-text"
        )));

        AssistenteDTOs.BuscaResponse response = new AssistenteDTOs.BuscaResponse(termoLimpo, resultados.size(), resultados.stream().limit(15).toList());
        auditoriaService.registrarPorUsuarioId(
                usuarioId,
                "ASSISTENTE_BUSCA_REALIZADA",
                "ASSISTENTE:q=" + termoLimpo,
                "Busca orientada realizada com " + response.quantidade() + " resultado(s)."
        );
        return response;
    }
}

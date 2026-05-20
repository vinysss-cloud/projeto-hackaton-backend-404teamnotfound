package br.caixa.gov.hackathon.processo.service;

import br.caixa.gov.hackathon.auditoria.service.AuditoriaService;
import br.caixa.gov.hackathon.processo.entity.UsuarioChecklistProcesso;
import br.caixa.gov.hackathon.usuario.service.UsuarioService;

import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.processo.dto.ProcessoDTOs;
import br.caixa.gov.hackathon.processo.entity.ChecklistProcesso;
import br.caixa.gov.hackathon.processo.entity.ProcessoInterno;
import br.caixa.gov.hackathon.usuario.entity.Usuario;
import br.caixa.gov.hackathon.processo.repository.ChecklistProcessoRepository;
import br.caixa.gov.hackathon.processo.repository.ProcessoInternoRepository;
import br.caixa.gov.hackathon.processo.repository.UsuarioChecklistProcessoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ProcessoService {

    @Inject
    UsuarioService usuarioService;

    @Inject
    AuditoriaService auditoriaService;

    @Inject
    ProcessoInternoRepository processoRepository;

    @Inject
    ChecklistProcessoRepository checklistRepository;

    @Inject
    UsuarioChecklistProcessoRepository usuarioChecklistRepository;

    public PaginacaoDTOs.PaginaResponse<ProcessoDTOs.ProcessoResponse> listarPaginado(String categoria, Integer pagina, Integer tamanho) {
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "ordem", "asc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = paginacao.tamanhoSeguro();
        List<ProcessoDTOs.ProcessoResponse> itens = processoRepository.listarAtivos(categoria, paginaSegura, tamanhoSeguro)
                .stream().map(ProcessoDTOs.ProcessoResponse::from).toList();
        long total = processoRepository.contarAtivos(categoria);
        return PaginacaoDTOs.PaginaResponse.of(itens, total, paginaSegura, tamanhoSeguro, "ordem", "asc");
    }

    public List<ProcessoDTOs.ProcessoResponse> listar(String categoria) {
        return listarPaginado(categoria, 0, 50).itens();
    }

    public ProcessoDTOs.ProcessoDetalheResponse detalhar(Long processoId, Long usuarioId) {
        if (usuarioId != null) {
            usuarioService.buscarUsuario(usuarioId);
        }
        ProcessoInterno processo = buscarProcesso(processoId);
        List<ChecklistProcesso> itens = checklistRepository.listarAtivosPorProcesso(processo.id);
        List<ProcessoDTOs.ChecklistItemResponse> checklist = itens.stream()
                .map(item -> ProcessoDTOs.ChecklistItemResponse.from(item, usuarioId == null ? null : usuarioChecklistRepository.buscar(usuarioId, item.id)))
                .toList();
        int total = checklist.size();
        int concluidos = (int) checklist.stream().filter(item -> Boolean.TRUE.equals(item.concluido())).count();
        int percentual = total == 0 ? 0 : Math.round((concluidos * 100f) / total);
        return new ProcessoDTOs.ProcessoDetalheResponse(ProcessoDTOs.ProcessoResponse.from(processo), checklist, total, concluidos, percentual);
    }

    @Transactional
    public ProcessoDTOs.MarcarChecklistResponse marcarChecklist(Long checklistId, ProcessoDTOs.MarcarChecklistRequest request) {
        if (request == null || request.usuarioId() == null) {
            throw new NotFoundException("Informe o usuário para atualizar o checklist.");
        }
        Usuario usuario = usuarioService.buscarUsuario(request.usuarioId());
        ChecklistProcesso item = checklistRepository.buscarAtivo(checklistId);
        if (item == null) {
            throw new NotFoundException("Item de checklist não encontrado.");
        }

        UsuarioChecklistProcesso estado = usuarioChecklistRepository.buscar(usuario.id, item.id);
        if (estado == null) {
            estado = new UsuarioChecklistProcesso();
            estado.usuario = usuario;
            estado.checklist = item;
            usuarioChecklistRepository.persist(estado);
        }

        boolean concluido = request.concluido() == null || Boolean.TRUE.equals(request.concluido());
        estado.concluido = concluido;
        estado.dataConclusao = concluido ? LocalDateTime.now() : null;

        auditoriaService.registrar(usuario, "CHECKLIST_PROCESSO_ATUALIZADO", item.processo.codigo,
                "Usuário atualizou checklist do processo: " + item.processo.titulo);

        return new ProcessoDTOs.MarcarChecklistResponse(
                true,
                concluido ? "Item marcado como concluído." : "Item desmarcado.",
                ProcessoDTOs.ChecklistItemResponse.from(item, estado)
        );
    }

    private ProcessoInterno buscarProcesso(Long id) {
        ProcessoInterno processo = processoRepository.buscarAtivo(id);
        if (processo == null) {
            throw new NotFoundException("Processo não encontrado.");
        }
        return processo;
    }
}

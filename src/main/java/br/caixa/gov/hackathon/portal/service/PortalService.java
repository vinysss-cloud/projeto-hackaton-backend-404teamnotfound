package br.caixa.gov.hackathon.portal.service;

import br.caixa.gov.hackathon.auditoria.service.AuditoriaService;
import br.caixa.gov.hackathon.cotacao.service.CotacaoPropostaService;
import br.caixa.gov.hackathon.usuario.service.UsuarioService;

import br.caixa.gov.hackathon.servico.dto.AcessoRecenteDTOs;
import br.caixa.gov.hackathon.anotacao.dto.AnotacaoDTOs;
import br.caixa.gov.hackathon.cotacao.dto.CotacaoPropostaDTOs;
import br.caixa.gov.hackathon.favorito.dto.FavoritoDTOs;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.portal.dto.PortalDTOs;
import br.caixa.gov.hackathon.processo.dto.ProcessoDTOs;
import br.caixa.gov.hackathon.servico.entity.AcessoServico;
import br.caixa.gov.hackathon.servico.entity.ServicoInterno;
import br.caixa.gov.hackathon.usuario.entity.Usuario;
import br.caixa.gov.hackathon.servico.repository.AcessoServicoRepository;
import br.caixa.gov.hackathon.anotacao.repository.AnotacaoUsuarioRepository;
import br.caixa.gov.hackathon.conteudo.repository.ConteudoInternoRepository;
import br.caixa.gov.hackathon.favorito.repository.FavoritoUsuarioRepository;
import br.caixa.gov.hackathon.processo.repository.ProcessoInternoRepository;
import br.caixa.gov.hackathon.servico.repository.ServicoInternoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class PortalService {

    @Inject
    UsuarioService usuarioService;

    @Inject
    AuditoriaService auditoriaService;

    @Inject
    CotacaoPropostaService cotacaoPropostaService;

    @Inject
    ConteudoInternoRepository conteudoRepository;

    @Inject
    ServicoInternoRepository servicoRepository;

    @Inject
    FavoritoUsuarioRepository favoritoRepository;

    @Inject
    AcessoServicoRepository acessoRepository;

    @Inject
    AnotacaoUsuarioRepository anotacaoRepository;

    @Inject
    ProcessoInternoRepository processoRepository;

    public PortalDTOs.PortalInicialResponse carregarPortalInicial(Long usuarioId) {
        Usuario usuario = usuarioService.buscarUsuario(usuarioId);
        auditoriaService.registrar(usuario, "PORTAL_INICIAL_ACESSADO", "PORTAL", "Usuário carregou o portal inicial.");

        List<PortalDTOs.ConteudoResponse> conteudos = conteudoRepository.listarConteudosNaoNormativos(0, 10)
                .stream().map(PortalDTOs.ConteudoResponse::from).toList();

        List<PortalDTOs.ConteudoResponse> normativos = conteudoRepository.listarNormativos(0, 10)
                .stream().map(PortalDTOs.ConteudoResponse::from).toList();

        List<PortalDTOs.ServicoResponse> servicos = servicoRepository.listarAtivos(null, 0, 12)
                .stream().map(PortalDTOs.ServicoResponse::from).toList();

        List<PortalDTOs.ServicoResponse> destaques = servicoRepository.listarDestaques(0, 8)
                .stream().map(PortalDTOs.ServicoResponse::from).toList();

        List<FavoritoDTOs.FavoritoResponse> favoritos = favoritoRepository.listarAtivosPorUsuario(usuario.id, 0, 8)
                .stream().map(FavoritoDTOs.FavoritoResponse::from).toList();

        List<AcessoRecenteDTOs.AcessoRecenteResponse> acessosRecentes = acessoRepository.listarPorUsuario(usuario.id, 0, 8)
                .stream().map(AcessoRecenteDTOs.AcessoRecenteResponse::from).toList();

        List<AnotacaoDTOs.AnotacaoResponse> anotacoes = anotacaoRepository.listarAtivasPorUsuario(usuario.id, 0, 5)
                .stream().map(AnotacaoDTOs.AnotacaoResponse::from).toList();

        List<ProcessoDTOs.ProcessoResponse> processos = processoRepository.listarAtivos(null, 0, 10)
                .stream().map(ProcessoDTOs.ProcessoResponse::from).toList();

        CotacaoPropostaDTOs.ResumoCotacoesResponse resumoCotacoes = cotacaoPropostaService.resumo();
        List<CotacaoPropostaDTOs.CotacaoPropostaResponse> cotacoesRecentes = cotacaoPropostaService.listarRecentes(6);

        return new PortalDTOs.PortalInicialResponse(
                header(),
                PortalDTOs.ResumoUsuarioResponse.from(usuario),
                menu(),
                resumoCotacoes,
                cotacoesRecentes,
                conteudos,
                normativos,
                destaques,
                servicos,
                favoritos,
                acessosRecentes,
                anotacoes,
                processos
        );
    }

    public PaginacaoDTOs.PaginaResponse<PortalDTOs.ServicoResponse> listarServicosPaginado(String categoria, Integer pagina, Integer tamanho) {
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "ordem", "asc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = paginacao.tamanhoSeguro();
        List<PortalDTOs.ServicoResponse> itens = servicoRepository.listarAtivos(categoria, paginaSegura, tamanhoSeguro)
                .stream().map(PortalDTOs.ServicoResponse::from).toList();
        long total = servicoRepository.contarAtivos(categoria);
        return PaginacaoDTOs.PaginaResponse.of(itens, total, paginaSegura, tamanhoSeguro, "ordem", "asc");
    }

    public List<PortalDTOs.ServicoResponse> listarServicos(String categoria) {
        return listarServicosPaginado(categoria, 0, 50).itens();
    }

    public PortalDTOs.ServicoResponse buscarServico(Long id) {
        ServicoInterno servico = servicoRepository.buscarAtivo(id);
        if (servico == null) {
            throw new NotFoundException("Serviço não encontrado.");
        }
        return PortalDTOs.ServicoResponse.from(servico);
    }

    @Transactional
    public PortalDTOs.AcessarServicoResponse acessarServico(Long servicoId, Long usuarioId) {
        Usuario usuario = usuarioService.buscarUsuario(usuarioId);
        ServicoInterno servico = servicoRepository.buscarAtivo(servicoId);
        if (servico == null) {
            throw new NotFoundException("Serviço não encontrado.");
        }

        AcessoServico acesso = acessoRepository.buscar(usuario.id, servico.id);
        if (acesso == null) {
            acesso = new AcessoServico();
            acesso.usuario = usuario;
            acesso.servico = servico;
            acesso.quantidadeAcessos = 0;
            acessoRepository.persist(acesso);
        }
        acesso.quantidadeAcessos = acesso.quantidadeAcessos + 1;
        acesso.ultimoAcesso = LocalDateTime.now();

        auditoriaService.registrar(usuario, "SERVICO_ACESSADO", servico.codigo, "Usuário acessou serviço interno: " + servico.titulo);
        return new PortalDTOs.AcessarServicoResponse(true, "Acesso registrado.", PortalDTOs.ServicoResponse.from(servico), servico.rotaFrontend);
    }

    public PortalDTOs.HeaderResponse header() {
        return new PortalDTOs.HeaderResponse("CAIXA HUB", "SICAS", "Cotações e propostas da agência", "#INTERNO.CAIXA", "1.0.0");
    }

    public List<PortalDTOs.MenuItemResponse> menu() {
        return List.of(
                new PortalDTOs.MenuItemResponse("Início", "/home", "home", 1),
                new PortalDTOs.MenuItemResponse("Cotações e Propostas", "/cotacoes-propostas", "table", 2),
                new PortalDTOs.MenuItemResponse("Meus Sistemas", "/meus-sistemas", "grid", 3),
                new PortalDTOs.MenuItemResponse("Acessos Recentes", "/acessos-recentes", "clock", 4),
                new PortalDTOs.MenuItemResponse("Normativos", "/normativos", "file-text", 5)
        );
    }
}

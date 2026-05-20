package br.caixa.gov.hackathon.cotacao.service;

import br.caixa.gov.hackathon.auditoria.service.AuditoriaService;

import br.caixa.gov.hackathon.cotacao.dto.CotacaoPropostaDTOs;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.cotacao.entity.ComentarioProposta;
import br.caixa.gov.hackathon.cotacao.entity.CotacaoProposta;
import br.caixa.gov.hackathon.cotacao.entity.HistoricoProposta;
import br.caixa.gov.hackathon.cotacao.entity.PrioridadeProposta;
import br.caixa.gov.hackathon.cotacao.entity.StatusProposta;
import br.caixa.gov.hackathon.cotacao.repository.ComentarioPropostaRepository;
import br.caixa.gov.hackathon.cotacao.repository.CotacaoPropostaRepository;
import br.caixa.gov.hackathon.cotacao.repository.HistoricoPropostaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@ApplicationScoped
public class CotacaoPropostaService {

    @Inject
    AuditoriaService auditoriaService;

    @Inject
    CotacaoPropostaRepository cotacaoRepository;

    @Inject
    HistoricoPropostaRepository historicoRepository;

    @Inject
    ComentarioPropostaRepository comentarioRepository;

    private static final int TAMANHO_PADRAO = 10;
    private static final int TAMANHO_MAXIMO = 50;

    public List<CotacaoPropostaDTOs.CotacaoPropostaResponse> listar(String status, String agencia, String termo) {
        return listarPaginado(status, null, agencia, termo, null, null, 0, TAMANHO_MAXIMO, "dataAtualizacao", "desc")
                .itens()
                .stream()
                .map(item -> CotacaoPropostaDTOs.CotacaoPropostaResponse.from(buscarEntidade(item.id())))
                .toList();
    }

    public PaginacaoDTOs.PaginaResponse<CotacaoPropostaDTOs.CotacaoPropostaListaResponse> listarPaginado(
            String status,
            String prioridade,
            String agencia,
            String termo,
            LocalDate dataInicio,
            LocalDate dataFim,
            Integer pagina,
            Integer tamanho,
            String ordenarPor,
            String direcao
    ) {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new BadRequestException("dataInicio não pode ser maior que dataFim.");
        }

        int paginaSegura = normalizarPagina(pagina);
        int tamanhoSeguro = normalizarTamanho(tamanho);
        String campoOrdenacao = normalizarCampoOrdenacao(ordenarPor);
        String direcaoOrdenacao = normalizarDirecao(direcao);

        try {
            CotacaoPropostaRepository.ResultadoPaginado<CotacaoProposta> resultado = cotacaoRepository.listar(
                    normalizarEnumTexto(status),
                    normalizarEnumTexto(prioridade),
                    normalizar(agencia),
                    normalizar(termo),
                    dataInicio,
                    dataFim,
                    paginaSegura,
                    tamanhoSeguro,
                    campoOrdenacao,
                    direcaoOrdenacao
            );

            List<CotacaoPropostaDTOs.CotacaoPropostaListaResponse> itens = resultado.itens()
                    .stream()
                    .map(CotacaoPropostaDTOs.CotacaoPropostaListaResponse::from)
                    .toList();

            return PaginacaoDTOs.PaginaResponse.of(
                    itens,
                    resultado.total(),
                    paginaSegura,
                    tamanhoSeguro,
                    campoOrdenacao,
                    direcaoOrdenacao
            );
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Filtro inválido para cotações/propostas: " + e.getMessage());
        }
    }

    public CotacaoPropostaDTOs.FiltrosAplicadosResponse filtrosAplicados(
            String status,
            String prioridade,
            String agencia,
            String termo,
            LocalDate dataInicio,
            LocalDate dataFim,
            String ordenarPor,
            String direcao
    ) {
        return new CotacaoPropostaDTOs.FiltrosAplicadosResponse(
                normalizarEnumTexto(status),
                normalizarEnumTexto(prioridade),
                normalizar(agencia),
                normalizar(termo),
                dataInicio,
                dataFim,
                normalizarCampoOrdenacao(ordenarPor),
                normalizarDirecao(direcao)
        );
    }

    public CotacaoPropostaDTOs.OpcoesCotacaoPropostaResponse opcoes() {
        return new CotacaoPropostaDTOs.OpcoesCotacaoPropostaResponse(
                Arrays.stream(StatusProposta.values()).map(Enum::name).toList(),
                Arrays.stream(PrioridadeProposta.values()).map(Enum::name).toList(),
                cotacaoRepository.camposOrdenacaoPermitidos(),
                List.of("asc", "desc")
        );
    }

    public List<CotacaoPropostaDTOs.CotacaoPropostaResponse> listarRecentes(int limite) {
        int limiteSeguro = limite <= 0 ? 6 : Math.min(limite, 20);
        return cotacaoRepository.listarRecentes(limiteSeguro)
                .stream()
                .map(CotacaoPropostaDTOs.CotacaoPropostaResponse::from)
                .toList();
    }

    public CotacaoPropostaDTOs.DetalheCotacaoPropostaResponse detalhar(Long id) {
        CotacaoProposta proposta = buscarEntidade(id);
        List<CotacaoPropostaDTOs.HistoricoResponse> historico = historicoRepository.listarPorProposta(id, 0, 50)
                .stream()
                .map(CotacaoPropostaDTOs.HistoricoResponse::from)
                .toList();

        List<CotacaoPropostaDTOs.ComentarioResponse> comentarios = comentarioRepository.listarAtivosPorProposta(id, 0, 50)
                .stream()
                .map(CotacaoPropostaDTOs.ComentarioResponse::from)
                .toList();

        return new CotacaoPropostaDTOs.DetalheCotacaoPropostaResponse(
                CotacaoPropostaDTOs.CotacaoPropostaResponse.from(proposta),
                historico,
                comentarios
        );
    }

    @Transactional
    public CotacaoPropostaDTOs.CotacaoPropostaResponse criar(CotacaoPropostaDTOs.CriarCotacaoPropostaRequest request) {
        validarCriacao(request);

        String numero = request.numero() == null || request.numero().isBlank()
                ? gerarNumero()
                : request.numero().trim();

        if (cotacaoRepository.existeNumero(numero)) {
            throw new WebApplicationException("Já existe cotação/proposta com o número informado.", Response.Status.CONFLICT);
        }

        LocalDateTime agora = LocalDateTime.now();
        CotacaoProposta proposta = new CotacaoProposta();
        proposta.numero = numero;
        proposta.agencia = request.agencia().trim();
        proposta.nomeCliente = request.nomeCliente().trim();
        proposta.cpfCnpj = normalizar(request.cpfCnpj());
        proposta.produto = request.produto().trim();
        proposta.modalidade = normalizar(request.modalidade());
        proposta.valor = request.valor();
        proposta.status = request.status() == null ? StatusProposta.EM_ANALISE : request.status();
        proposta.prioridade = request.prioridade() == null ? PrioridadeProposta.MEDIA : request.prioridade();
        proposta.responsavel = request.responsavel().trim();
        proposta.matriculaResponsavel = normalizar(request.matriculaResponsavel());
        proposta.prazoResposta = request.prazoResposta();
        proposta.observacao = normalizar(request.observacao());
        proposta.dataCriacao = agora;
        proposta.dataAtualizacao = agora;
        proposta.ativo = Boolean.TRUE;
        cotacaoRepository.persist(proposta);

        registrarHistorico(proposta.id, null, proposta.status, proposta.responsavel, proposta.matriculaResponsavel, "Proposta criada.");
        auditoriaService.registrarPorMatricula(
                proposta.matriculaResponsavel,
                "COTACAO_PROPOSTA_CRIADA",
                proposta.numero,
                "Cotação/proposta criada para o cliente " + proposta.nomeCliente + "."
        );
        return CotacaoPropostaDTOs.CotacaoPropostaResponse.from(proposta);
    }

    @Transactional
    public CotacaoPropostaDTOs.CotacaoPropostaResponse atualizar(Long id, CotacaoPropostaDTOs.AtualizarCotacaoPropostaRequest request) {
        CotacaoProposta proposta = buscarEntidade(id);

        if (request.agencia() != null && !request.agencia().isBlank()) proposta.agencia = request.agencia().trim();
        if (request.nomeCliente() != null && !request.nomeCliente().isBlank()) proposta.nomeCliente = request.nomeCliente().trim();
        if (request.cpfCnpj() != null) proposta.cpfCnpj = normalizar(request.cpfCnpj());
        if (request.produto() != null && !request.produto().isBlank()) proposta.produto = request.produto().trim();
        if (request.modalidade() != null) proposta.modalidade = normalizar(request.modalidade());
        if (request.valor() != null) proposta.valor = request.valor();
        if (request.prioridade() != null) proposta.prioridade = request.prioridade();
        if (request.responsavel() != null && !request.responsavel().isBlank()) proposta.responsavel = request.responsavel().trim();
        if (request.matriculaResponsavel() != null) proposta.matriculaResponsavel = normalizar(request.matriculaResponsavel());
        if (request.prazoResposta() != null) proposta.prazoResposta = request.prazoResposta();
        if (request.observacao() != null) proposta.observacao = normalizar(request.observacao());
        if (request.ativo() != null) proposta.ativo = request.ativo();

        proposta.dataAtualizacao = LocalDateTime.now();
        auditoriaService.registrarPorMatricula(
                proposta.matriculaResponsavel,
                "COTACAO_PROPOSTA_ATUALIZADA",
                proposta.numero,
                "Cotação/proposta atualizada para o cliente " + proposta.nomeCliente + "."
        );
        return CotacaoPropostaDTOs.CotacaoPropostaResponse.from(proposta);
    }

    @Transactional
    public CotacaoPropostaDTOs.CotacaoPropostaResponse alterarStatus(Long id, CotacaoPropostaDTOs.AlterarStatusRequest request) {
        if (request.status() == null) {
            throw new WebApplicationException("Status é obrigatório.", Response.Status.BAD_REQUEST);
        }

        CotacaoProposta proposta = buscarEntidade(id);
        StatusProposta statusAnterior = proposta.status;
        proposta.status = request.status();
        proposta.dataAtualizacao = LocalDateTime.now();

        registrarHistorico(
                proposta.id,
                statusAnterior,
                proposta.status,
                request.usuario() == null || request.usuario().isBlank() ? proposta.responsavel : request.usuario().trim(),
                normalizar(request.matriculaUsuario()),
                request.descricao() == null || request.descricao().isBlank() ? "Status atualizado." : request.descricao().trim()
        );

        auditoriaService.registrarPorMatricula(
                normalizar(request.matriculaUsuario()) != null ? request.matriculaUsuario() : proposta.matriculaResponsavel,
                "COTACAO_PROPOSTA_STATUS_ALTERADO",
                proposta.numero,
                "Status alterado de " + statusAnterior + " para " + proposta.status + "."
        );
        return CotacaoPropostaDTOs.CotacaoPropostaResponse.from(proposta);
    }

    @Transactional
    public CotacaoPropostaDTOs.ComentarioResponse adicionarComentario(Long id, CotacaoPropostaDTOs.AdicionarComentarioRequest request) {
        CotacaoProposta proposta = buscarEntidade(id);
        if (request.autor() == null || request.autor().isBlank()) {
            throw new WebApplicationException("Autor é obrigatório.", Response.Status.BAD_REQUEST);
        }
        if (request.comentario() == null || request.comentario().isBlank()) {
            throw new WebApplicationException("Comentário é obrigatório.", Response.Status.BAD_REQUEST);
        }

        ComentarioProposta comentario = new ComentarioProposta();
        comentario.propostaId = id;
        comentario.autor = request.autor().trim();
        comentario.matriculaAutor = normalizar(request.matriculaAutor());
        comentario.comentario = request.comentario().trim();
        comentario.criadoEm = LocalDateTime.now();
        comentario.ativo = Boolean.TRUE;
        comentarioRepository.persist(comentario);

        auditoriaService.registrarPorMatricula(
                comentario.matriculaAutor,
                "COTACAO_PROPOSTA_COMENTARIO_INCLUIDO",
                proposta.numero,
                "Comentário incluído na cotação/proposta."
        );
        return CotacaoPropostaDTOs.ComentarioResponse.from(comentario);
    }

    public PaginacaoDTOs.PaginaResponse<CotacaoPropostaDTOs.HistoricoResponse> listarHistoricoPaginado(Long id, Integer pagina, Integer tamanho) {
        buscarEntidade(id);
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "criadoEm", "desc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = paginacao.tamanhoSeguro();
        List<CotacaoPropostaDTOs.HistoricoResponse> itens = historicoRepository.listarPorProposta(id, paginaSegura, tamanhoSeguro)
                .stream()
                .map(CotacaoPropostaDTOs.HistoricoResponse::from)
                .toList();
        long total = historicoRepository.contarPorProposta(id);
        return PaginacaoDTOs.PaginaResponse.of(itens, total, paginaSegura, tamanhoSeguro, "criadoEm", "desc");
    }

    public PaginacaoDTOs.PaginaResponse<CotacaoPropostaDTOs.ComentarioResponse> listarComentariosPaginado(Long id, Integer pagina, Integer tamanho) {
        buscarEntidade(id);
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "criadoEm", "desc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = paginacao.tamanhoSeguro();
        List<CotacaoPropostaDTOs.ComentarioResponse> itens = comentarioRepository.listarAtivosPorProposta(id, paginaSegura, tamanhoSeguro)
                .stream()
                .map(CotacaoPropostaDTOs.ComentarioResponse::from)
                .toList();
        long total = comentarioRepository.contarAtivosPorProposta(id);
        return PaginacaoDTOs.PaginaResponse.of(itens, total, paginaSegura, tamanhoSeguro, "criadoEm", "desc");
    }

    /** Mantido para compatibilidade interna. */
    public List<CotacaoPropostaDTOs.HistoricoResponse> listarHistorico(Long id) {
        return listarHistoricoPaginado(id, 0, 50).itens();
    }

    /** Mantido para compatibilidade interna. */
    public List<CotacaoPropostaDTOs.ComentarioResponse> listarComentarios(Long id) {
        return listarComentariosPaginado(id, 0, 50).itens();
    }

    public CotacaoPropostaDTOs.ResumoCotacoesResponse resumo() {
        List<CotacaoProposta> propostas = cotacaoRepository.listarAtivas();
        BigDecimal valorTotal = BigDecimal.ZERO;
        BigDecimal valorAprovado = BigDecimal.ZERO;

        for (CotacaoProposta proposta : propostas) {
            if (proposta.valor != null) {
                valorTotal = valorTotal.add(proposta.valor);
                if (proposta.status == StatusProposta.APROVADA) {
                    valorAprovado = valorAprovado.add(proposta.valor);
                }
            }
        }

        return new CotacaoPropostaDTOs.ResumoCotacoesResponse(
                propostas.size(),
                contar(propostas, StatusProposta.RASCUNHO),
                contar(propostas, StatusProposta.EM_ANALISE),
                contar(propostas, StatusProposta.PENDENTE_DOCUMENTACAO),
                contar(propostas, StatusProposta.APROVADA),
                contar(propostas, StatusProposta.REPROVADA),
                contar(propostas, StatusProposta.CANCELADA),
                valorTotal,
                valorAprovado
        );
    }

    private CotacaoProposta buscarEntidade(Long id) {
        CotacaoProposta proposta = cotacaoRepository.buscarAtiva(id);
        if (proposta == null) {
            throw new NotFoundException("Cotação/proposta não encontrada.");
        }
        return proposta;
    }

    private void validarCriacao(CotacaoPropostaDTOs.CriarCotacaoPropostaRequest request) {
        if (request.agencia() == null || request.agencia().isBlank()
                || request.nomeCliente() == null || request.nomeCliente().isBlank()
                || request.produto() == null || request.produto().isBlank()
                || request.valor() == null || request.valor().compareTo(BigDecimal.ZERO) < 0
                || request.responsavel() == null || request.responsavel().isBlank()) {
            throw new BadRequestException("Campos obrigatórios ausentes ou inválidos: agencia, nomeCliente, produto, valor e responsavel.");
        }
    }

    private void registrarHistorico(Long propostaId, StatusProposta anterior, StatusProposta novo, String usuario, String matricula, String descricao) {
        HistoricoProposta historico = new HistoricoProposta();
        historico.propostaId = propostaId;
        historico.statusAnterior = anterior;
        historico.statusNovo = novo;
        historico.usuario = usuario == null || usuario.isBlank() ? "Sistema" : usuario;
        historico.matriculaUsuario = normalizar(matricula);
        historico.descricao = descricao;
        historico.criadoEm = LocalDateTime.now();
        historicoRepository.persist(historico);
    }

    private String gerarNumero() {
        return "PROP-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private long contar(List<CotacaoProposta> propostas, StatusProposta status) {
        return propostas.stream().filter(p -> p.status == status).count();
    }

    private int normalizarPagina(Integer pagina) {
        return pagina == null || pagina < 0 ? 0 : pagina;
    }

    private int normalizarTamanho(Integer tamanho) {
        if (tamanho == null || tamanho <= 0) return TAMANHO_PADRAO;
        return Math.min(tamanho, TAMANHO_MAXIMO);
    }

    private String normalizarCampoOrdenacao(String ordenarPor) {
        try {
            return cotacaoRepository.validarCampoOrdenacao(ordenarPor);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private String normalizarDirecao(String direcao) {
        if (direcao == null || direcao.isBlank()) return "desc";
        String valor = direcao.trim().toLowerCase(Locale.ROOT);
        if (!valor.equals("asc") && !valor.equals("desc")) {
            throw new WebApplicationException("Direção de ordenação inválida. Use asc ou desc.", Response.Status.BAD_REQUEST);
        }
        return valor;
    }

    private String normalizarEnumTexto(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}

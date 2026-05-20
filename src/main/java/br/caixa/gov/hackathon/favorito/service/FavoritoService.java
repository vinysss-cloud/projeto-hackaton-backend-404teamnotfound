package br.caixa.gov.hackathon.favorito.service;

import br.caixa.gov.hackathon.auditoria.service.AuditoriaService;
import br.caixa.gov.hackathon.usuario.service.UsuarioService;

import br.caixa.gov.hackathon.favorito.dto.FavoritoDTOs;
import br.caixa.gov.hackathon.common.dto.PaginacaoDTOs;
import br.caixa.gov.hackathon.favorito.entity.FavoritoUsuario;
import br.caixa.gov.hackathon.servico.entity.ServicoInterno;
import br.caixa.gov.hackathon.usuario.entity.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import br.caixa.gov.hackathon.favorito.repository.FavoritoUsuarioRepository;
import br.caixa.gov.hackathon.servico.repository.ServicoInternoRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class FavoritoService {

    @Inject
    UsuarioService usuarioService;

    @Inject
    AuditoriaService auditoriaService;

    @Inject
    FavoritoUsuarioRepository favoritoRepository;

    @Inject
    ServicoInternoRepository servicoRepository;

    public PaginacaoDTOs.PaginaResponse<FavoritoDTOs.FavoritoResponse> listarPorUsuarioPaginado(Long usuarioId, Integer pagina, Integer tamanho) {
        usuarioService.buscarUsuario(usuarioId);
        PaginacaoDTOs.PaginacaoRequest paginacao = new PaginacaoDTOs.PaginacaoRequest(pagina, tamanho, "dataCriacao", "desc");
        int paginaSegura = paginacao.paginaSegura();
        int tamanhoSeguro = paginacao.tamanhoSeguro();
        List<FavoritoDTOs.FavoritoResponse> itens = favoritoRepository.listarAtivosPorUsuario(usuarioId, paginaSegura, tamanhoSeguro)
                .stream().map(FavoritoDTOs.FavoritoResponse::from).toList();
        long total = favoritoRepository.contarAtivosPorUsuario(usuarioId);
        return PaginacaoDTOs.PaginaResponse.of(itens, total, paginaSegura, tamanhoSeguro, "dataCriacao", "desc");
    }

    public List<FavoritoDTOs.FavoritoResponse> listarPorUsuario(Long usuarioId) {
        return listarPorUsuarioPaginado(usuarioId, 0, 50).itens();
    }

    @Transactional
    public FavoritoDTOs.FavoritoResponse salvar(FavoritoDTOs.FavoritoRequest request) {
        validarRequest(request);
        Usuario usuario = usuarioService.buscarUsuario(request.usuarioId());
        ServicoInterno servico = servicoRepository.buscarAtivo(request.servicoId());
        if (servico == null || !Boolean.TRUE.equals(servico.ativo)) {
            throw new NotFoundException("Serviço não encontrado para favoritar.");
        }

        FavoritoUsuario favorito = favoritoRepository.buscar(usuario.id, servico.id);
        if (favorito == null) {
            favorito = new FavoritoUsuario();
            favorito.usuario = usuario;
            favorito.servico = servico;
            favorito.dataCriacao = LocalDateTime.now();
            favoritoRepository.persist(favorito);
        }

        favorito.tituloCustomizado = normalizar(request.tituloCustomizado());
        favorito.observacao = normalizar(request.observacao());
        favorito.ativo = Boolean.TRUE;

        auditoriaService.registrar(usuario, "FAVORITO_SALVO", servico.codigo, "Usuário adicionou serviço aos favoritos: " + servico.titulo);
        return FavoritoDTOs.FavoritoResponse.from(favorito);
    }

    @Transactional
    public FavoritoDTOs.FavoritoResponse atualizar(Long id, FavoritoDTOs.FavoritoRequest request) {
        FavoritoUsuario favorito = buscarFavorito(id);
        favorito.tituloCustomizado = normalizar(request.tituloCustomizado());
        favorito.observacao = normalizar(request.observacao());
        auditoriaService.registrar(favorito.usuario, "FAVORITO_ATUALIZADO", favorito.servico.codigo, "Usuário atualizou um favorito.");
        return FavoritoDTOs.FavoritoResponse.from(favorito);
    }

    @Transactional
    public void remover(Long id) {
        FavoritoUsuario favorito = buscarFavorito(id);
        favorito.ativo = Boolean.FALSE;
        auditoriaService.registrar(favorito.usuario, "FAVORITO_REMOVIDO", favorito.servico.codigo, "Usuário removeu serviço dos favoritos.");
    }

    private FavoritoUsuario buscarFavorito(Long id) {
        FavoritoUsuario favorito = favoritoRepository.buscarAtivo(id);
        if (favorito == null) {
            throw new NotFoundException("Favorito não encontrado.");
        }
        return favorito;
    }

    private void validarRequest(FavoritoDTOs.FavoritoRequest request) {
        if (request == null || request.usuarioId() == null || request.servicoId() == null) {
            throw new BadRequestException("Informe usuarioId e servicoId para salvar o favorito.");
        }
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}

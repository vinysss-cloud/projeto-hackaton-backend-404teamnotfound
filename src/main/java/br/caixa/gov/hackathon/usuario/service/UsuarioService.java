package br.caixa.gov.hackathon.usuario.service;

import br.caixa.gov.hackathon.usuario.dto.UsuarioDTOs;
import br.caixa.gov.hackathon.usuario.entity.PreferenciaUsuario;
import br.caixa.gov.hackathon.usuario.entity.Usuario;
import br.caixa.gov.hackathon.usuario.repository.PreferenciaUsuarioRepository;
import br.caixa.gov.hackathon.usuario.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PreferenciaUsuarioRepository preferenciaRepository;

    public Usuario buscarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) {
            throw new NotFoundException("Usuário não encontrado.");
        }
        return usuario;
    }

    public Usuario buscarPorMatricula(String matricula) {
        Usuario usuario = usuarioRepository.buscarPorMatricula(matricula);
        if (usuario == null) {
            throw new NotFoundException("Usuário não encontrado para a matrícula informada.");
        }
        return usuario;
    }

    @Transactional
    public PreferenciaUsuario buscarOuCriarPreferencias(Usuario usuario) {
        PreferenciaUsuario preferencia = preferenciaRepository.buscarPorUsuario(usuario.id);
        if (preferencia == null) {
            preferencia = new PreferenciaUsuario();
            preferencia.usuario = usuario;
            preferencia.tema = "PADRAO";
            preferencia.menuCompacto = Boolean.FALSE;
            preferencia.ultimaRota = "/home";
            preferencia.atualizadoEm = LocalDateTime.now();
            preferenciaRepository.persist(preferencia);
        }
        return preferencia;
    }

    @Transactional
    public Usuario atualizarUsuario(Long id, UsuarioDTOs.AtualizarUsuarioRequest request) {
        Usuario usuario = buscarUsuario(id);
        if (request != null && request.nomeExibicao() != null && !request.nomeExibicao().isBlank()) {
            usuario.nomeExibicao = request.nomeExibicao().trim();
        }
        return usuario;
    }

    @Transactional
    public PreferenciaUsuario atualizarPreferencias(Long id, UsuarioDTOs.PreferenciaRequest request) {
        Usuario usuario = buscarUsuario(id);
        PreferenciaUsuario preferencia = buscarOuCriarPreferencias(usuario);

        if (request != null) {
            if (request.tema() != null && !request.tema().isBlank()) {
                preferencia.tema = request.tema().trim().toUpperCase();
            }
            if (request.menuCompacto() != null) {
                preferencia.menuCompacto = request.menuCompacto();
            }
            if (request.ultimaRota() != null && !request.ultimaRota().isBlank()) {
                preferencia.ultimaRota = request.ultimaRota().trim();
            }
        }
        preferencia.atualizadoEm = LocalDateTime.now();
        return preferencia;
    }
}

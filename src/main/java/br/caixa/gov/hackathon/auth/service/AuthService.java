package br.caixa.gov.hackathon.auth.service;

import br.caixa.gov.hackathon.auditoria.service.AuditoriaService;

import br.caixa.gov.hackathon.auth.dto.AuthDTOs;
import br.caixa.gov.hackathon.usuario.entity.PreferenciaUsuario;
import br.caixa.gov.hackathon.usuario.entity.Usuario;
import br.caixa.gov.hackathon.usuario.repository.PreferenciaUsuarioRepository;
import br.caixa.gov.hackathon.usuario.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;

import java.time.LocalDateTime;

@ApplicationScoped
public class AuthService {

    @Inject
    PasswordService passwordService;

    @Inject
    AuditoriaService auditoriaService;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PreferenciaUsuarioRepository preferenciaRepository;

    @Transactional
    public AuthDTOs.AuthResponse login(AuthDTOs.LoginRequest request) {
        validarLogin(request);

        String matricula = request.matricula().trim().toUpperCase();
        Usuario usuario = usuarioRepository.buscarPorMatricula(matricula);

        if (usuario == null) {
            usuario = criarUsuario(matricula, request.senha(), request.nomeExibicao());
            auditoriaService.registrar(usuario, "LOGIN_PRIMEIRO_ACESSO", "AUTH", "Usuário criado automaticamente no primeiro acesso.");
            return new AuthDTOs.AuthResponse(true, true, "Primeiro acesso identificado. Usuário criado e login realizado.", AuthDTOs.UsuarioResponse.from(usuario));
        }

        if (!Boolean.TRUE.equals(usuario.ativo)) {
            auditoriaService.registrar(usuario, "LOGIN_FALHA", "AUTH", "Tentativa de acesso com usuário inativo.");
            throw new NotAuthorizedException("Usuário inativo.");
        }

        if (!passwordService.senhaConfere(request.senha(), usuario.senhaSalt, usuario.senhaHash)) {
            auditoriaService.registrar(usuario, "LOGIN_FALHA", "AUTH", "Senha inválida para a matrícula informada.");
            throw new NotAuthorizedException("Senha inválida para a matrícula informada.");
        }

        if (passwordService.deveAtualizarParaBCrypt(usuario.senhaHash)) {
            usuario.senhaSalt = passwordService.gerarSalt();
            usuario.senhaHash = passwordService.gerarHash(request.senha(), usuario.senhaSalt);
        }

        usuario.primeiroAcesso = Boolean.FALSE;
        usuario.ultimoAcesso = LocalDateTime.now();
        if (request.nomeExibicao() != null && !request.nomeExibicao().isBlank()) {
            usuario.nomeExibicao = request.nomeExibicao().trim();
        }
        auditoriaService.registrar(usuario, "LOGIN_SUCESSO", "AUTH", "Login realizado com sucesso.");

        return new AuthDTOs.AuthResponse(true, false, "Login realizado com sucesso.", AuthDTOs.UsuarioResponse.from(usuario));
    }

    private Usuario criarUsuario(String matricula, String senha, String nomeExibicao) {
        Usuario usuario = new Usuario();
        usuario.matricula = matricula;
        usuario.nomeExibicao = nomeExibicao == null || nomeExibicao.isBlank() ? matricula : nomeExibicao.trim();
        usuario.senhaSalt = passwordService.gerarSalt();
        usuario.senhaHash = passwordService.gerarHash(senha, usuario.senhaSalt);
        usuario.ativo = Boolean.TRUE;
        usuario.primeiroAcesso = Boolean.TRUE;
        usuario.dataCriacao = LocalDateTime.now();
        usuario.ultimoAcesso = LocalDateTime.now();
        usuarioRepository.persist(usuario);

        PreferenciaUsuario preferencias = new PreferenciaUsuario();
        preferencias.usuario = usuario;
        preferencias.tema = "PADRAO";
        preferencias.menuCompacto = Boolean.FALSE;
        preferencias.ultimaRota = "/home";
        preferencias.atualizadoEm = LocalDateTime.now();
        preferenciaRepository.persist(preferencias);
        return usuario;
    }

    private void validarLogin(AuthDTOs.LoginRequest request) {
        if (request == null) {
            throw new BadRequestException("Dados de login são obrigatórios.");
        }
        if (request.matricula() == null || request.matricula().isBlank()) {
            throw new BadRequestException("Matrícula é obrigatória.");
        }
        if (request.senha() == null || request.senha().isBlank()) {
            throw new BadRequestException("Senha é obrigatória.");
        }
    }
}

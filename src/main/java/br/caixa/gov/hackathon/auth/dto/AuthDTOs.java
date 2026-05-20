package br.caixa.gov.hackathon.auth.dto;

import br.caixa.gov.hackathon.usuario.entity.Usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class AuthDTOs {

    public record LoginRequest(
            @NotBlank(message = "Matrícula é obrigatória.")
            @Size(min = 3, max = 30, message = "Matrícula deve ter entre 3 e 30 caracteres.")
            @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Matrícula deve conter apenas letras, números, ponto, hífen ou underline.")
            String matricula,

            @NotBlank(message = "Senha é obrigatória.")
            @Size(min = 4, max = 80, message = "Senha deve ter entre 4 e 80 caracteres.")
            String senha,

            @Size(max = 120, message = "Nome de exibição deve ter no máximo 120 caracteres.")
            String nomeExibicao
    ) {}

    public record UsuarioResponse(
            Long id,
            String matricula,
            String nomeExibicao,
            Boolean primeiroAcesso,
            Boolean ativo,
            LocalDateTime dataCriacao,
            LocalDateTime ultimoAcesso
    ) {
        public static UsuarioResponse from(Usuario usuario) {
            if (usuario == null) {
                return null;
            }
            return new UsuarioResponse(
                    usuario.id,
                    usuario.matricula,
                    usuario.nomeExibicao,
                    usuario.primeiroAcesso,
                    usuario.ativo,
                    usuario.dataCriacao,
                    usuario.ultimoAcesso
            );
        }
    }

    public record AuthResponse(
            Boolean autenticado,
            Boolean usuarioCriado,
            String mensagem,
            UsuarioResponse usuario
    ) {}
}

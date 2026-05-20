package br.caixa.gov.hackathon.usuario.dto;

import br.caixa.gov.hackathon.auth.dto.AuthDTOs;

import br.caixa.gov.hackathon.usuario.entity.PreferenciaUsuario;
import br.caixa.gov.hackathon.usuario.entity.Usuario;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioDTOs {

    public record AtualizarUsuarioRequest(
            @Size(min = 2, max = 120, message = "Nome de exibição deve ter entre 2 e 120 caracteres.")
            String nomeExibicao
    ) {}

    public record PreferenciaRequest(
            @Pattern(regexp = "^(CLARO|ESCURO|SISTEMA)$", message = "Tema deve ser CLARO, ESCURO ou SISTEMA.")
            String tema,

            Boolean menuCompacto,

            @Size(max = 200, message = "Última rota deve ter no máximo 200 caracteres.")
            @Pattern(regexp = "^$|^/[A-Za-z0-9/_?=&.-]*$", message = "Última rota deve iniciar com / e conter apenas caracteres seguros.")
            String ultimaRota
    ) {}

    public record PreferenciaResponse(
            Long usuarioId,
            String tema,
            Boolean menuCompacto,
            String ultimaRota
    ) {
        public static PreferenciaResponse from(PreferenciaUsuario preferencia) {
            return new PreferenciaResponse(
                    preferencia.usuario.id,
                    preferencia.tema,
                    preferencia.menuCompacto,
                    preferencia.ultimaRota
            );
        }
    }

    public record UsuarioDetalheResponse(
            AuthDTOs.UsuarioResponse usuario,
            PreferenciaResponse preferencias
    ) {
        public static UsuarioDetalheResponse of(Usuario usuario, PreferenciaUsuario preferencia) {
            return new UsuarioDetalheResponse(AuthDTOs.UsuarioResponse.from(usuario), PreferenciaResponse.from(preferencia));
        }
    }
}

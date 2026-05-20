package br.caixa.gov.hackathon.usuario.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.auth.dto.AuthDTOs;
import br.caixa.gov.hackathon.usuario.dto.UsuarioDTOs;
import br.caixa.gov.hackathon.usuario.entity.PreferenciaUsuario;
import br.caixa.gov.hackathon.usuario.entity.Usuario;
import br.caixa.gov.hackathon.usuario.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/usuarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    @GET
    @Path("/{id}")
    public ApiResponse<UsuarioDTOs.UsuarioDetalheResponse> buscar(
            @PathParam("id") @Positive(message = "ID do usuário deve ser maior que zero.") Long id
    ) {
        Usuario usuario = usuarioService.buscarUsuario(id);
        PreferenciaUsuario preferencia = usuarioService.buscarOuCriarPreferencias(usuario);
        return ApiResponse.ok("Usuário localizado com sucesso.", UsuarioDTOs.UsuarioDetalheResponse.of(usuario, preferencia));
    }

    @GET
    @Path("/matricula/{matricula}")
    public ApiResponse<AuthDTOs.UsuarioResponse> buscarPorMatricula(
            @PathParam("matricula") @NotBlank(message = "Matrícula é obrigatória.") String matricula
    ) {
        Usuario usuario = usuarioService.buscarPorMatricula(matricula);
        return ApiResponse.ok("Usuário localizado com sucesso.", AuthDTOs.UsuarioResponse.from(usuario));
    }

    @PUT
    @Path("/{id}")
    public ApiResponse<AuthDTOs.UsuarioResponse> atualizar(
            @PathParam("id") @Positive(message = "ID do usuário deve ser maior que zero.") Long id,
            @Valid UsuarioDTOs.AtualizarUsuarioRequest request
    ) {
        return ApiResponse.ok("Usuário atualizado com sucesso.", AuthDTOs.UsuarioResponse.from(usuarioService.atualizarUsuario(id, request)));
    }

    @GET
    @Path("/{id}/preferencias")
    public ApiResponse<UsuarioDTOs.PreferenciaResponse> preferencias(
            @PathParam("id") @Positive(message = "ID do usuário deve ser maior que zero.") Long id
    ) {
        Usuario usuario = usuarioService.buscarUsuario(id);
        return ApiResponse.ok("Preferências carregadas com sucesso.", UsuarioDTOs.PreferenciaResponse.from(usuarioService.buscarOuCriarPreferencias(usuario)));
    }

    @PUT
    @Path("/{id}/preferencias")
    public ApiResponse<UsuarioDTOs.PreferenciaResponse> atualizarPreferencias(
            @PathParam("id") @Positive(message = "ID do usuário deve ser maior que zero.") Long id,
            @Valid UsuarioDTOs.PreferenciaRequest request
    ) {
        return ApiResponse.ok("Preferências atualizadas com sucesso.", UsuarioDTOs.PreferenciaResponse.from(usuarioService.atualizarPreferencias(id, request)));
    }
}

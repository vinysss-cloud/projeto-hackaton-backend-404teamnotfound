package br.caixa.gov.hackathon.auth.resource;

import br.caixa.gov.hackathon.common.dto.ApiResponse;
import br.caixa.gov.hackathon.auth.dto.AuthDTOs;
import br.caixa.gov.hackathon.auth.service.AuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public ApiResponse<AuthDTOs.AuthResponse> login(@Valid AuthDTOs.LoginRequest request) {
        AuthDTOs.AuthResponse response = authService.login(request);
        return ApiResponse.ok(response.mensagem(), response);
    }

}

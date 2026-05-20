package br.caixa.gov.hackathon;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class AuthResourceTest {

    @Test
    void deveCriarUsuarioNoPrimeiroLoginComEnvelopePadronizado() {
        String matricula = matriculaUnica("T");
        String body = """
                {
                  "matricula": "%s",
                  "senha": "1234",
                  "nomeExibicao": "Teste Integração"
                }
                """.formatted(matricula);

        given()
                .contentType("application/json")
                .body(body)
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.autenticado", is(true))
                .body("dados.usuarioCriado", is(true))
                .body("dados.usuario.id", notNullValue())
                .body("dados.usuario.matricula", is(matricula))
                .body("dados.usuario.nomeExibicao", is("Teste Integração"));
    }

    @Test
    void deveLogarUsuarioExistenteValidandoSenha() {
        String matricula = matriculaUnica("L");
        String primeiroLogin = """
                {"matricula":"%s","senha":"1234","nomeExibicao":"Usuário Existente"}
                """.formatted(matricula);

        Integer usuarioId = given()
                .contentType("application/json")
                .body(primeiroLogin)
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .extract().path("dados.usuario.id");

        String segundoLogin = """
                {"matricula":"%s","senha":"1234"}
                """.formatted(matricula);

        given()
                .contentType("application/json")
                .body(segundoLogin)
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.autenticado", is(true))
                .body("dados.usuarioCriado", is(false))
                .body("dados.usuario.id", is(usuarioId))
                .body("dados.usuario.matricula", is(matricula));
    }

    @Test
    void deveBloquearSegundoLoginComSenhaInvalida() {
        String matricula = matriculaUnica("E");
        String primeiroLogin = """
                {"matricula":"%s","senha":"1234","nomeExibicao":"Usuário Senha Errada"}
                """.formatted(matricula);

        given()
                .contentType("application/json")
                .body(primeiroLogin)
                .when().post("/auth/login")
                .then()
                .statusCode(200);

        String senhaInvalida = """
                {"matricula":"%s","senha":"9999"}
                """.formatted(matricula);

        given()
                .contentType("application/json")
                .body(senhaInvalida)
                .when().post("/auth/login")
                .then()
                .statusCode(401)
                .body("sucesso", is(false))
                .body("codigo", is("NAO_AUTORIZADO"));
    }

    @Test
    void deveRetornarErroDeValidacaoQuandoLoginVazio() {
        String body = """
                {"matricula":"","senha":"12"}
                """;

        given()
                .contentType("application/json")
                .body(body)
                .when().post("/auth/login")
                .then()
                .statusCode(400)
                .body("sucesso", is(false))
                .body("codigo", is("REQUISICAO_INVALIDA"))
                .body("mensagem", is("Existem campos inválidos na requisição."));
    }

    private String matriculaUnica(String prefixo) {
        return prefixo + Long.toString(System.nanoTime()).substring(5, 13);
    }
}

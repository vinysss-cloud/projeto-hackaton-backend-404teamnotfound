package br.caixa.gov.hackathon;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class AuditoriaResourceTest {

    @Test
    void deveConsultarAuditoriaGeradaAutomaticamenteNoLoginEPortal() {
        Integer usuarioId = criarUsuario("A");

        given()
                .when().get("/portal/inicial/{usuarioId}", usuarioId)
                .then()
                .statusCode(200);

        given()
                .queryParam("limite", 10)
                .when().get("/auditoria/usuario/{usuarioId}", usuarioId)
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.size()", greaterThanOrEqualTo(2));

        given()
                .when().get("/auditoria/usuario/{usuarioId}/resumo", usuarioId)
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.usuarioId", is(usuarioId))
                .body("dados.totalAcoes", greaterThanOrEqualTo(2));
    }

    private Integer criarUsuario(String prefixo) {
        String matricula = prefixo + Long.toString(System.nanoTime()).substring(5, 13);
        String body = """
                {"matricula":"%s","senha":"1234","nomeExibicao":"Usuário Auditoria"}
                """.formatted(matricula);

        return given()
                .contentType("application/json")
                .body(body)
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .extract().path("dados.usuario.id");
    }
}

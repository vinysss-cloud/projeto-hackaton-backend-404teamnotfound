package br.caixa.gov.hackathon;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class PortalResourceTest {

    @Test
    void deveCarregarPortalInicialComDadosConsolidados() {
        Integer usuarioId = criarUsuario("P");

        given()
                .when().get("/portal/inicial/{usuarioId}", usuarioId)
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.header.produto", is("CAIXA HUB"))
                .body("dados.usuario.id", is(usuarioId))
                .body("dados.menu.size()", greaterThanOrEqualTo(4))
                .body("dados.servicos.size()", greaterThanOrEqualTo(1))
                .body("dados.normativos.size()", greaterThanOrEqualTo(1))
                .body("dados.resumoCotacoesPropostas", notNullValue())
                .body("dados.cotacoesPropostasRecentes", notNullValue());
    }

    @Test
    void deveCarregarMenuPadraoDoPortal() {
        given()
                .when().get("/portal/menu")
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados[0].label", is("Início"));
    }

    private Integer criarUsuario(String prefixo) {
        String matricula = prefixo + Long.toString(System.nanoTime()).substring(5, 13);
        String body = """
                {"matricula":"%s","senha":"1234","nomeExibicao":"Usuário Portal"}
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

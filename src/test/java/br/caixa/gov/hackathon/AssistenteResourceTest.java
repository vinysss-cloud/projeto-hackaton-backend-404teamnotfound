package br.caixa.gov.hackathon;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class AssistenteResourceTest {

    @Test
    void deveBuscarItensRelacionadosAoTermoInformado() {
        given()
                .queryParam("q", "abertura")
                .when().get("/assistente/buscar")
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.termo", is("abertura"))
                .body("dados.quantidade", greaterThanOrEqualTo(1))
                .body("dados.resultados.size()", greaterThanOrEqualTo(1));
    }
}

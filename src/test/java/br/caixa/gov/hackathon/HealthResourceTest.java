package br.caixa.gov.hackathon;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class HealthResourceTest {

    @Test
    void deveRetornarStatusUpNoEnvelopePadronizado() {
        given()
                .when().get("/health")
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("mensagem", is("Aplicação disponível."))
                .body("dados.status", is("UP"))
                .body("dados.app", is("caixa-hub-backend"))
                .body("timestamp", notNullValue());
    }
}

package br.caixa.gov.hackathon;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class CotacaoPropostaResourceTest {

    @Test
    void deveListarCotacoesComPaginacaoEnvelopePadronizado() {
        given()
                .when().get("/cotacoes-propostas?pagina=0&tamanho=5&ordenarPor=dataAtualizacao&direcao=desc")
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.pagina", is(0))
                .body("dados.tamanho", is(5))
                .body("dados.totalElementos", greaterThanOrEqualTo(1))
                .body("dados.itens.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void deveCriarDetalharAlterarStatusComentarEConsultarHistorico() {
        String numero = "PROP-TEST-" + Long.toString(System.nanoTime()).substring(5, 13);
        String bodyCriacao = """
                {
                  "numero": "%s",
                  "agencia": "1234",
                  "nomeCliente": "Cliente Teste API",
                  "cpfCnpj": "12345678900",
                  "produto": "Abertura de Conta",
                  "modalidade": "Conta Pessoa Física",
                  "valor": 100.00,
                  "status": "EM_ANALISE",
                  "prioridade": "ALTA",
                  "responsavel": "Atendente Teste",
                  "matriculaResponsavel": "C123456",
                  "observacao": "Registro criado por teste automatizado."
                }
                """.formatted(numero);

        Integer propostaId = given()
                .contentType("application/json")
                .body(bodyCriacao)
                .when().post("/cotacoes-propostas")
                .then()
                .statusCode(201)
                .body("sucesso", is(true))
                .body("dados.id", notNullValue())
                .body("dados.numero", is(numero))
                .body("dados.status", is("EM_ANALISE"))
                .extract().path("dados.id");

        given()
                .when().get("/cotacoes-propostas/{id}", propostaId)
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.proposta.id", is(propostaId))
                .body("dados.historico.size()", greaterThanOrEqualTo(1));

        String bodyStatus = """
                {
                  "status": "APROVADA",
                  "usuario": "Gerente Teste",
                  "matriculaUsuario": "C654321",
                  "descricao": "Aprovação realizada por teste automatizado."
                }
                """;

        given()
                .contentType("application/json")
                .body(bodyStatus)
                .when().patch("/cotacoes-propostas/{id}/status", propostaId)
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.status", is("APROVADA"));

        String bodyComentario = """
                {
                  "autor": "Gerente Teste",
                  "matriculaAutor": "C654321",
                  "comentario": "Comentário incluído pelo teste automatizado."
                }
                """;

        given()
                .contentType("application/json")
                .body(bodyComentario)
                .when().post("/cotacoes-propostas/{id}/comentarios", propostaId)
                .then()
                .statusCode(201)
                .body("sucesso", is(true))
                .body("dados.comentario", is("Comentário incluído pelo teste automatizado."));

        given()
                .when().get("/cotacoes-propostas/{id}/historico", propostaId)
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.size()", greaterThanOrEqualTo(2));

        given()
                .when().get("/cotacoes-propostas/{id}/comentarios", propostaId)
                .then()
                .statusCode(200)
                .body("sucesso", is(true))
                .body("dados.size()", greaterThanOrEqualTo(1));
    }

    @Test
    void deveRetornarErroQuandoCriacaoDaPropostaForInvalida() {
        String bodyInvalido = """
                {
                  "agencia": "ABC",
                  "nomeCliente": "",
                  "produto": "",
                  "valor": -1,
                  "responsavel": ""
                }
                """;

        given()
                .contentType("application/json")
                .body(bodyInvalido)
                .when().post("/cotacoes-propostas")
                .then()
                .statusCode(400)
                .body("sucesso", is(false))
                .body("codigo", is("REQUISICAO_INVALIDA"))
                .body("erros.size()", greaterThanOrEqualTo(1));
    }
}

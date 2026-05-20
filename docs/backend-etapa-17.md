# Etapa 17 — Testes automatizados dos fluxos principais

Esta etapa mantém o repositório como **somente backend** e adiciona testes de API com `@QuarkusTest` + RestAssured.

## Objetivo

Validar os fluxos principais que o frontend Angular externo vai consumir:

- health check padronizado;
- login com auto-cadastro por matrícula;
- login de segundo acesso com validação de senha;
- erro de senha inválida;
- erro de validação de entrada;
- portal inicial consolidado;
- menu do portal;
- listagem paginada de cotações/propostas;
- criação de cotação/proposta;
- detalhamento de cotação/proposta;
- alteração de status;
- inclusão de comentário;
- histórico;
- busca orientada do assistente;
- auditoria automática gerada por login e acesso ao portal.

## Arquivos criados/alterados

```txt
src/test/java/br/caixa/gov/hackathon/HealthResourceTest.java
src/test/java/br/caixa/gov/hackathon/AuthResourceTest.java
src/test/java/br/caixa/gov/hackathon/PortalResourceTest.java
src/test/java/br/caixa/gov/hackathon/CotacaoPropostaResourceTest.java
src/test/java/br/caixa/gov/hackathon/AssistenteResourceTest.java
src/test/java/br/caixa/gov/hackathon/AuditoriaResourceTest.java
```

## Como executar

```bash
./mvnw test
```

Ou, se estiver usando Maven instalado localmente:

```bash
mvn test
```

## Observação importante

O ambiente usado para montar esta etapa não conseguiu acessar o Maven Central (`repo.maven.apache.org`), então não foi possível executar a suíte aqui. Os testes foram escritos alinhados ao código atual do projeto e ao envelope padronizado `ApiResponse`/`ApiErroResponse`.

## Ponto de atenção

Os testes antigos esperavam retorno direto, por exemplo:

```json
{
  "status": "UP"
}
```

Agora o backend retorna envelope padronizado:

```json
{
  "sucesso": true,
  "mensagem": "Aplicação disponível.",
  "dados": {
    "status": "UP"
  },
  "timestamp": "..."
}
```

Por isso os testes de `HealthResourceTest` e `AuthResourceTest` foram atualizados.

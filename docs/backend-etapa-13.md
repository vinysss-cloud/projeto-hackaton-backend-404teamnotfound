# Etapa 13 — Padronização de respostas e erros da API

Esta etapa mantém o projeto como **somente backend** e cria um padrão único para facilitar o consumo pelo frontend externo em Angular ou qualquer outro cliente.

## Objetivo

Padronizar respostas de sucesso e erro para que o frontend não precise tratar formatos diferentes em cada endpoint.

## Arquivos criados

```txt
src/main/java/br/caixa/gov/hackathon/dto/ApiResponse.java
src/main/java/br/caixa/gov/hackathon/dto/ApiErroResponse.java
src/main/java/br/caixa/gov/hackathon/dto/ErroCampoResponse.java
src/main/java/br/caixa/gov/hackathon/exception/CodigoErro.java
src/main/java/br/caixa/gov/hackathon/exception/ApiNegocioException.java
```

## Arquivos alterados

```txt
src/main/java/br/caixa/gov/hackathon/exception/ApiExceptionMapper.java
src/main/java/br/caixa/gov/hackathon/resource/AuthResource.java
src/main/java/br/caixa/gov/hackathon/resource/HealthResource.java
src/main/java/br/caixa/gov/hackathon/resource/CotacaoPropostaResource.java
```

## Resposta de sucesso padronizada

Formato:

```json
{
  "sucesso": true,
  "mensagem": "Operação realizada com sucesso.",
  "dados": {},
  "timestamp": "2026-05-20T10:30:00"
}
```

Exemplo no login:

```http
POST /api/auth/login
```

```json
{
  "sucesso": true,
  "mensagem": "Primeiro acesso identificado. Usuário criado e login realizado.",
  "dados": {
    "autenticado": true,
    "usuarioCriado": true,
    "mensagem": "Primeiro acesso identificado. Usuário criado e login realizado.",
    "usuario": {
      "id": 1,
      "matricula": "C123456",
      "nomeExibicao": "Usuário Teste"
    }
  },
  "timestamp": "2026-05-20T10:30:00"
}
```

## Resposta de erro padronizada

Formato:

```json
{
  "sucesso": false,
  "status": 404,
  "codigo": "NAO_ENCONTRADO",
  "mensagem": "Registro não encontrado.",
  "erros": [],
  "timestamp": "2026-05-20T10:30:00"
}
```

Códigos previstos:

```txt
REQUISICAO_INVALIDA
NAO_AUTORIZADO
NAO_ENCONTRADO
CONFLITO
REGRA_NEGOCIO
ERRO_HTTP
ERRO_INTERNO
```

## Exemplo de uso em regra de negócio

Quando uma regra funcional precisar devolver erro controlado:

```java
throw new ApiNegocioException(
        Response.Status.CONFLICT,
        CodigoErro.CONFLITO,
        "Já existe uma proposta com este número."
);
```

## Importante para o frontend

A partir dos endpoints padronizados, o frontend deve ler os dados reais sempre em:

```txt
dados
```

Exemplo:

```ts
response.dados.usuario.id
```

Para erro, deve tratar:

```txt
codigo
mensagem
status
```

Exemplo:

```ts
error.error.codigo
error.error.mensagem
```

## Endpoints já padronizados nesta etapa

```txt
GET  /api/health
POST /api/auth/login
GET  /api/cotacoes-propostas
GET  /api/cotacoes-propostas/opcoes
GET  /api/cotacoes-propostas/filtros-aplicados
GET  /api/cotacoes-propostas/resumo
GET  /api/cotacoes-propostas/recentes
GET  /api/cotacoes-propostas/{id}
POST /api/cotacoes-propostas
PUT  /api/cotacoes-propostas/{id}
PATCH /api/cotacoes-propostas/{id}/status
POST /api/cotacoes-propostas/{id}/comentarios
GET  /api/cotacoes-propostas/{id}/comentarios
GET  /api/cotacoes-propostas/{id}/historico
```

## Próxima etapa recomendada

Etapa 14 — Padronizar os demais resources do portal:

```txt
PortalResource
UsuarioResource
ServicoResource
ConteudoResource
FavoritoResource
SistemaUsuarioResource
AnotacaoResource
NormativoResource
ProcessoInternoResource
AcessoRecenteResource
AssistenteResource
AuditoriaResource
```

Assim todo o backend terá o mesmo contrato de resposta.

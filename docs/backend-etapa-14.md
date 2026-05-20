# Etapa 14 — Padronização dos demais resources do backend

Esta etapa mantém o projeto como **somente backend** e aplica o envelope `ApiResponse<T>` nos resources que ainda retornavam listas, objetos ou `204 No Content` diretamente.

## Objetivo

Deixar todos os endpoints principais do portal com o mesmo formato de resposta para facilitar a integração com o sistema frontend externo.

Formato de sucesso:

```json
{
  "sucesso": true,
  "mensagem": "Operação realizada com sucesso.",
  "dados": {},
  "timestamp": "2026-05-20T10:30:00"
}
```

Formato de erro permanece centralizado pelo `ApiExceptionMapper` criado na etapa anterior.

## Arquivos alterados

```txt
src/main/java/br/caixa/gov/hackathon/resource/PortalResource.java
src/main/java/br/caixa/gov/hackathon/resource/UsuarioResource.java
src/main/java/br/caixa/gov/hackathon/resource/ServicoResource.java
src/main/java/br/caixa/gov/hackathon/resource/ConteudoResource.java
src/main/java/br/caixa/gov/hackathon/resource/FavoritoResource.java
src/main/java/br/caixa/gov/hackathon/resource/SistemaResource.java
src/main/java/br/caixa/gov/hackathon/resource/AnotacaoResource.java
src/main/java/br/caixa/gov/hackathon/resource/NormativoResource.java
src/main/java/br/caixa/gov/hackathon/resource/ProcessoResource.java
src/main/java/br/caixa/gov/hackathon/resource/AcessoRecenteResource.java
src/main/java/br/caixa/gov/hackathon/resource/AssistenteResource.java
src/main/java/br/caixa/gov/hackathon/resource/AuditoriaResource.java
```

## Endpoints padronizados nesta etapa

```txt
GET  /api/portal/inicial/{usuarioId}
GET  /api/portal/header
GET  /api/portal/menu

GET  /api/usuarios/{id}
GET  /api/usuarios/matricula/{matricula}
PUT  /api/usuarios/{id}
GET  /api/usuarios/{id}/preferencias
PUT  /api/usuarios/{id}/preferencias

GET  /api/servicos
GET  /api/servicos/{id}
POST /api/servicos/{id}/acessar

GET  /api/conteudos
GET  /api/conteudos/{id}

GET    /api/favoritos/usuario/{usuarioId}
POST   /api/favoritos
PUT    /api/favoritos/{id}
DELETE /api/favoritos/{id}

GET    /api/sistemas/usuario/{usuarioId}
POST   /api/sistemas
PUT    /api/sistemas/{id}
POST   /api/sistemas/{id}/acessar
DELETE /api/sistemas/{id}

GET    /api/anotacoes/usuario/{usuarioId}
POST   /api/anotacoes
PUT    /api/anotacoes/{id}
DELETE /api/anotacoes/{id}

GET /api/normativos
GET /api/normativos/{id}

GET  /api/processos
GET  /api/processos/{id}
POST /api/processos/checklist/{checklistId}/marcar

GET /api/acessos-recentes/usuario/{usuarioId}
GET /api/assistente/buscar

GET /api/auditoria/usuario/{usuarioId}
GET /api/auditoria/recentes
```

## Impacto para o frontend externo

Antes, alguns endpoints retornavam diretamente uma lista:

```json
[
  { "id": 1, "titulo": "Abertura de Conta" }
]
```

Agora retornam:

```json
{
  "sucesso": true,
  "mensagem": "Serviços carregados com sucesso.",
  "dados": [
    { "id": 1, "titulo": "Abertura de Conta" }
  ],
  "timestamp": "2026-05-20T10:30:00"
}
```

Assim, o frontend deve sempre buscar o conteúdo útil em:

```txt
dados
```

## Observação sobre DELETE

Os endpoints `DELETE` agora retornam `ApiResponse<Void>` com mensagem de sucesso, em vez de `204 No Content`, porque isso mantém o contrato único para o frontend.

Exemplo:

```json
{
  "sucesso": true,
  "mensagem": "Favorito removido com sucesso.",
  "dados": null,
  "timestamp": "2026-05-20T10:30:00"
}
```

## Próxima etapa recomendada

Etapa 15 — validações de entrada e mensagens funcionais:

```txt
validar matrícula e senha no login
validar campos obrigatórios de favoritos, sistemas e anotações
validar filtros de cotações/propostas
validar datas e status
retornar erros de campo usando ErroCampoResponse
```

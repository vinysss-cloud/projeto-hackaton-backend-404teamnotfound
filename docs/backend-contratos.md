# Contratos REST do Backend

Este documento resume os contratos que o frontend externo deve consumir. O projeto continua sendo somente backend.

## Base

```txt
http://localhost:8080/api
```

## Envelope padrão

Todas as respostas de sucesso usam:

```json
{
  "sucesso": true,
  "mensagem": "Operação realizada com sucesso.",
  "dados": {},
  "timestamp": "2026-05-20T10:30:00"
}
```

Todas as respostas de erro usam:

```json
{
  "sucesso": false,
  "status": 400,
  "codigo": "REQUISICAO_INVALIDA",
  "mensagem": "Mensagem funcional do erro.",
  "erros": [],
  "timestamp": "2026-05-20T10:30:00"
}
```

## Login

```http
POST /api/auth/login
```

Entrada:

```json
{
  "matricula": "C000000",
  "senha": "1234",
  "nomeExibicao": "Usuário Teste"
}
```

Regra:

- primeiro acesso: cria usuário pela matrícula e senha;
- próximos acessos: busca pela matrícula e valida a senha;
- senha inválida: retorna erro `NAO_AUTORIZADO`.

## Portal inicial

```http
GET /api/portal/inicial/{usuarioId}
GET /api/portal/header
GET /api/portal/menu
```

O portal inicial consolida dados para a home: usuário, header, menu, serviços, conteúdos, favoritos, sistemas, acessos recentes, anotações, normativos, processos e resumo de cotações/propostas.

## Serviços internos

```http
GET  /api/servicos
GET  /api/servicos/{id}
POST /api/servicos/{id}/acessar?usuarioId=1
```

## Favoritos

```http
GET    /api/favoritos/usuario/{usuarioId}
POST   /api/favoritos
PUT    /api/favoritos/{id}
DELETE /api/favoritos/{id}
```

## Sistemas do usuário

```http
GET    /api/sistemas/usuario/{usuarioId}
POST   /api/sistemas
PUT    /api/sistemas/{id}
POST   /api/sistemas/{id}/acessar
DELETE /api/sistemas/{id}
```

## Acessos recentes

```http
GET /api/acessos-recentes/usuario/{usuarioId}?limite=8
```

## Conteúdos e normativos

```http
GET /api/conteudos
GET /api/conteudos/{id}

GET /api/normativos
GET /api/normativos/{id}
```

## Anotações

```http
GET    /api/anotacoes/usuario/{usuarioId}
POST   /api/anotacoes
PUT    /api/anotacoes/{id}
DELETE /api/anotacoes/{id}
```

## Processos internos e checklist

```http
GET  /api/processos
GET  /api/processos/{id}?usuarioId=1
POST /api/processos/checklist/{checklistId}/marcar
```

## Busca orientada

```http
GET /api/assistente/buscar?q=abertura&usuarioId=1
```

A busca é simples por palavra-chave; não depende de IA externa.

## Cotações/propostas

```http
GET   /api/cotacoes-propostas
GET   /api/cotacoes-propostas?status=EM_ANALISE&prioridade=ALTA&agencia=1234&termo=abertura&pagina=0&tamanho=10&ordenarPor=dataAtualizacao&direcao=desc
GET   /api/cotacoes-propostas/opcoes
GET   /api/cotacoes-propostas/filtros-aplicados
GET   /api/cotacoes-propostas/resumo
GET   /api/cotacoes-propostas/recentes?limite=6
GET   /api/cotacoes-propostas/{id}
POST  /api/cotacoes-propostas
PUT   /api/cotacoes-propostas/{id}
PATCH /api/cotacoes-propostas/{id}/status
POST  /api/cotacoes-propostas/{id}/comentarios
GET   /api/cotacoes-propostas/{id}/comentarios
GET   /api/cotacoes-propostas/{id}/historico
```

Status permitidos:

```txt
RASCUNHO
EM_ANALISE
PENDENTE_DOCUMENTACAO
APROVADA
REPROVADA
CANCELADA
```

Prioridades permitidas:

```txt
BAIXA
MEDIA
ALTA
```

## Usuário e preferências

```http
GET /api/usuarios/{id}
GET /api/usuarios/matricula/{matricula}
PUT /api/usuarios/{id}
GET /api/usuarios/{id}/preferencias
PUT /api/usuarios/{id}/preferencias
```

## Auditoria

```http
GET /api/auditoria/usuario/{usuarioId}
GET /api/auditoria/usuario/{usuarioId}/resumo
GET /api/auditoria/recentes
```

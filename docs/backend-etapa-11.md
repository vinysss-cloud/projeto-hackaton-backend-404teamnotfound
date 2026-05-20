# Etapa 11 — Integração do resumo de cotações/propostas no portal inicial

Esta etapa mantém o projeto como backend puro e apenas consolida informações da área de cotações/propostas dentro do payload do portal inicial.

## Objetivo

Permitir que a tela principal do Figma exiba, em uma única chamada, os indicadores e as últimas cotações/propostas da agência.

Endpoint já existente:

```http
GET /api/portal/inicial/{usuarioId}
```

Agora o retorno inclui também:

```json
{
  "resumoCotacoesPropostas": {
    "total": 5,
    "emAnalise": 2,
    "pendenteDocumentacao": 1,
    "aprovadas": 1,
    "reprovadas": 0,
    "canceladas": 1,
    "valorTotal": 125000.00,
    "valorAprovado": 45000.00
  },
  "cotacoesPropostasRecentes": [
    {
      "id": 1,
      "numero": "PROP-2026-0001",
      "agencia": "1234",
      "nomeCliente": "Maria Cliente",
      "produto": "Abertura de Conta",
      "status": "EM_ANALISE",
      "prioridade": "MEDIA"
    }
  ]
}
```

## Arquivos alterados

```txt
src/main/java/br/caixa/gov/hackathon/dto/PortalDTOs.java
src/main/java/br/caixa/gov/hackathon/service/PortalService.java
src/main/java/br/caixa/gov/hackathon/service/CotacaoPropostaService.java
```

## Ajustes técnicos

Além da integração com o portal inicial, esta etapa corrige a montagem da consulta em `CotacaoPropostaService.listar(...)` e adiciona o método:

```java
listarRecentes(int limite)
```

Esse método é usado pelo portal para retornar as últimas propostas atualizadas.

## Fluxo recomendado para o frontend

1. Fazer login:

```http
POST /api/auth/login
```

2. Guardar o `usuario.id` retornado.

3. Carregar o portal inicial:

```http
GET /api/portal/inicial/1
```

4. Renderizar os blocos:

```txt
header
menu
resumoCotacoesPropostas
cotacoesPropostasRecentes
servicosDestaque
favoritos
acessosRecentes
normativos
anotacoes
processos
```

## Observação

Esta etapa não cria tabela nova e não altera script SQL. Ela depende da Etapa 10, que criou as tabelas de cotações/propostas.

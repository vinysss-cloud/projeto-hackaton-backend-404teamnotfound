# Etapa 10 — Cotações e Propostas da Agência

Esta etapa cria os contratos de backend para a tela de **Cotações e Propostas da Agência** vista no protótipo do Figma.

O objetivo é atender a listagem, filtros, resumo, detalhe, criação, edição, mudança de status, comentários e histórico da proposta.

## Tabelas criadas

Script Flyway:

```txt
src/main/resources/db/migration/V4__create_cotacoes_propostas_schema.sql
```

Script SQL Server equivalente:

```txt
database/sqlserver/04_cotacoes_propostas_sqlserver.sql
```

Tabelas:

```txt
cotacoes_propostas
historico_proposta
comentarios_proposta
```

## Endpoints

### Listar cotações/propostas

```http
GET /api/cotacoes-propostas
GET /api/cotacoes-propostas?status=EM_ANALISE
GET /api/cotacoes-propostas?agencia=1234
GET /api/cotacoes-propostas?termo=abertura
```

### Resumo da tela

```http
GET /api/cotacoes-propostas/resumo
```

Retorna totais para cards ou indicadores superiores da tela.

### Detalhar proposta

```http
GET /api/cotacoes-propostas/{id}
```

Retorna a proposta, o histórico e os comentários.

### Criar proposta

```http
POST /api/cotacoes-propostas
```

Exemplo:

```json
{
  "agencia": "1234",
  "nomeCliente": "Maria Cliente",
  "cpfCnpj": "12345678900",
  "produto": "Abertura de Conta",
  "modalidade": "Conta Pessoa Física",
  "valor": 0.00,
  "status": "EM_ANALISE",
  "prioridade": "MEDIA",
  "responsavel": "Atendente Agência",
  "matriculaResponsavel": "C123456",
  "prazoResposta": "2026-05-25",
  "observacao": "Cliente iniciou a solicitação pela agência."
}
```

O campo `numero` é opcional. Se não for enviado, o backend gera automaticamente.

### Atualizar proposta

```http
PUT /api/cotacoes-propostas/{id}
```

### Alterar status

```http
PATCH /api/cotacoes-propostas/{id}/status
```

Exemplo:

```json
{
  "status": "APROVADA",
  "usuario": "Gerente Agência",
  "matriculaUsuario": "C000002",
  "descricao": "Proposta aprovada após conferência documental."
}
```

### Comentários

```http
POST /api/cotacoes-propostas/{id}/comentarios
GET  /api/cotacoes-propostas/{id}/comentarios
```

### Histórico

```http
GET /api/cotacoes-propostas/{id}/historico
```

## Status disponíveis

```txt
RASCUNHO
EM_ANALISE
PENDENTE_DOCUMENTACAO
APROVADA
REPROVADA
CANCELADA
```

## Prioridades disponíveis

```txt
BAIXA
MEDIA
ALTA
```

## Como aplicar

Copie os arquivos desta etapa para o projeto backend.

Depois rode localmente:

```bash
./mvnw clean quarkus:dev
```

Para SQL Server, execute manualmente:

```txt
database/sqlserver/04_cotacoes_propostas_sqlserver.sql
```

## Próxima etapa sugerida

Etapa 11 — Ajustar o endpoint `/api/portal/inicial/{usuarioId}` para incluir uma seção resumida de cotações/propostas na tela inicial, caso o frontend precise mostrar essa informação no dashboard.

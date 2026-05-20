# Etapa 12 — Refinamento da API de Cotações e Propostas

Esta etapa mantém o projeto como backend puro e refina a API de cotações/propostas para consumo pelo sistema frontend externo.

## Objetivo

Melhorar a listagem de cotações/propostas com:

- paginação;
- filtros combinados;
- ordenação segura;
- busca por termo;
- filtro por período;
- filtro por status;
- filtro por prioridade;
- payload resumido para listagens;
- endpoint de opções para combos/filtros;
- endpoint de recentes;
- resumo consolidado.

Não foram criadas telas, componentes Angular, CSS ou HTML.

## Arquivos alterados

```txt
src/main/java/br/caixa/gov/hackathon/dto/CotacaoPropostaDTOs.java
src/main/java/br/caixa/gov/hackathon/service/CotacaoPropostaService.java
src/main/java/br/caixa/gov/hackathon/resource/CotacaoPropostaResource.java
```

## Endpoint principal de listagem

```http
GET /api/cotacoes-propostas
```

Parâmetros aceitos:

```txt
status
prioridade
agencia
termo
dataInicio
dataFim
pagina
tamanho
ordenarPor
direcao
```

Exemplo:

```http
GET /api/cotacoes-propostas?status=EM_ANALISE&prioridade=ALTA&agencia=1234&termo=abertura&pagina=0&tamanho=10&ordenarPor=dataAtualizacao&direcao=desc
```

Exemplo com período:

```http
GET /api/cotacoes-propostas?dataInicio=2026-05-01&dataFim=2026-05-31
```

## Retorno paginado

```json
{
  "itens": [
    {
      "id": 1,
      "numero": "PROP-2026-0001",
      "agencia": "1234",
      "nomeCliente": "Maria Cliente",
      "produto": "Abertura de Conta",
      "modalidade": "Conta Pessoa Física",
      "valor": 0.00,
      "status": "EM_ANALISE",
      "prioridade": "MEDIA",
      "responsavel": "Atendente Agência",
      "prazoResposta": "2026-05-25",
      "dataAtualizacao": "2026-05-20T10:30:00"
    }
  ],
  "totalElementos": 1,
  "totalPaginas": 1,
  "pagina": 0,
  "tamanho": 10,
  "primeira": true,
  "ultima": true,
  "possuiProxima": false,
  "possuiAnterior": false,
  "ordenarPor": "dataAtualizacao",
  "direcao": "desc"
}
```

## Campos de ordenação permitidos

```txt
id
numero
agencia
nomeCliente
produto
status
prioridade
responsavel
prazoResposta
dataCriacao
dataAtualizacao
valor
```

## Direções permitidas

```txt
asc
desc
```

## Endpoint de opções

```http
GET /api/cotacoes-propostas/opcoes
```

Retorna os valores possíveis para status, prioridade, ordenação e direção.

## Endpoint de filtros aplicados

```http
GET /api/cotacoes-propostas/filtros-aplicados?status=aprovada&prioridade=alta&ordenarPor=valor&direcao=asc
```

Retorna os filtros normalizados. É útil para depuração e para o frontend confirmar o estado atual da busca.

## Endpoint de recentes

```http
GET /api/cotacoes-propostas/recentes?limite=6
```

Retorna as propostas mais recentes, respeitando limite máximo de 20.

## Endpoint de resumo

```http
GET /api/cotacoes-propostas/resumo
```

Agora o resumo também retorna a quantidade em `RASCUNHO`.

## Observações técnicas

- A listagem principal agora retorna `CotacaoPropostaListaResponse`, mais leve para grids/listagens.
- O detalhe continua retornando dados completos com histórico e comentários.
- A ordenação é validada por whitelist para evitar campos inválidos.
- `tamanho` tem limite máximo de 50 para evitar retorno excessivo.
- `pagina` começa em 0.
- `dataFim` é tratada de forma inclusiva, internamente convertida para menor que o próximo dia.

## Validação sugerida

```bash
./mvnw clean test
./mvnw quarkus:dev
```

Depois testar:

```http
GET /api/cotacoes-propostas/opcoes
GET /api/cotacoes-propostas?pagina=0&tamanho=10
GET /api/cotacoes-propostas?status=EM_ANALISE&prioridade=MEDIA
GET /api/cotacoes-propostas?termo=abertura
GET /api/cotacoes-propostas?ordenarPor=valor&direcao=desc
GET /api/cotacoes-propostas/recentes?limite=6
GET /api/cotacoes-propostas/resumo
```

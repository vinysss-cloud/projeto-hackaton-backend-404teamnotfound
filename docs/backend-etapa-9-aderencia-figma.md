# Etapa 9 — Adequação do backend às telas do Figma

Esta etapa mantém o projeto somente como backend Quarkus e adiciona os contratos REST necessários para as telas identificadas no protótipo:

- Tela principal / home
- Meus Favoritos
- Meus Sistemas
- Acessos Recentes
- Normativos
- Favoritos & Anotações
- Abertura de Conta — Orientações de Pré-Acesso
- Busca orientada / assistente simples

Não há Angular neste pacote. O frontend ficará em outro repositório e apenas consumirá estes endpoints.

## Novas tabelas

Criadas pelo Flyway no arquivo:

```txt
src/main/resources/db/migration/V3__adequacao_figma_portal.sql
```

Tabelas adicionadas:

```txt
favoritos_usuario
sistemas_usuario
anotacoes_usuario
processos_internos
checklist_processo
usuario_checklist_processo
```

Script equivalente para SQL Server:

```txt
database/sqlserver/03_adequacao_figma_portal.sql
```

## Endpoints adicionados

### Favoritos

```http
GET    /api/favoritos/usuario/{usuarioId}
POST   /api/favoritos
PUT    /api/favoritos/{id}
DELETE /api/favoritos/{id}
```

Exemplo para adicionar favorito:

```json
{
  "usuarioId": 1,
  "servicoId": 1,
  "tituloCustomizado": "Abertura de Conta",
  "observacao": "Atalho usado com frequência"
}
```

### Meus Sistemas

```http
GET    /api/sistemas/usuario/{usuarioId}
POST   /api/sistemas
PUT    /api/sistemas/{id}
POST   /api/sistemas/{id}/acessar
DELETE /api/sistemas/{id}
```

Exemplo para adicionar sistema:

```json
{
  "usuarioId": 1,
  "nome": "Sistema de Propostas",
  "descricao": "Consulta e acompanhamento de propostas da agência.",
  "url": "https://intranet.caixa/sistema-propostas",
  "categoria": "Propostas",
  "icone": "grid",
  "favorito": true
}
```

### Anotações

```http
GET    /api/anotacoes/usuario/{usuarioId}
POST   /api/anotacoes
PUT    /api/anotacoes/{id}
DELETE /api/anotacoes/{id}
```

Exemplo:

```json
{
  "usuarioId": 1,
  "titulo": "Pendência da agência",
  "descricao": "Revisar documentação antes de enviar a proposta.",
  "referencia": "Abertura de Conta"
}
```

### Acessos Recentes

```http
GET /api/acessos-recentes/usuario/{usuarioId}?limite=8
```

Este endpoint usa a tabela `acessos_servico`, já alimentada por:

```http
POST /api/servicos/{id}/acessar?usuarioId=1
```

### Normativos

```http
GET /api/normativos
GET /api/normativos/{id}
```

Normativos usam a tabela `conteudos_internos` com `tipo = 'NORMATIVO'`.

### Processos e checklist

```http
GET  /api/processos
GET  /api/processos/{id}?usuarioId=1
POST /api/processos/checklist/{checklistId}/marcar
```

Exemplo para marcar item do checklist:

```json
{
  "usuarioId": 1,
  "concluido": true
}
```

### Assistente / busca orientada

```http
GET /api/assistente/buscar?q=abertura
```

O endpoint busca em:

```txt
servicos_internos
processos_internos
conteudos_internos
```

Não é uma IA real. Para o MVP, funciona como busca orientada por palavra-chave.

## Portal inicial atualizado

O endpoint existente foi ampliado:

```http
GET /api/portal/inicial/{usuarioId}
```

Agora retorna também:

```txt
normativos
favoritos
acessosRecentes
anotacoes
processos
```

Assim a home consegue montar as áreas principais do Figma com uma chamada única.

## Menu ajustado ao protótipo

O menu retornado por:

```http
GET /api/portal/menu
```

passa a ser:

```txt
Início
Meus Sistemas
Acessos Recentes
Normativos
```

## Fluxo recomendado para testar

```http
POST /api/auth/login
GET  /api/portal/inicial/1
GET  /api/assistente/buscar?q=abertura
GET  /api/processos/1?usuarioId=1
POST /api/processos/checklist/1/marcar
POST /api/favoritos
GET  /api/favoritos/usuario/1
POST /api/sistemas
GET  /api/sistemas/usuario/1
POST /api/anotacoes
GET  /api/anotacoes/usuario/1
```

## Próxima etapa sugerida

Etapa 10 — Endpoints de cotações/propostas da agência, caso essa tela precise deixar de ser apenas visual e passe a consultar dados reais em lista/tabela.

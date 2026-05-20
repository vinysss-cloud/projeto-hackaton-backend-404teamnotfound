# Etapa 16 — Auditoria automática nos fluxos principais

Esta etapa mantém o projeto como **somente backend** e adiciona rastreabilidade automática nos fluxos mais importantes para integração com o frontend externo.

## Objetivo

Registrar ações relevantes sem depender do frontend chamar manualmente um endpoint de auditoria após cada operação.

## Fluxos auditados automaticamente

- Login no primeiro acesso.
- Login com sucesso.
- Falha de login por usuário inativo ou senha inválida.
- Acesso ao portal inicial.
- Busca orientada pelo assistente.
- Criação de cotação/proposta.
- Atualização de cotação/proposta.
- Alteração de status da cotação/proposta.
- Inclusão de comentário na cotação/proposta.

Observação: alguns fluxos como acesso a serviços, sistemas e checklist de processo já registravam auditoria antes desta etapa.

## Endpoints de consulta da auditoria

```http
GET /api/auditoria/usuario/{usuarioId}
GET /api/auditoria/usuario/{usuarioId}?limite=50
GET /api/auditoria/usuario/{usuarioId}/resumo
GET /api/auditoria/recentes
GET /api/auditoria/recentes?limite=50
```

## Endpoint de busca orientada

O endpoint de busca passa a aceitar `usuarioId` opcional para registrar quem realizou a consulta.

```http
GET /api/assistente/buscar?q=abertura&usuarioId=1
```

Se o `usuarioId` não for enviado, a busca continua funcionando normalmente, mas a auditoria fica registrada sem vínculo com usuário.

## Exemplos de ações gravadas

```txt
PORTAL_INICIAL_ACESSADO
ASSISTENTE_BUSCA_REALIZADA
ASSISTENTE_BUSCA_INVALIDA
COTACAO_PROPOSTA_CRIADA
COTACAO_PROPOSTA_ATUALIZADA
COTACAO_PROPOSTA_STATUS_ALTERADO
COTACAO_PROPOSTA_COMENTARIO_INCLUIDO
```

## Tabela usada

Esta etapa utiliza a tabela já existente:

```txt
auditoria_acessos
```

Não foi necessário criar novo script de banco.

## Arquivos alterados

```txt
src/main/java/br/caixa/gov/hackathon/dto/AuditoriaDTOs.java
src/main/java/br/caixa/gov/hackathon/resource/AuditoriaResource.java
src/main/java/br/caixa/gov/hackathon/resource/AssistenteResource.java
src/main/java/br/caixa/gov/hackathon/service/AuditoriaService.java
src/main/java/br/caixa/gov/hackathon/service/AssistenteService.java
src/main/java/br/caixa/gov/hackathon/service/PortalService.java
src/main/java/br/caixa/gov/hackathon/service/CotacaoPropostaService.java
```

## Teste rápido

1. Faça login:

```http
POST /api/auth/login
```

2. Acesse o portal:

```http
GET /api/portal/inicial/1
```

3. Faça uma busca:

```http
GET /api/assistente/buscar?q=abertura&usuarioId=1
```

4. Consulte a auditoria:

```http
GET /api/auditoria/usuario/1
```

5. Consulte o resumo:

```http
GET /api/auditoria/usuario/1/resumo
```

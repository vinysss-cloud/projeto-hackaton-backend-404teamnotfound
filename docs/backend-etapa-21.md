# Etapa 21 — Índices de performance para auditoria e propostas

Esta etapa cria índices para melhorar as consultas mais usadas pelo backend em auditoria e cotações/propostas.

## Arquivos incluídos

```txt
src/main/resources/db/migration/V6__create_performance_indexes.sql
database/sqlserver/06_performance_indexes.sql
database/sqlserver/00_executar_todos_sqlserver.sql
```

## Auditoria

Consultas beneficiadas:

```http
GET /api/auditoria/usuario/{usuarioId}
GET /api/auditoria/usuario/{usuarioId}/resumo
GET /api/auditoria/recentes
```

Índices criados:

```txt
ix_auditoria_usuario_criado
ix_auditoria_criado
ix_auditoria_matricula_criado
ix_auditoria_acao_criado
```

Esses índices melhoram listagem por usuário, atividades recentes, filtros por matrícula e filtros por ação.

## Cotações/propostas

Consultas beneficiadas:

```http
GET /api/cotacoes-propostas
GET /api/cotacoes-propostas/recentes
GET /api/cotacoes-propostas/resumo
GET /api/cotacoes-propostas/{id}/historico
GET /api/cotacoes-propostas/{id}/comentarios
GET /api/portal/inicial/{usuarioId}
```

Índices criados para filtros e ordenação por:

```txt
ativo + data_atualizacao
ativo + status + data_atualizacao
ativo + agencia + data_atualizacao
ativo + prioridade + data_atualizacao
ativo + data_criacao
ativo + status + prioridade + agencia + data_atualizacao
proposta_id + criado_em no histórico
proposta_id + ativo + criado_em nos comentários
```

## SQL Server

O arquivo `database/sqlserver/06_performance_indexes.sql` é idempotente. Ele pode ser executado mais de uma vez, pois verifica se o índice já existe antes de criar.

Para SQL Server, os principais índices de propostas foram criados como índices filtrados com `WHERE ativo = 1`, reduzindo o tamanho do índice e acelerando a listagem principal de propostas ativas.

## Como aplicar

### H2/Flyway local

Basta subir a aplicação:

```bash
./mvnw quarkus:dev
```

O Flyway executará:

```txt
V6__create_performance_indexes.sql
```

### SQL Server

Execute manualmente:

```txt
database/sqlserver/06_performance_indexes.sql
```

Ou use o script mestre:

```txt
database/sqlserver/00_executar_todos_sqlserver.sql
```

No SSMS, habilite antes:

```txt
Query > SQLCMD Mode
```

## Observação

Índices aceleram leitura, mas aumentam um pouco o custo de escrita em inserts/updates. Para este projeto, vale a pena porque auditoria e propostas serão consultadas com frequência pelo portal e pela listagem.

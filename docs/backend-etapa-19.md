# Etapa 19 — Revisão dos scripts SQL Server

Esta etapa deixa os scripts SQL Server mais seguros para apresentação e homologação simples.

## Objetivo

Garantir que os scripts possam ser executados mais de uma vez sem apagar dados e sem falhar por tabela ou índice já existente.

## Arquivos alterados/criados

```txt
database/sqlserver/00_executar_todos_sqlserver.sql
database/sqlserver/01_schema_sqlserver.sql
database/sqlserver/02_seed_sqlserver.sql
database/sqlserver/03_adequacao_figma_portal.sql
database/sqlserver/04_cotacoes_propostas_sqlserver.sql
database/sqlserver/05_fix_backend_consistency.sql
```

## O que mudou

- `01_schema_sqlserver.sql` agora usa `IF OBJECT_ID(..., 'U') IS NULL` antes de criar tabelas.
- Índices agora usam verificação em `sys.indexes` antes de criar.
- Seeds agora usam `IF NOT EXISTS` com `BEGIN/END` e `GO`.
- `04_cotacoes_propostas_sqlserver.sql` já cria `produto`, `modalidade` e `observacao` com tamanhos compatíveis com os DTOs/entities.
- `05_fix_backend_consistency.sql` foi criado para bancos que já existiam antes dessas correções.
- `00_executar_todos_sqlserver.sql` foi criado como script mestre para execução em ordem.

## Como executar no SQL Server

### Opção 1 — SSMS

1. Abra o SQL Server Management Studio.
2. Selecione o banco `caixa_hub`.
3. Execute os scripts nesta ordem:

```txt
01_schema_sqlserver.sql
02_seed_sqlserver.sql
03_adequacao_figma_portal.sql
04_cotacoes_propostas_sqlserver.sql
05_fix_backend_consistency.sql
```

### Opção 2 — Script mestre

No SSMS, habilite:

```txt
Query > SQLCMD Mode
```

Depois execute:

```txt
00_executar_todos_sqlserver.sql
```

### Opção 3 — Terminal com sqlcmd

```bash
sqlcmd -S localhost,1433 -d caixa_hub -U sa -P "SuaSenha@123" -i database/sqlserver/01_schema_sqlserver.sql
sqlcmd -S localhost,1433 -d caixa_hub -U sa -P "SuaSenha@123" -i database/sqlserver/02_seed_sqlserver.sql
sqlcmd -S localhost,1433 -d caixa_hub -U sa -P "SuaSenha@123" -i database/sqlserver/03_adequacao_figma_portal.sql
sqlcmd -S localhost,1433 -d caixa_hub -U sa -P "SuaSenha@123" -i database/sqlserver/04_cotacoes_propostas_sqlserver.sql
sqlcmd -S localhost,1433 -d caixa_hub -U sa -P "SuaSenha@123" -i database/sqlserver/05_fix_backend_consistency.sql
```

## Importante

Nenhum script desta etapa usa `DROP TABLE`.

Eles não limpam banco e não apagam dados. A ideia é permitir rodar com segurança antes da apresentação.

## Próxima etapa sugerida

Etapa 20 — revisão final de execução local, Swagger e checklist de integração com o frontend externo.

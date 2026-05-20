/*
 Script mestre para execução em SQL Server.
 No SQL Server Management Studio, habilite: Query > SQLCMD Mode.
 Ajuste os caminhos se os arquivos estiverem em outra pasta.
*/
:r 01_schema_sqlserver.sql
:r 02_seed_sqlserver.sql
:r 03_adequacao_figma_portal.sql
:r 04_cotacoes_propostas_sqlserver.sql
:r 05_fix_backend_consistency.sql
:r 06_performance_indexes.sql

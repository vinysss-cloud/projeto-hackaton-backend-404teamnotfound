/*
 Etapa 19 - Ajustes de consistência SQL Server.
 Use este script caso o banco já tenha sido criado antes da Etapa 19.
 Ele não apaga dados; apenas amplia colunas e cria índices ausentes.
*/

IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL AND COL_LENGTH('cotacoes_propostas', 'produto') IS NOT NULL
    ALTER TABLE cotacoes_propostas ALTER COLUMN produto VARCHAR(120) NOT NULL;
GO

IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL AND COL_LENGTH('cotacoes_propostas', 'modalidade') IS NOT NULL
    ALTER TABLE cotacoes_propostas ALTER COLUMN modalidade VARCHAR(120) NULL;
GO

IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL AND COL_LENGTH('cotacoes_propostas', 'observacao') IS NOT NULL
    ALTER TABLE cotacoes_propostas ALTER COLUMN observacao VARCHAR(1000) NULL;
GO

IF OBJECT_ID('usuarios', 'U') IS NOT NULL AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_usuarios_matricula' AND object_id = OBJECT_ID('usuarios'))
    CREATE INDEX ix_usuarios_matricula ON usuarios(matricula);
GO

IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_status' AND object_id = OBJECT_ID('cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_status ON cotacoes_propostas(status);
GO

IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_agencia' AND object_id = OBJECT_ID('cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_agencia ON cotacoes_propostas(agencia);
GO

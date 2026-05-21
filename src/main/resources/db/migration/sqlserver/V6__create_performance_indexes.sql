-- V6 - SQL Server

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_auditoria_usuario_criado' AND object_id = OBJECT_ID('dbo.auditoria_acessos'))
    CREATE INDEX ix_auditoria_usuario_criado ON dbo.auditoria_acessos(usuario_id, criado_em DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_auditoria_criado' AND object_id = OBJECT_ID('dbo.auditoria_acessos'))
    CREATE INDEX ix_auditoria_criado ON dbo.auditoria_acessos(criado_em DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_auditoria_matricula_criado' AND object_id = OBJECT_ID('dbo.auditoria_acessos'))
    CREATE INDEX ix_auditoria_matricula_criado ON dbo.auditoria_acessos(matricula, criado_em DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_auditoria_acao_criado' AND object_id = OBJECT_ID('dbo.auditoria_acessos'))
    CREATE INDEX ix_auditoria_acao_criado ON dbo.auditoria_acessos(acao, criado_em DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativo_atualizacao' AND object_id = OBJECT_ID('dbo.cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_ativo_atualizacao ON dbo.cotacoes_propostas(ativo, data_atualizacao DESC, id DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativo_status_atualizacao' AND object_id = OBJECT_ID('dbo.cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_ativo_status_atualizacao ON dbo.cotacoes_propostas(ativo, status, data_atualizacao DESC, id DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativo_agencia_atualizacao' AND object_id = OBJECT_ID('dbo.cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_ativo_agencia_atualizacao ON dbo.cotacoes_propostas(ativo, agencia, data_atualizacao DESC, id DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativo_prioridade_atualizacao' AND object_id = OBJECT_ID('dbo.cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_ativo_prioridade_atualizacao ON dbo.cotacoes_propostas(ativo, prioridade, data_atualizacao DESC, id DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativo_data_criacao' AND object_id = OBJECT_ID('dbo.cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_ativo_data_criacao ON dbo.cotacoes_propostas(ativo, data_criacao DESC, id DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativo_status_prioridade_agencia' AND object_id = OBJECT_ID('dbo.cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_ativo_status_prioridade_agencia
    ON dbo.cotacoes_propostas(ativo, status, prioridade, agencia, data_atualizacao DESC, id DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_historico_proposta_criado' AND object_id = OBJECT_ID('dbo.historico_proposta'))
    CREATE INDEX ix_historico_proposta_criado ON dbo.historico_proposta(proposta_id, criado_em DESC);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_comentarios_proposta_ativo_criado' AND object_id = OBJECT_ID('dbo.comentarios_proposta'))
    CREATE INDEX ix_comentarios_proposta_ativo_criado ON dbo.comentarios_proposta(proposta_id, ativo, criado_em DESC);
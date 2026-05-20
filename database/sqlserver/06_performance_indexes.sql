/*
 Etapa 21 - Índices de performance para SQL Server
 Objetivo: melhorar auditoria e cotações/propostas sem apagar ou recriar tabelas.
 Pode ser executado mais de uma vez.
*/

-- =========================
-- Auditoria
-- =========================
IF OBJECT_ID('auditoria_acessos', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_auditoria_usuario_criado' AND object_id = OBJECT_ID('auditoria_acessos'))
BEGIN
    CREATE INDEX ix_auditoria_usuario_criado
        ON auditoria_acessos(usuario_id, criado_em DESC);
END;
GO

IF OBJECT_ID('auditoria_acessos', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_auditoria_criado' AND object_id = OBJECT_ID('auditoria_acessos'))
BEGIN
    CREATE INDEX ix_auditoria_criado
        ON auditoria_acessos(criado_em DESC);
END;
GO

IF OBJECT_ID('auditoria_acessos', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_auditoria_matricula_criado' AND object_id = OBJECT_ID('auditoria_acessos'))
BEGIN
    CREATE INDEX ix_auditoria_matricula_criado
        ON auditoria_acessos(matricula, criado_em DESC);
END;
GO

IF OBJECT_ID('auditoria_acessos', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_auditoria_acao_criado' AND object_id = OBJECT_ID('auditoria_acessos'))
BEGIN
    CREATE INDEX ix_auditoria_acao_criado
        ON auditoria_acessos(acao, criado_em DESC);
END;
GO

-- =========================
-- Cotações/propostas
-- Usamos índices filtrados por ativo = 1 para reduzir tamanho e acelerar o grid principal.
-- =========================
IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativas_atualizacao' AND object_id = OBJECT_ID('cotacoes_propostas'))
BEGIN
    CREATE INDEX ix_cotacoes_ativas_atualizacao
        ON cotacoes_propostas(data_atualizacao DESC, id DESC)
        INCLUDE (numero, agencia, nome_cliente, produto, modalidade, valor, status, prioridade, responsavel, prazo_resposta)
        WHERE ativo = 1;
END;
GO

IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativas_status_atualizacao' AND object_id = OBJECT_ID('cotacoes_propostas'))
BEGIN
    CREATE INDEX ix_cotacoes_ativas_status_atualizacao
        ON cotacoes_propostas(status, data_atualizacao DESC, id DESC)
        INCLUDE (numero, agencia, nome_cliente, produto, modalidade, valor, prioridade, responsavel, prazo_resposta)
        WHERE ativo = 1;
END;
GO

IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativas_agencia_atualizacao' AND object_id = OBJECT_ID('cotacoes_propostas'))
BEGIN
    CREATE INDEX ix_cotacoes_ativas_agencia_atualizacao
        ON cotacoes_propostas(agencia, data_atualizacao DESC, id DESC)
        INCLUDE (numero, nome_cliente, produto, modalidade, valor, status, prioridade, responsavel, prazo_resposta)
        WHERE ativo = 1;
END;
GO

IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativas_prioridade_atualizacao' AND object_id = OBJECT_ID('cotacoes_propostas'))
BEGIN
    CREATE INDEX ix_cotacoes_ativas_prioridade_atualizacao
        ON cotacoes_propostas(prioridade, data_atualizacao DESC, id DESC)
        INCLUDE (numero, agencia, nome_cliente, produto, modalidade, valor, status, responsavel, prazo_resposta)
        WHERE ativo = 1;
END;
GO

IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativas_data_criacao' AND object_id = OBJECT_ID('cotacoes_propostas'))
BEGIN
    CREATE INDEX ix_cotacoes_ativas_data_criacao
        ON cotacoes_propostas(data_criacao DESC, id DESC)
        INCLUDE (numero, agencia, nome_cliente, produto, modalidade, valor, status, prioridade, responsavel, prazo_resposta)
        WHERE ativo = 1;
END;
GO

IF OBJECT_ID('cotacoes_propostas', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_ativas_status_prioridade_agencia' AND object_id = OBJECT_ID('cotacoes_propostas'))
BEGIN
    CREATE INDEX ix_cotacoes_ativas_status_prioridade_agencia
        ON cotacoes_propostas(status, prioridade, agencia, data_atualizacao DESC, id DESC)
        INCLUDE (numero, nome_cliente, produto, modalidade, valor, responsavel, prazo_resposta)
        WHERE ativo = 1;
END;
GO

-- Histórico e comentários da proposta
IF OBJECT_ID('historico_proposta', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_historico_proposta_criado' AND object_id = OBJECT_ID('historico_proposta'))
BEGIN
    CREATE INDEX ix_historico_proposta_criado
        ON historico_proposta(proposta_id, criado_em DESC)
        INCLUDE (status_anterior, status_novo, usuario, matricula_usuario);
END;
GO

IF OBJECT_ID('comentarios_proposta', 'U') IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_comentarios_proposta_ativo_criado' AND object_id = OBJECT_ID('comentarios_proposta'))
BEGIN
    CREATE INDEX ix_comentarios_proposta_ativo_criado
        ON comentarios_proposta(proposta_id, ativo, criado_em DESC)
        INCLUDE (autor, matricula_autor);
END;
GO

/*
 Etapa 19 - Cotações e propostas SQL Server idempotente.
 Pode ser executado mais de uma vez sem apagar dados.
*/

IF OBJECT_ID('cotacoes_propostas', 'U') IS NULL
BEGIN
    CREATE TABLE cotacoes_propostas (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        numero VARCHAR(40) NOT NULL,
        agencia VARCHAR(20) NOT NULL,
        nome_cliente VARCHAR(160) NOT NULL,
        cpf_cnpj VARCHAR(20) NULL,
        produto VARCHAR(120) NOT NULL,
        modalidade VARCHAR(120) NULL,
        valor DECIMAL(15, 2) NOT NULL,
        status VARCHAR(30) NOT NULL,
        prioridade VARCHAR(20) NOT NULL CONSTRAINT df_cotacoes_prioridade DEFAULT 'MEDIA',
        responsavel VARCHAR(120) NOT NULL,
        matricula_responsavel VARCHAR(30) NULL,
        data_criacao DATETIME2 NOT NULL,
        data_atualizacao DATETIME2 NOT NULL,
        prazo_resposta DATE NULL,
        observacao VARCHAR(1000) NULL,
        ativo BIT NOT NULL CONSTRAINT df_cotacoes_ativo DEFAULT 1,
        CONSTRAINT uk_cotacoes_numero UNIQUE (numero)
    );
END;
GO

IF COL_LENGTH('cotacoes_propostas', 'produto') IS NOT NULL
    ALTER TABLE cotacoes_propostas ALTER COLUMN produto VARCHAR(120) NOT NULL;
GO
IF COL_LENGTH('cotacoes_propostas', 'modalidade') IS NOT NULL
    ALTER TABLE cotacoes_propostas ALTER COLUMN modalidade VARCHAR(120) NULL;
GO
IF COL_LENGTH('cotacoes_propostas', 'observacao') IS NOT NULL
    ALTER TABLE cotacoes_propostas ALTER COLUMN observacao VARCHAR(1000) NULL;
GO

IF OBJECT_ID('historico_proposta', 'U') IS NULL
BEGIN
    CREATE TABLE historico_proposta (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        proposta_id BIGINT NOT NULL,
        status_anterior VARCHAR(30) NULL,
        status_novo VARCHAR(30) NOT NULL,
        usuario VARCHAR(120) NOT NULL,
        matricula_usuario VARCHAR(30) NULL,
        descricao VARCHAR(500) NULL,
        criado_em DATETIME2 NOT NULL,
        CONSTRAINT fk_historico_proposta FOREIGN KEY (proposta_id) REFERENCES cotacoes_propostas(id)
    );
END;
GO

IF OBJECT_ID('comentarios_proposta', 'U') IS NULL
BEGIN
    CREATE TABLE comentarios_proposta (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        proposta_id BIGINT NOT NULL,
        autor VARCHAR(120) NOT NULL,
        matricula_autor VARCHAR(30) NULL,
        comentario VARCHAR(1000) NOT NULL,
        criado_em DATETIME2 NOT NULL,
        ativo BIT NOT NULL CONSTRAINT df_comentarios_ativo DEFAULT 1,
        CONSTRAINT fk_comentario_proposta FOREIGN KEY (proposta_id) REFERENCES cotacoes_propostas(id)
    );
END;
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_status' AND object_id = OBJECT_ID('cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_status ON cotacoes_propostas(status);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_agencia' AND object_id = OBJECT_ID('cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_agencia ON cotacoes_propostas(agencia);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_cliente' AND object_id = OBJECT_ID('cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_cliente ON cotacoes_propostas(nome_cliente);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_historico_proposta' AND object_id = OBJECT_ID('historico_proposta'))
    CREATE INDEX ix_historico_proposta ON historico_proposta(proposta_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_comentarios_proposta' AND object_id = OBJECT_ID('comentarios_proposta'))
    CREATE INDEX ix_comentarios_proposta ON comentarios_proposta(proposta_id);
GO

IF NOT EXISTS (SELECT 1 FROM cotacoes_propostas WHERE numero = 'PROP-2026-0001')
BEGIN
    INSERT INTO cotacoes_propostas
    (numero, agencia, nome_cliente, cpf_cnpj, produto, modalidade, valor, status, prioridade, responsavel, matricula_responsavel, data_criacao, data_atualizacao, prazo_resposta, observacao, ativo)
    VALUES
    ('PROP-2026-0001', '1234', 'Cliente Demonstração 01', '00000000000', 'Abertura de Conta', 'Conta Pessoa Física', 0.00, 'EM_ANALISE', 'MEDIA', 'Atendente Agência', 'C000001', SYSDATETIME(), SYSDATETIME(), DATEADD(DAY, 2, CAST(GETDATE() AS DATE)), 'Registro inicial para demonstrar a tela de cotações e propostas da agência.', 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM cotacoes_propostas WHERE numero = 'PROP-2026-0002')
BEGIN
    INSERT INTO cotacoes_propostas
    (numero, agencia, nome_cliente, cpf_cnpj, produto, modalidade, valor, status, prioridade, responsavel, matricula_responsavel, data_criacao, data_atualizacao, prazo_resposta, observacao, ativo)
    VALUES
    ('PROP-2026-0002', '1234', 'Cliente Demonstração 02', '11111111111', 'Habitação', 'Financiamento Habitacional', 350000.00, 'PENDENTE_DOCUMENTACAO', 'ALTA', 'Gerente Agência', 'C000002', SYSDATETIME(), SYSDATETIME(), DATEADD(DAY, 5, CAST(GETDATE() AS DATE)), 'Aguardando complementação documental.', 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM cotacoes_propostas WHERE numero = 'PROP-2026-0003')
BEGIN
    INSERT INTO cotacoes_propostas
    (numero, agencia, nome_cliente, cpf_cnpj, produto, modalidade, valor, status, prioridade, responsavel, matricula_responsavel, data_criacao, data_atualizacao, prazo_resposta, observacao, ativo)
    VALUES
    ('PROP-2026-0003', '5678', 'Empresa Demonstração LTDA', '22222222000100', 'Capital de Giro', 'Pessoa Jurídica', 80000.00, 'APROVADA', 'MEDIA', 'Consultor PJ', 'C000003', SYSDATETIME(), SYSDATETIME(), DATEADD(DAY, 1, CAST(GETDATE() AS DATE)), 'Proposta aprovada para simulação do painel.', 1);
END;
GO

INSERT INTO historico_proposta (proposta_id, status_anterior, status_novo, usuario, matricula_usuario, descricao, criado_em)
SELECT p.id, NULL, p.status, p.responsavel, p.matricula_responsavel, 'Registro inicial criado por carga de dados.', SYSDATETIME()
FROM cotacoes_propostas p
WHERE NOT EXISTS (SELECT 1 FROM historico_proposta h WHERE h.proposta_id = p.id);
GO

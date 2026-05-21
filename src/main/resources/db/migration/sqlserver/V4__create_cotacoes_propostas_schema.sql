-- V4 - SQL Server

IF OBJECT_ID('dbo.cotacoes_propostas', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.cotacoes_propostas (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_cotacoes_propostas PRIMARY KEY,
        numero VARCHAR(40) NOT NULL CONSTRAINT uq_cotacoes_numero UNIQUE,
        agencia VARCHAR(20) NOT NULL,
        nome_cliente VARCHAR(160) NOT NULL,
        cpf_cnpj VARCHAR(20) NULL,
        produto VARCHAR(80) NOT NULL,
        modalidade VARCHAR(80) NULL,
        valor DECIMAL(15,2) NOT NULL,
        status VARCHAR(30) NOT NULL,
        prioridade VARCHAR(20) NOT NULL CONSTRAINT df_cotacoes_prioridade DEFAULT ('MEDIA'),
        responsavel VARCHAR(120) NOT NULL,
        matricula_responsavel VARCHAR(30) NULL,
        data_criacao DATETIME2 NOT NULL,
        data_atualizacao DATETIME2 NOT NULL,
        prazo_resposta DATE NULL,
        observacao VARCHAR(800) NULL,
        ativo BIT NOT NULL CONSTRAINT df_cotacoes_ativo DEFAULT (1)
    );
END;

IF OBJECT_ID('dbo.historico_proposta', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.historico_proposta (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_historico_proposta PRIMARY KEY,
        proposta_id BIGINT NOT NULL,
        status_anterior VARCHAR(30) NULL,
        status_novo VARCHAR(30) NOT NULL,
        usuario VARCHAR(120) NOT NULL,
        matricula_usuario VARCHAR(30) NULL,
        descricao VARCHAR(500) NULL,
        criado_em DATETIME2 NOT NULL,
        CONSTRAINT fk_historico_proposta FOREIGN KEY (proposta_id) REFERENCES dbo.cotacoes_propostas(id)
    );
END;

IF OBJECT_ID('dbo.comentarios_proposta', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.comentarios_proposta (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_comentarios_proposta PRIMARY KEY,
        proposta_id BIGINT NOT NULL,
        autor VARCHAR(120) NOT NULL,
        matricula_autor VARCHAR(30) NULL,
        comentario VARCHAR(1000) NOT NULL,
        criado_em DATETIME2 NOT NULL,
        ativo BIT NOT NULL CONSTRAINT df_comentarios_ativo DEFAULT (1),
        CONSTRAINT fk_comentario_proposta FOREIGN KEY (proposta_id) REFERENCES dbo.cotacoes_propostas(id)
    );
END;

-- Índices
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_status' AND object_id = OBJECT_ID('dbo.cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_status ON dbo.cotacoes_propostas(status);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_agencia' AND object_id = OBJECT_ID('dbo.cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_agencia ON dbo.cotacoes_propostas(agencia);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_cotacoes_cliente' AND object_id = OBJECT_ID('dbo.cotacoes_propostas'))
    CREATE INDEX ix_cotacoes_cliente ON dbo.cotacoes_propostas(nome_cliente);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_historico_proposta' AND object_id = OBJECT_ID('dbo.historico_proposta'))
    CREATE INDEX ix_historico_proposta ON dbo.historico_proposta(proposta_id);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_comentarios_proposta' AND object_id = OBJECT_ID('dbo.comentarios_proposta'))
    CREATE INDEX ix_comentarios_proposta ON dbo.comentarios_proposta(proposta_id);

-- Seeds (sem TRUE e sem CURRENT_DATE + N)
INSERT INTO dbo.cotacoes_propostas (
    numero, agencia, nome_cliente, cpf_cnpj, produto, modalidade, valor, status, prioridade,
    responsavel, matricula_responsavel, data_criacao, data_atualizacao, prazo_resposta, observacao, ativo
)
SELECT 'PROP-2026-0001', '1234', 'Cliente Demonstração 01', '00000000000', 'Abertura de Conta', 'Conta Pessoa Física',
       0.00, 'EM_ANALISE', 'MEDIA', 'Atendente Agência', 'C000001',
       SYSDATETIME(), SYSDATETIME(),
       CAST(DATEADD(DAY, 2, GETDATE()) AS DATE),
       'Registro inicial para demonstrar a tela de cotações e propostas da agência.', 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.cotacoes_propostas WHERE numero = 'PROP-2026-0001');

INSERT INTO dbo.cotacoes_propostas (
    numero, agencia, nome_cliente, cpf_cnpj, produto, modalidade, valor, status, prioridade,
    responsavel, matricula_responsavel, data_criacao, data_atualizacao, prazo_resposta, observacao, ativo
)
SELECT 'PROP-2026-0002', '1234', 'Cliente Demonstração 02', '11111111111', 'Habitação', 'Financiamento Habitacional',
       350000.00, 'PENDENTE_DOCUMENTACAO', 'ALTA', 'Gerente Agência', 'C000002',
       SYSDATETIME(), SYSDATETIME(),
       CAST(DATEADD(DAY, 5, GETDATE()) AS DATE),
       'Aguardando complementação documental.', 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.cotacoes_propostas WHERE numero = 'PROP-2026-0002');

INSERT INTO dbo.cotacoes_propostas (
    numero, agencia, nome_cliente, cpf_cnpj, produto, modalidade, valor, status, prioridade,
    responsavel, matricula_responsavel, data_criacao, data_atualizacao, prazo_resposta, observacao, ativo
)
SELECT 'PROP-2026-0003', '5678', 'Empresa Demonstração LTDA', '22222222000100', 'Capital de Giro', 'Pessoa Jurídica',
       80000.00, 'APROVADA', 'MEDIA', 'Consultor PJ', 'C000003',
       SYSDATETIME(), SYSDATETIME(),
       CAST(DATEADD(DAY, 1, GETDATE()) AS DATE),
       'Proposta aprovada para simulação do painel.', 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.cotacoes_propostas WHERE numero = 'PROP-2026-0003');

INSERT INTO dbo.historico_proposta (proposta_id, status_anterior, status_novo, usuario, matricula_usuario, descricao, criado_em)
SELECT p.id, NULL, p.status, p.responsavel, p.matricula_responsavel, 'Registro inicial criado por carga de dados.', SYSDATETIME()
FROM dbo.cotacoes_propostas p
WHERE NOT EXISTS (SELECT 1 FROM dbo.historico_proposta h WHERE h.proposta_id = p.id);
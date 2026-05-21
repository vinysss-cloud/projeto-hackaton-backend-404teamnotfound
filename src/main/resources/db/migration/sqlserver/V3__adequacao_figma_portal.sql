-- V3 - SQL Server

IF OBJECT_ID('dbo.favoritos_usuario', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.favoritos_usuario (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_favoritos_usuario PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        servico_id BIGINT NOT NULL,
        titulo_customizado VARCHAR(120) NULL,
        observacao VARCHAR(500) NULL,
        ativo BIT NOT NULL CONSTRAINT df_favoritos_usuario_ativo DEFAULT (1),
        data_criacao DATETIME2 NOT NULL,
        CONSTRAINT fk_favoritos_usuario FOREIGN KEY (usuario_id) REFERENCES dbo.usuarios(id),
        CONSTRAINT fk_favoritos_servico FOREIGN KEY (servico_id) REFERENCES dbo.servicos_internos(id),
        CONSTRAINT uk_favorito_usuario_servico UNIQUE (usuario_id, servico_id)
    );
END;

IF OBJECT_ID('dbo.sistemas_usuario', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.sistemas_usuario (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_sistemas_usuario PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        nome VARCHAR(120) NOT NULL,
        descricao VARCHAR(500) NULL,
        url VARCHAR(300) NULL,
        categoria VARCHAR(80) NULL,
        icone VARCHAR(80) NULL,
        favorito BIT NOT NULL CONSTRAINT df_sistemas_usuario_favorito DEFAULT (0),
        ativo BIT NOT NULL CONSTRAINT df_sistemas_usuario_ativo DEFAULT (1),
        data_criacao DATETIME2 NOT NULL,
        data_atualizacao DATETIME2 NULL,
        CONSTRAINT fk_sistemas_usuario FOREIGN KEY (usuario_id) REFERENCES dbo.usuarios(id)
    );
END;

IF OBJECT_ID('dbo.anotacoes_usuario', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.anotacoes_usuario (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_anotacoes_usuario PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        titulo VARCHAR(120) NOT NULL,
        descricao VARCHAR(MAX) NULL,
        referencia VARCHAR(120) NULL,
        ativo BIT NOT NULL CONSTRAINT df_anotacoes_usuario_ativo DEFAULT (1),
        data_criacao DATETIME2 NOT NULL,
        data_atualizacao DATETIME2 NULL,
        CONSTRAINT fk_anotacoes_usuario FOREIGN KEY (usuario_id) REFERENCES dbo.usuarios(id)
    );
END;

IF OBJECT_ID('dbo.processos_internos', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.processos_internos (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_processos_internos PRIMARY KEY,
        codigo VARCHAR(80) NOT NULL CONSTRAINT uq_processos_internos_codigo UNIQUE,
        titulo VARCHAR(150) NOT NULL,
        descricao VARCHAR(MAX) NULL,
        categoria VARCHAR(80) NULL,
        rota_frontend VARCHAR(150) NULL,
        icone VARCHAR(80) NULL,
        ordem INT NOT NULL CONSTRAINT df_processos_internos_ordem DEFAULT (0),
        ativo BIT NOT NULL CONSTRAINT df_processos_internos_ativo DEFAULT (1)
    );
END;

IF OBJECT_ID('dbo.checklist_processo', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.checklist_processo (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_checklist_processo PRIMARY KEY,
        processo_id BIGINT NOT NULL,
        descricao VARCHAR(400) NOT NULL,
        ordem INT NOT NULL CONSTRAINT df_checklist_processo_ordem DEFAULT (0),
        obrigatorio BIT NOT NULL CONSTRAINT df_checklist_processo_obrigatorio DEFAULT (1),
        ativo BIT NOT NULL CONSTRAINT df_checklist_processo_ativo DEFAULT (1),
        CONSTRAINT fk_checklist_processo FOREIGN KEY (processo_id) REFERENCES dbo.processos_internos(id)
    );
END;

IF OBJECT_ID('dbo.usuario_checklist_processo', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.usuario_checklist_processo (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_usuario_checklist_processo PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        checklist_id BIGINT NOT NULL,
        concluido BIT NOT NULL CONSTRAINT df_usuario_checklist_concluido DEFAULT (0),
        data_conclusao DATETIME2 NULL,
        CONSTRAINT fk_usuario_checklist_usuario FOREIGN KEY (usuario_id) REFERENCES dbo.usuarios(id),
        CONSTRAINT fk_usuario_checklist_item FOREIGN KEY (checklist_id) REFERENCES dbo.checklist_processo(id),
        CONSTRAINT uk_usuario_checklist UNIQUE (usuario_id, checklist_id)
    );
END;

-- Índices (SQL Server)
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_favoritos_usuario' AND object_id = OBJECT_ID('dbo.favoritos_usuario'))
    CREATE INDEX ix_favoritos_usuario ON dbo.favoritos_usuario(usuario_id);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_sistemas_usuario' AND object_id = OBJECT_ID('dbo.sistemas_usuario'))
    CREATE INDEX ix_sistemas_usuario ON dbo.sistemas_usuario(usuario_id);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_anotacoes_usuario' AND object_id = OBJECT_ID('dbo.anotacoes_usuario'))
    CREATE INDEX ix_anotacoes_usuario ON dbo.anotacoes_usuario(usuario_id);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_processos_categoria' AND object_id = OBJECT_ID('dbo.processos_internos'))
    CREATE INDEX ix_processos_categoria ON dbo.processos_internos(categoria);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_checklist_processo' AND object_id = OBJECT_ID('dbo.checklist_processo'))
    CREATE INDEX ix_checklist_processo ON dbo.checklist_processo(processo_id);

-- Seeds (sem TRUE/FALSE)
INSERT INTO dbo.servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
SELECT 'ABERTURA_CONTA', 'Abertura de Conta', 'Orientações e pré-acessos para abertura de conta.', 'ATENDIMENTO', '/processos/abertura-conta', 'file-check', 10, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.servicos_internos WHERE codigo = 'ABERTURA_CONTA');

INSERT INTO dbo.servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
SELECT 'HABITACAO', 'Habitação', 'Acesso rápido aos fluxos e sistemas de habitação.', 'NEGOCIOS', '/servicos/habitacao', 'home', 20, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.servicos_internos WHERE codigo = 'HABITACAO');

INSERT INTO dbo.servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
SELECT 'COBRANCA', 'Cobrança', 'Atalhos de consulta e orientação para cobrança.', 'NEGOCIOS', '/servicos/cobranca', 'receipt', 30, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.servicos_internos WHERE codigo = 'COBRANCA');

INSERT INTO dbo.servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
SELECT 'JURIDICO', 'Jurídico', 'Consulta a orientações, normativos e fluxos jurídicos.', 'SUPORTE', '/servicos/juridico', 'scale', 40, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.servicos_internos WHERE codigo = 'JURIDICO');

INSERT INTO dbo.conteudos_internos (codigo, titulo, subtitulo, descricao, tipo, slug, ordem, ativo)
SELECT 'NORM_ABERTURA_CONTA', 'Normativo de Abertura de Conta', 'Consulta rápida', 'Regras operacionais e orientações resumidas para abertura de conta.', 'NORMATIVO', 'normativo-abertura-conta', 10, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.conteudos_internos WHERE codigo = 'NORM_ABERTURA_CONTA');

INSERT INTO dbo.conteudos_internos (codigo, titulo, subtitulo, descricao, tipo, slug, ordem, ativo)
SELECT 'NORM_COTACOES', 'Normativo de Cotações e Propostas', 'Agência', 'Orientações para acompanhamento de cotações, propostas e pendências da agência.', 'NORMATIVO', 'normativo-cotacoes-propostas', 20, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.conteudos_internos WHERE codigo = 'NORM_COTACOES');

INSERT INTO dbo.processos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, ativo)
SELECT 'ABERTURA_CONTA', 'Abertura de Conta - Orientações de Pré-Acesso',
       'Fluxo de apoio com documentação necessária, conferência e pontos de atenção antes de iniciar a abertura de conta.',
       'ATENDIMENTO', '/processos/abertura-conta', 'file-check', 1, 1
WHERE NOT EXISTS (SELECT 1 FROM dbo.processos_internos WHERE codigo = 'ABERTURA_CONTA');

INSERT INTO dbo.checklist_processo (processo_id, descricao, ordem, obrigatorio, ativo)
SELECT p.id, 'Conferir documento oficial com foto do cliente.', 1, 1, 1
FROM dbo.processos_internos p
WHERE p.codigo = 'ABERTURA_CONTA'
  AND NOT EXISTS (SELECT 1 FROM dbo.checklist_processo c WHERE c.processo_id = p.id AND c.ordem = 1);

INSERT INTO dbo.checklist_processo (processo_id, descricao, ordem, obrigatorio, ativo)
SELECT p.id, 'Validar CPF e dados cadastrais antes de prosseguir.', 2, 1, 1
FROM dbo.processos_internos p
WHERE p.codigo = 'ABERTURA_CONTA'
  AND NOT EXISTS (SELECT 1 FROM dbo.checklist_processo c WHERE c.processo_id = p.id AND c.ordem = 2);

INSERT INTO dbo.checklist_processo (processo_id, descricao, ordem, obrigatorio, ativo)
SELECT p.id, 'Confirmar comprovante de residência quando aplicável.', 3, 1, 1
FROM dbo.processos_internos p
WHERE p.codigo = 'ABERTURA_CONTA'
  AND NOT EXISTS (SELECT 1 FROM dbo.checklist_processo c WHERE c.processo_id = p.id AND c.ordem = 3);

INSERT INTO dbo.checklist_processo (processo_id, descricao, ordem, obrigatorio, ativo)
SELECT p.id, 'Selecionar o canal/sistema correto para abertura de conta.', 4, 1, 1
FROM dbo.processos_internos p
WHERE p.codigo = 'ABERTURA_CONTA'
  AND NOT EXISTS (SELECT 1 FROM dbo.checklist_processo c WHERE c.processo_id = p.id AND c.ordem = 4);

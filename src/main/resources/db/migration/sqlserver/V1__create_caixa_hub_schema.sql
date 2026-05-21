-- V1 - SQL Server

IF OBJECT_ID('dbo.usuarios', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.usuarios (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_usuarios PRIMARY KEY,
        matricula VARCHAR(30) NOT NULL CONSTRAINT uq_usuarios_matricula UNIQUE,
        nome_exibicao VARCHAR(120) NULL,
        senha_hash VARCHAR(128) NOT NULL,
        senha_salt VARCHAR(64) NOT NULL,
        ativo BIT NOT NULL CONSTRAINT df_usuarios_ativo DEFAULT (1),
        primeiro_acesso BIT NOT NULL CONSTRAINT df_usuarios_primeiro_acesso DEFAULT (1),
        data_criacao DATETIME2 NOT NULL,
        ultimo_acesso DATETIME2 NULL
    );
END;

IF OBJECT_ID('dbo.preferencias_usuario', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.preferencias_usuario (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_preferencias_usuario PRIMARY KEY,
        usuario_id BIGINT NOT NULL CONSTRAINT uq_preferencias_usuario_usuario UNIQUE,
        tema VARCHAR(30) NOT NULL CONSTRAINT df_preferencias_usuario_tema DEFAULT ('PADRAO'),
        menu_compacto BIT NOT NULL CONSTRAINT df_preferencias_usuario_menu_compacto DEFAULT (0),
        ultima_rota VARCHAR(120) NULL,
        atualizado_em DATETIME2 NOT NULL,
        CONSTRAINT fk_preferencias_usuario FOREIGN KEY (usuario_id) REFERENCES dbo.usuarios(id)
    );
END;

IF OBJECT_ID('dbo.servicos_internos', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.servicos_internos (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_servicos_internos PRIMARY KEY,
        codigo VARCHAR(50) NOT NULL CONSTRAINT uq_servicos_internos_codigo UNIQUE,
        titulo VARCHAR(120) NOT NULL,
        descricao VARCHAR(500) NULL,
        categoria VARCHAR(80) NOT NULL,
        rota_frontend VARCHAR(150) NULL,
        icone VARCHAR(80) NULL,
        ordem INT NOT NULL CONSTRAINT df_servicos_internos_ordem DEFAULT (0),
        destaque BIT NOT NULL CONSTRAINT df_servicos_internos_destaque DEFAULT (0),
        ativo BIT NOT NULL CONSTRAINT df_servicos_internos_ativo DEFAULT (1)
    );
END;

IF OBJECT_ID('dbo.conteudos_internos', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.conteudos_internos (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_conteudos_internos PRIMARY KEY,
        codigo VARCHAR(60) NOT NULL CONSTRAINT uq_conteudos_internos_codigo UNIQUE,
        titulo VARCHAR(150) NOT NULL,
        subtitulo VARCHAR(200) NULL,
        descricao VARCHAR(MAX) NULL,
        tipo VARCHAR(40) NOT NULL,
        slug VARCHAR(120) NULL,
        ordem INT NOT NULL CONSTRAINT df_conteudos_internos_ordem DEFAULT (0),
        ativo BIT NOT NULL CONSTRAINT df_conteudos_internos_ativo DEFAULT (1)
    );
END;

IF OBJECT_ID('dbo.acessos_servico', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.acessos_servico (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_acessos_servico PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        servico_id BIGINT NOT NULL,
        quantidade_acessos INT NOT NULL CONSTRAINT df_acessos_servico_qtd DEFAULT (0),
        ultimo_acesso DATETIME2 NOT NULL,
        CONSTRAINT fk_acessos_usuario FOREIGN KEY (usuario_id) REFERENCES dbo.usuarios(id),
        CONSTRAINT fk_acessos_servico FOREIGN KEY (servico_id) REFERENCES dbo.servicos_internos(id),
        CONSTRAINT uk_acesso_usuario_servico UNIQUE (usuario_id, servico_id)
    );
END;

IF OBJECT_ID('dbo.auditoria_acessos', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.auditoria_acessos (
        id BIGINT IDENTITY(1,1) NOT NULL CONSTRAINT pk_auditoria_acessos PRIMARY KEY,
        usuario_id BIGINT NULL,
        matricula VARCHAR(30) NULL,
        acao VARCHAR(50) NOT NULL,
        referencia VARCHAR(120) NULL,
        descricao VARCHAR(500) NULL,
        criado_em DATETIME2 NOT NULL
    );
END;

-- Índices (SQL Server)
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_usuarios_matricula' AND object_id = OBJECT_ID('dbo.usuarios'))
    CREATE INDEX ix_usuarios_matricula ON dbo.usuarios(matricula);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_servicos_categoria' AND object_id = OBJECT_ID('dbo.servicos_internos'))
    CREATE INDEX ix_servicos_categoria ON dbo.servicos_internos(categoria);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_conteudos_tipo' AND object_id = OBJECT_ID('dbo.conteudos_internos'))
    CREATE INDEX ix_conteudos_tipo ON dbo.conteudos_internos(tipo);

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_auditoria_usuario' AND object_id = OBJECT_ID('dbo.auditoria_acessos'))
    CREATE INDEX ix_auditoria_usuario ON dbo.auditoria_acessos(usuario_id);
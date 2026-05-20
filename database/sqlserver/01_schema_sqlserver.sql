/*
 Etapa 19 - Schema base SQL Server idempotente.
 Pode ser executado mais de uma vez sem apagar dados.
*/

IF OBJECT_ID('usuarios', 'U') IS NULL
BEGIN
    CREATE TABLE usuarios (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        matricula VARCHAR(30) NOT NULL,
        nome_exibicao VARCHAR(120) NULL,
        senha_hash VARCHAR(128) NOT NULL,
        senha_salt VARCHAR(64) NOT NULL,
        ativo BIT NOT NULL CONSTRAINT df_usuarios_ativo DEFAULT 1,
        primeiro_acesso BIT NOT NULL CONSTRAINT df_usuarios_primeiro_acesso DEFAULT 1,
        data_criacao DATETIME2 NOT NULL,
        ultimo_acesso DATETIME2 NULL,
        CONSTRAINT uk_usuarios_matricula UNIQUE (matricula)
    );
END;
GO

IF OBJECT_ID('preferencias_usuario', 'U') IS NULL
BEGIN
    CREATE TABLE preferencias_usuario (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        tema VARCHAR(30) NOT NULL CONSTRAINT df_preferencias_tema DEFAULT 'PADRAO',
        menu_compacto BIT NOT NULL CONSTRAINT df_preferencias_menu_compacto DEFAULT 0,
        ultima_rota VARCHAR(120) NULL,
        atualizado_em DATETIME2 NOT NULL,
        CONSTRAINT fk_preferencias_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
        CONSTRAINT uk_preferencias_usuario UNIQUE (usuario_id)
    );
END;
GO

IF OBJECT_ID('servicos_internos', 'U') IS NULL
BEGIN
    CREATE TABLE servicos_internos (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        codigo VARCHAR(50) NOT NULL,
        titulo VARCHAR(120) NOT NULL,
        descricao VARCHAR(500) NULL,
        categoria VARCHAR(80) NOT NULL,
        rota_frontend VARCHAR(150) NULL,
        icone VARCHAR(80) NULL,
        ordem INT NOT NULL CONSTRAINT df_servicos_ordem DEFAULT 0,
        destaque BIT NOT NULL CONSTRAINT df_servicos_destaque DEFAULT 0,
        ativo BIT NOT NULL CONSTRAINT df_servicos_ativo DEFAULT 1,
        CONSTRAINT uk_servicos_codigo UNIQUE (codigo)
    );
END;
GO

IF OBJECT_ID('conteudos_internos', 'U') IS NULL
BEGIN
    CREATE TABLE conteudos_internos (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        codigo VARCHAR(60) NOT NULL,
        titulo VARCHAR(150) NOT NULL,
        subtitulo VARCHAR(200) NULL,
        descricao VARCHAR(MAX) NULL,
        tipo VARCHAR(40) NOT NULL,
        slug VARCHAR(120) NULL,
        ordem INT NOT NULL CONSTRAINT df_conteudos_ordem DEFAULT 0,
        ativo BIT NOT NULL CONSTRAINT df_conteudos_ativo DEFAULT 1,
        CONSTRAINT uk_conteudos_codigo UNIQUE (codigo)
    );
END;
GO

IF OBJECT_ID('acessos_servico', 'U') IS NULL
BEGIN
    CREATE TABLE acessos_servico (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        servico_id BIGINT NOT NULL,
        quantidade_acessos INT NOT NULL CONSTRAINT df_acessos_quantidade DEFAULT 0,
        ultimo_acesso DATETIME2 NOT NULL,
        CONSTRAINT fk_acessos_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
        CONSTRAINT fk_acessos_servico FOREIGN KEY (servico_id) REFERENCES servicos_internos(id),
        CONSTRAINT uk_acesso_usuario_servico UNIQUE (usuario_id, servico_id)
    );
END;
GO

IF OBJECT_ID('auditoria_acessos', 'U') IS NULL
BEGIN
    CREATE TABLE auditoria_acessos (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        usuario_id BIGINT NULL,
        matricula VARCHAR(30) NULL,
        acao VARCHAR(50) NOT NULL,
        referencia VARCHAR(120) NULL,
        descricao VARCHAR(500) NULL,
        criado_em DATETIME2 NOT NULL
    );
END;
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_usuarios_matricula' AND object_id = OBJECT_ID('usuarios'))
    CREATE INDEX ix_usuarios_matricula ON usuarios(matricula);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_servicos_categoria' AND object_id = OBJECT_ID('servicos_internos'))
    CREATE INDEX ix_servicos_categoria ON servicos_internos(categoria);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_conteudos_tipo' AND object_id = OBJECT_ID('conteudos_internos'))
    CREATE INDEX ix_conteudos_tipo ON conteudos_internos(tipo);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_auditoria_usuario' AND object_id = OBJECT_ID('auditoria_acessos'))
    CREATE INDEX ix_auditoria_usuario ON auditoria_acessos(usuario_id);
GO

/*
 Etapa 19 - Adequação Figma/Portal SQL Server idempotente.
 Pode ser executado mais de uma vez sem apagar dados.
*/

IF OBJECT_ID('favoritos_usuario', 'U') IS NULL
BEGIN
    CREATE TABLE favoritos_usuario (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        servico_id BIGINT NOT NULL,
        titulo_customizado VARCHAR(120) NULL,
        observacao VARCHAR(500) NULL,
        ativo BIT NOT NULL CONSTRAINT df_favoritos_ativo DEFAULT 1,
        data_criacao DATETIME2 NOT NULL,
        CONSTRAINT fk_favoritos_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
        CONSTRAINT fk_favoritos_servico FOREIGN KEY (servico_id) REFERENCES servicos_internos(id),
        CONSTRAINT uk_favorito_usuario_servico UNIQUE (usuario_id, servico_id)
    );
END;
GO

IF OBJECT_ID('sistemas_usuario', 'U') IS NULL
BEGIN
    CREATE TABLE sistemas_usuario (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        nome VARCHAR(120) NOT NULL,
        descricao VARCHAR(500) NULL,
        url VARCHAR(300) NULL,
        categoria VARCHAR(80) NULL,
        icone VARCHAR(80) NULL,
        favorito BIT NOT NULL CONSTRAINT df_sistemas_favorito DEFAULT 0,
        ativo BIT NOT NULL CONSTRAINT df_sistemas_ativo DEFAULT 1,
        data_criacao DATETIME2 NOT NULL,
        data_atualizacao DATETIME2 NULL,
        CONSTRAINT fk_sistemas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
    );
END;
GO

IF OBJECT_ID('anotacoes_usuario', 'U') IS NULL
BEGIN
    CREATE TABLE anotacoes_usuario (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        titulo VARCHAR(120) NOT NULL,
        descricao VARCHAR(MAX) NULL,
        referencia VARCHAR(120) NULL,
        ativo BIT NOT NULL CONSTRAINT df_anotacoes_ativo DEFAULT 1,
        data_criacao DATETIME2 NOT NULL,
        data_atualizacao DATETIME2 NULL,
        CONSTRAINT fk_anotacoes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
    );
END;
GO

IF OBJECT_ID('processos_internos', 'U') IS NULL
BEGIN
    CREATE TABLE processos_internos (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        codigo VARCHAR(80) NOT NULL,
        titulo VARCHAR(150) NOT NULL,
        descricao VARCHAR(MAX) NULL,
        categoria VARCHAR(80) NULL,
        rota_frontend VARCHAR(150) NULL,
        icone VARCHAR(80) NULL,
        ordem INT NOT NULL CONSTRAINT df_processos_ordem DEFAULT 0,
        ativo BIT NOT NULL CONSTRAINT df_processos_ativo DEFAULT 1,
        CONSTRAINT uk_processos_codigo UNIQUE (codigo)
    );
END;
GO

IF OBJECT_ID('checklist_processo', 'U') IS NULL
BEGIN
    CREATE TABLE checklist_processo (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        processo_id BIGINT NOT NULL,
        descricao VARCHAR(400) NOT NULL,
        ordem INT NOT NULL CONSTRAINT df_checklist_ordem DEFAULT 0,
        obrigatorio BIT NOT NULL CONSTRAINT df_checklist_obrigatorio DEFAULT 1,
        ativo BIT NOT NULL CONSTRAINT df_checklist_ativo DEFAULT 1,
        CONSTRAINT fk_checklist_processo FOREIGN KEY (processo_id) REFERENCES processos_internos(id)
    );
END;
GO

IF OBJECT_ID('usuario_checklist_processo', 'U') IS NULL
BEGIN
    CREATE TABLE usuario_checklist_processo (
        id BIGINT IDENTITY(1,1) PRIMARY KEY,
        usuario_id BIGINT NOT NULL,
        checklist_id BIGINT NOT NULL,
        concluido BIT NOT NULL CONSTRAINT df_usuario_checklist_concluido DEFAULT 0,
        data_conclusao DATETIME2 NULL,
        CONSTRAINT fk_usuario_checklist_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
        CONSTRAINT fk_usuario_checklist_item FOREIGN KEY (checklist_id) REFERENCES checklist_processo(id),
        CONSTRAINT uk_usuario_checklist UNIQUE (usuario_id, checklist_id)
    );
END;
GO

IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_favoritos_usuario' AND object_id = OBJECT_ID('favoritos_usuario'))
    CREATE INDEX ix_favoritos_usuario ON favoritos_usuario(usuario_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_sistemas_usuario' AND object_id = OBJECT_ID('sistemas_usuario'))
    CREATE INDEX ix_sistemas_usuario ON sistemas_usuario(usuario_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_anotacoes_usuario' AND object_id = OBJECT_ID('anotacoes_usuario'))
    CREATE INDEX ix_anotacoes_usuario ON anotacoes_usuario(usuario_id);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_processos_categoria' AND object_id = OBJECT_ID('processos_internos'))
    CREATE INDEX ix_processos_categoria ON processos_internos(categoria);
GO
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name = 'ix_checklist_processo' AND object_id = OBJECT_ID('checklist_processo'))
    CREATE INDEX ix_checklist_processo ON checklist_processo(processo_id);
GO

IF NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'ABERTURA_CONTA')
BEGIN
    INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
    VALUES ('ABERTURA_CONTA', 'Abertura de Conta', 'Orientações e pré-acessos para abertura de conta.', 'ATENDIMENTO', '/processos/abertura-conta', 'file-check', 10, 1, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'HABITACAO')
BEGIN
    INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
    VALUES ('HABITACAO', 'Habitação', 'Acesso rápido aos fluxos e sistemas de habitação.', 'NEGOCIOS', '/servicos/habitacao', 'home', 20, 1, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'COBRANCA')
BEGIN
    INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
    VALUES ('COBRANCA', 'Cobrança', 'Atalhos de consulta e orientação para cobrança.', 'NEGOCIOS', '/servicos/cobranca', 'receipt', 30, 1, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'JURIDICO')
BEGIN
    INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
    VALUES ('JURIDICO', 'Jurídico', 'Consulta a orientações, normativos e fluxos jurídicos.', 'SUPORTE', '/servicos/juridico', 'scale', 40, 1, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM conteudos_internos WHERE codigo = 'NORM_ABERTURA_CONTA')
BEGIN
    INSERT INTO conteudos_internos (codigo, titulo, subtitulo, descricao, tipo, slug, ordem, ativo)
    VALUES ('NORM_ABERTURA_CONTA', 'Normativo de Abertura de Conta', 'Consulta rápida', 'Regras operacionais e orientações resumidas para abertura de conta.', 'NORMATIVO', 'normativo-abertura-conta', 10, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM conteudos_internos WHERE codigo = 'NORM_COTACOES')
BEGIN
    INSERT INTO conteudos_internos (codigo, titulo, subtitulo, descricao, tipo, slug, ordem, ativo)
    VALUES ('NORM_COTACOES', 'Normativo de Cotações e Propostas', 'Agência', 'Orientações para acompanhamento de cotações, propostas e pendências da agência.', 'NORMATIVO', 'normativo-cotacoes-propostas', 20, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM processos_internos WHERE codigo = 'ABERTURA_CONTA')
BEGIN
    INSERT INTO processos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, ativo)
    VALUES ('ABERTURA_CONTA', 'Abertura de Conta - Orientações de Pré-Acesso', 'Fluxo de apoio com documentação necessária, conferência e pontos de atenção antes de iniciar a abertura de conta.', 'ATENDIMENTO', '/processos/abertura-conta', 'file-check', 1, 1);
END;
GO

DECLARE @processoAbertura BIGINT = (SELECT id FROM processos_internos WHERE codigo = 'ABERTURA_CONTA');

IF @processoAbertura IS NOT NULL AND NOT EXISTS (SELECT 1 FROM checklist_processo WHERE processo_id = @processoAbertura AND ordem = 1)
    INSERT INTO checklist_processo (processo_id, descricao, ordem, obrigatorio, ativo) VALUES (@processoAbertura, 'Conferir documento oficial com foto do cliente.', 1, 1, 1);

IF @processoAbertura IS NOT NULL AND NOT EXISTS (SELECT 1 FROM checklist_processo WHERE processo_id = @processoAbertura AND ordem = 2)
    INSERT INTO checklist_processo (processo_id, descricao, ordem, obrigatorio, ativo) VALUES (@processoAbertura, 'Validar CPF e dados cadastrais antes de prosseguir.', 2, 1, 1);

IF @processoAbertura IS NOT NULL AND NOT EXISTS (SELECT 1 FROM checklist_processo WHERE processo_id = @processoAbertura AND ordem = 3)
    INSERT INTO checklist_processo (processo_id, descricao, ordem, obrigatorio, ativo) VALUES (@processoAbertura, 'Confirmar comprovante de residência quando aplicável.', 3, 1, 1);

IF @processoAbertura IS NOT NULL AND NOT EXISTS (SELECT 1 FROM checklist_processo WHERE processo_id = @processoAbertura AND ordem = 4)
    INSERT INTO checklist_processo (processo_id, descricao, ordem, obrigatorio, ativo) VALUES (@processoAbertura, 'Selecionar o canal/sistema correto para abertura de conta.', 4, 1, 1);
GO

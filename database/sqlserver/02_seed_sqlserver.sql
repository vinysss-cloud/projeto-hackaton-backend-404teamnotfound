/*
 Etapa 19 - Carga base SQL Server idempotente.
 Pode ser executada mais de uma vez sem duplicar registros.
*/

IF NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'ACESSOS')
BEGIN
    INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
    VALUES ('ACESSOS', 'Acessos internos', 'Consulte e organize atalhos de acesso aos sistemas internos.', 'ACESSOS', '/servicos/acessos', 'key', 1, 1, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'CATALOGO')
BEGIN
    INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
    VALUES ('CATALOGO', 'Catálogo de serviços', 'Lista centralizada de serviços disponíveis para consulta e navegação.', 'SERVICOS', '/servicos/catalogo', 'grid', 2, 1, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'CONTEUDO_INTERNO')
BEGIN
    INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
    VALUES ('CONTEUDO_INTERNO', 'Conteúdo interno', 'Área de comunicados, orientações e informações institucionais.', 'CONTEUDO', '/conteudo', 'file-text', 3, 1, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'PERFIL')
BEGIN
    INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
    VALUES ('PERFIL', 'Meu perfil', 'Preferências básicas do usuário e identificação por matrícula.', 'USUARIO', '/perfil', 'user', 4, 0, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM conteudos_internos WHERE codigo = 'BOAS_VINDAS')
BEGIN
    INSERT INTO conteudos_internos (codigo, titulo, subtitulo, descricao, tipo, slug, ordem, ativo)
    VALUES ('BOAS_VINDAS', 'Bem-vindo ao CAIXA HUB', 'Central de Acessos e Serviços Internos', 'Acesse os principais serviços internos em uma experiência simples, objetiva e padronizada.', 'BANNER', 'boas-vindas', 1, 1);
END;
GO

IF NOT EXISTS (SELECT 1 FROM conteudos_internos WHERE codigo = 'INTERNO_CAIXA')
BEGIN
    INSERT INTO conteudos_internos (codigo, titulo, subtitulo, descricao, tipo, slug, ordem, ativo)
    VALUES ('INTERNO_CAIXA', '#INTERNO.CAIXA', 'Conteúdo institucional', 'Ambiente voltado à organização de acessos, conteúdo interno e serviços úteis para o dia a dia.', 'CARD', 'interno-caixa', 2, 1);
END;
GO

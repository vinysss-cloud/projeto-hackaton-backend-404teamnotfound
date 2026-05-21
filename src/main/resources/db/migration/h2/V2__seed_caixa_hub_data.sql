-- V2 - Carga inicial compatível com H2

INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
SELECT 'ACESSOS', 'Acessos internos', 'Consulte e organize atalhos de acesso aos sistemas internos.', 'ACESSOS', '/servicos/acessos', 'key', 1, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'ACESSOS');

INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
SELECT 'CATALOGO', 'Catálogo de serviços', 'Lista centralizada de serviços disponíveis para consulta e navegação.', 'SERVICOS', '/servicos/catalogo', 'grid', 2, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'CATALOGO');

INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
SELECT 'CONTEUDO_INTERNO', 'Conteúdo interno', 'Área de comunicados, orientações e informações institucionais.', 'CONTEUDO', '/conteudo', 'file-text', 3, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'CONTEUDO_INTERNO');

INSERT INTO servicos_internos (codigo, titulo, descricao, categoria, rota_frontend, icone, ordem, destaque, ativo)
SELECT 'PERFIL', 'Meu perfil', 'Preferências básicas do usuário e identificação por matrícula.', 'USUARIO', '/perfil', 'user', 4, FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM servicos_internos WHERE codigo = 'PERFIL');

INSERT INTO conteudos_internos (codigo, titulo, subtitulo, descricao, tipo, slug, ordem, ativo)
SELECT 'BOAS_VINDAS', 'Bem-vindo ao CAIXA HUB', 'Central de Acessos e Serviços Internos',
       'Acesse os principais serviços internos em uma experiência simples, objetiva e padronizada.', 'BANNER', 'boas-vindas', 1, TRUE
WHERE NOT EXISTS (SELECT 1 FROM conteudos_internos WHERE codigo = 'BOAS_VINDAS');

INSERT INTO conteudos_internos (codigo, titulo, subtitulo, descricao, tipo, slug, ordem, ativo)
SELECT 'INTERNO_CAIXA', '#INTERNO.CAIXA', 'Conteúdo institucional',
       'Ambiente voltado à organização de acessos, conteúdo interno e serviços úteis para o dia a dia.', 'CARD', 'interno-caixa', 2, TRUE
WHERE NOT EXISTS (SELECT 1 FROM conteudos_internos WHERE codigo = 'INTERNO_CAIXA');

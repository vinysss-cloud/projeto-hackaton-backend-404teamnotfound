# Avaliação do backend CAIXA HUB

## Veredito

O projeto está correto como backend separado do frontend. Não há Angular, React, HTML ou CSS no repositório. O escopo atual atende principalmente às telas de login e portal inicial disponíveis no protótipo enviado.

## Funcionalidades atendidas

- Login por matrícula e senha.
- Auto-cadastro no primeiro acesso.
- Validação da senha no segundo acesso em diante.
- Persistência do usuário para personalização da próxima tela.
- Retorno de dados para header, menu, conteúdos e serviços.
- Registro de acesso a serviços.
- Preferências simples do usuário.
- Auditoria simples.
- Criação das tabelas por script.

## Banco de dados

O ambiente padrão usa H2 persistente com usuário e senha hardcoded:

```properties
quarkus.datasource.username=sa
quarkus.datasource.password=sa
```

O perfil SQL Server também está com valores hardcoded, sem variáveis de ambiente:

```properties
%sqlserver.quarkus.datasource.username=sa
%sqlserver.quarkus.datasource.password=SuaSenha@123
%sqlserver.quarkus.datasource.jdbc.url=jdbc:sqlserver://localhost:1433;databaseName=caixa_hub;encrypt=false;trustServerCertificate=true
```

Para SQL Server, execute manualmente:

```txt
database/sqlserver/01_schema_sqlserver.sql
database/sqlserver/02_seed_sqlserver.sql
```

## Ajustes aplicados nesta revisão

- Removida dependência de variáveis de ambiente no perfil SQL Server.
- Adicionado `src/test/resources/application.properties` com H2 em memória para testes.
- Corrigido ponto transacional na criação de preferências do usuário.
- Adicionado mapper global de erro para retornar JSON amigável ao frontend.

## Limitação da avaliação

O ZIP do protótipo contém apenas duas imagens de login. Com base nelas, o backend atende a autenticação e criação/recuperação do usuário. Para validar 100% das demais telas, é necessário anexar as telas restantes do Figma ou listar os campos/ações esperados em cada uma.

# CAIXA HUB Backend

Backend Quarkus para atender o protótipo CAIXA HUB / SICAS, mantendo o frontend em outro repositório.

## Introdução

O CAIXA HUB Backend é um sistema desenvolvido para fornecer APIs REST que suportam funcionalidades essenciais para o protótipo CAIXA HUB. Ele é projetado para ser escalável, seguro e eficiente, atendendo às necessidades do frontend hospedado em outro repositório.

## Tecnologias Utilizadas

- **Quarkus**: Framework Java para desenvolvimento de microsserviços.
- **H2 Database**: Banco de dados em memória para desenvolvimento e testes.
- **SQL Server**: Banco de dados relacional para ambientes de produção.
- **Flyway**: Gerenciamento de migrações de banco de dados.
- **Docker**: Contêineres para facilitar o desenvolvimento e implantação.
- **Swagger**: Documentação interativa para APIs REST.

## Funcionalidades do Sistema

- Login com auto-cadastro e validação de senha.
- Gerenciamento de usuários e preferências.
- Portal inicial consolidado com header, menu e serviços.
- Favoritos, sistemas e acessos recentes do usuário.
- Normativos, anotações e processos internos.
- Busca orientada e gerenciamento de cotações/propostas.
- Auditoria automática dos fluxos principais.
- Criação e gerenciamento de tabelas via scripts SQL/Flyway.

## Estrutura do Projeto

- **src/main/java**: Código-fonte principal do backend.
- **src/main/resources**: Arquivos de configuração e scripts de migração do banco de dados.
- **database/sqlserver**: Scripts SQL específicos para o SQL Server.
- **docs**: Documentação detalhada do projeto.
- **scripts**: Scripts auxiliares para automação de tarefas.

## Como Rodar Localmente

```bash
./mvnw quarkus:dev
```

API base:

```txt
http://localhost:8080/api
```

Swagger:

```txt
http://localhost:8080/api/q/swagger-ui
```

## Configuração do Banco de Dados

### H2 (Padrão)

Banco de dados persistente em arquivo:

```properties
jdbc:h2:file:./data/caixa-hub
```

### SQL Server

Configuração no `application.properties`:

```properties
%sqlserver.quarkus.datasource.username=sa
%sqlserver.quarkus.datasource.password=SuaSenha@123
%sqlserver.quarkus.datasource.jdbc.url=jdbc:sqlserver://localhost:1433;databaseName=caixa_hub;encrypt=false;trustServerCertificate=true
```

Scripts necessários:

```txt
database/sqlserver/01_schema_sqlserver.sql
database/sqlserver/02_seed_sqlserver.sql
database/sqlserver/03_adequacao_figma_portal.sql
database/sqlserver/04_cotacoes_propostas_sqlserver.sql
database/sqlserver/05_fix_backend_consistency.sql
```

## Endpoints Principais

- **Login**: `POST /api/auth/login`
- **Portal Inicial**: `GET /api/portal/inicial/{usuarioId}`
- **Favoritos**: `GET /api/favoritos/usuario/{usuarioId}`
- **Sistemas do Usuário**: `GET /api/sistemas/usuario/{usuarioId}`
- **Cotações/Propostas**: `GET /api/cotacoes-propostas`
- **Auditoria**: `GET /api/auditoria/usuario/{usuarioId}`

Para mais detalhes, consulte a seção "Fluxo principal".

## Observação de Segurança

Para o MVP, a senha é salva com hash SHA-256 + salt. Para produção, recomenda-se evoluir para BCrypt/Argon2, JWT/sessão, controle de tentativas e integração com provedor oficial de identidade.

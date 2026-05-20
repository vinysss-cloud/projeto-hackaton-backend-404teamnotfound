# CAIXA HUB Backend

Backend Quarkus para atender o protótipo CAIXA HUB / SICAS, mantendo o frontend em outro repositório.

## Escopo

Este projeto é **somente backend**. Ele não contém Angular, React, HTML, CSS ou telas.

O backend entrega APIs REST para o sistema frontend consumir:

- login por matrícula com auto-cadastro no primeiro acesso;
- validação de senha a partir do segundo acesso;
- usuário e preferências básicas;
- portal inicial consolidado;
- header e menu;
- serviços internos;
- favoritos do usuário;
- sistemas do usuário;
- acessos recentes;
- normativos;
- anotações;
- processos internos e checklist;
- busca orientada simples;
- cotações/propostas da agência;
- comentários e histórico de propostas;
- auditoria automática dos fluxos principais;
- criação de tabelas por scripts SQL/Flyway.

Não há regra de gamificação, badges, ranking, desafios ou endpoints específicos de Angular.

## Rodar localmente

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

## Banco local

Por padrão usa H2 persistente em arquivo:

```properties
jdbc:h2:file:./data/caixa-hub
```

As tabelas são criadas por Flyway:

```txt
src/main/resources/db/migration/V1__create_caixa_hub_schema.sql
src/main/resources/db/migration/V2__seed_caixa_hub_data.sql
src/main/resources/db/migration/V3__adequacao_figma_portal.sql
src/main/resources/db/migration/V4__create_cotacoes_propostas_schema.sql
src/main/resources/db/migration/V5__fix_backend_consistency.sql
```

## SQL Server

O perfil SQL Server está hardcoded no `application.properties` para facilitar o ambiente de demonstração:

```properties
%sqlserver.quarkus.datasource.username=sa
%sqlserver.quarkus.datasource.password=SuaSenha@123
%sqlserver.quarkus.datasource.jdbc.url=jdbc:sqlserver://localhost:1433;databaseName=caixa_hub;encrypt=false;trustServerCertificate=true
```

Ajuste usuário, senha, host e banco conforme o ambiente usado.

Para subir com SQL Server:

```bash
./mvnw quarkus:dev -Dquarkus.profile=sqlserver
```

Antes de subir nesse perfil, execute os scripts:

```txt
database/sqlserver/01_schema_sqlserver.sql
database/sqlserver/02_seed_sqlserver.sql
database/sqlserver/03_adequacao_figma_portal.sql
database/sqlserver/04_cotacoes_propostas_sqlserver.sql
database/sqlserver/05_fix_backend_consistency.sql
```

## Fluxo principal

### Login / auto-cadastro

```http
POST /api/auth/login
```

```json
{
  "matricula": "C000000",
  "senha": "1234",
  "nomeExibicao": "Usuário Teste"
}
```

Regra:

- se a matrícula não existir, cria o usuário;
- se a matrícula existir, valida a senha;
- se a senha estiver correta, retorna o usuário;
- se a senha estiver errada, retorna erro de autenticação.

### Portal inicial

```http
GET /api/portal/inicial/{usuarioId}
```

Retorna dados consolidados para o frontend montar a área inicial:

- header;
- usuário;
- menu;
- serviços;
- conteúdos;
- favoritos;
- sistemas;
- acessos recentes;
- anotações;
- normativos;
- processos;
- resumo de cotações/propostas;
- cotações/propostas recentes.

### Serviços

```http
GET  /api/servicos
GET  /api/servicos/{id}
POST /api/servicos/{id}/acessar?usuarioId=1
```

### Favoritos

```http
GET    /api/favoritos/usuario/{usuarioId}
POST   /api/favoritos
PUT    /api/favoritos/{id}
DELETE /api/favoritos/{id}
```

### Sistemas do usuário

```http
GET    /api/sistemas/usuario/{usuarioId}
POST   /api/sistemas
PUT    /api/sistemas/{id}
POST   /api/sistemas/{id}/acessar
DELETE /api/sistemas/{id}
```

### Acessos recentes

```http
GET /api/acessos-recentes/usuario/{usuarioId}?limite=8
```

### Normativos

```http
GET /api/normativos
GET /api/normativos/{id}
```

### Anotações

```http
GET    /api/anotacoes/usuario/{usuarioId}
POST   /api/anotacoes
PUT    /api/anotacoes/{id}
DELETE /api/anotacoes/{id}
```

### Processos/checklist

```http
GET  /api/processos
GET  /api/processos/{id}?usuarioId=1
POST /api/processos/checklist/{checklistId}/marcar
```

### Busca orientada

```http
GET /api/assistente/buscar?q=abertura&usuarioId=1
```

### Cotações/propostas

```http
GET   /api/cotacoes-propostas
GET   /api/cotacoes-propostas/resumo
GET   /api/cotacoes-propostas/recentes?limite=6
GET   /api/cotacoes-propostas/{id}
POST  /api/cotacoes-propostas
PUT   /api/cotacoes-propostas/{id}
PATCH /api/cotacoes-propostas/{id}/status
POST  /api/cotacoes-propostas/{id}/comentarios
GET   /api/cotacoes-propostas/{id}/comentarios
GET   /api/cotacoes-propostas/{id}/historico
```

### Auditoria

```http
GET /api/auditoria/usuario/{usuarioId}
GET /api/auditoria/usuario/{usuarioId}/resumo
GET /api/auditoria/recentes
```

## Resposta padrão

Sucesso:

```json
{
  "sucesso": true,
  "mensagem": "Operação realizada com sucesso.",
  "dados": {},
  "timestamp": "2026-05-20T10:30:00"
}
```

Erro:

```json
{
  "sucesso": false,
  "status": 400,
  "codigo": "REQUISICAO_INVALIDA",
  "mensagem": "Existem campos inválidos na requisição.",
  "erros": [],
  "timestamp": "2026-05-20T10:30:00"
}
```

## Tabelas

- `usuarios`
- `preferencias_usuario`
- `servicos_internos`
- `conteudos_internos`
- `acessos_servico`
- `auditoria_acessos`
- `favoritos_usuario`
- `sistemas_usuario`
- `anotacoes_usuario`
- `processos_internos`
- `checklist_processo`
- `usuario_checklist_processo`
- `cotacoes_propostas`
- `historico_proposta`
- `comentarios_proposta`

## Observação de segurança

Para o MVP, a senha é salva com hash SHA-256 + salt. Para produção, recomenda-se evoluir para BCrypt/Argon2, JWT/sessão, controle de tentativas e integração com provedor oficial de identidade.

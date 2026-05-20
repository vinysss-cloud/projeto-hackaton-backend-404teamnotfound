# Etapa 20 — Segurança, padronização, logs e monitoramento

Esta etapa mantém o projeto como **somente backend** e aplica melhorias para integração com frontend externo, segurança e suporte operacional.

## 1. Senhas com BCrypt

O `PasswordService` foi trocado de SHA-256 + salt manual para BCrypt.

Novo padrão:

- BCrypt com custo 12;
- hash salvo em `usuarios.senha_hash`;
- `usuarios.senha_salt` permanece preenchido como `BCRYPT` para compatibilidade com a tabela atual;
- hashes legados SHA-256 ainda são aceitos temporariamente;
- após login bem-sucedido com hash legado, a senha é regravada automaticamente em BCrypt.

Isso evita quebrar usuários já criados no banco local durante as etapas anteriores.

## 2. Respostas padronizadas

As respostas de sucesso seguem o padrão já criado nas etapas anteriores:

```json
{
  "sucesso": true,
  "mensagem": "Operação realizada com sucesso.",
  "dados": {},
  "timestamp": "2026-05-20T10:30:00"
}
```

## 3. Erros padronizados com path

Os erros agora incluem `path`, facilitando tratamento pelo frontend e análise de suporte.

Exemplo:

```json
{
  "sucesso": false,
  "status": 400,
  "codigo": "REQUISICAO_INVALIDA",
  "mensagem": "Existem campos inválidos na requisição.",
  "path": "/auth/login",
  "erros": [
    {
      "campo": "matricula",
      "mensagem": "Matrícula é obrigatória."
    }
  ],
  "timestamp": "2026-05-20T10:30:00"
}
```

## 4. Logs estruturados

Foi criado o `LoggingFilter`, que registra início e fim das requisições HTTP.

Campos registrados:

- `event=http_request_start`;
- `event=http_request_end`;
- `requestId`;
- método HTTP;
- path;
- status;
- duração em milissegundos;
- user-agent.

Também é enviado o header:

```http
X-Request-Id
```

Se o frontend enviar esse header, o backend reaproveita o valor. Caso contrário, o backend gera um UUID.

## 5. Logs estruturados de erro

O `ApiExceptionMapper` agora registra erros com informações estruturadas:

```txt
event=api_error status=400 codigo=REQUISICAO_INVALIDA path=/auth/login mensagem="..." exception=ConstraintViolationException
```

Erros 5xx são registrados como `error`; erros 4xx são registrados como `warn`.

## 6. Monitoramento

Novo endpoint:

```http
GET /api/monitoramento/status
```

Retorna:

- status geral da aplicação;
- perfil ativo;
- tipo de banco;
- disponibilidade do banco;
- informações básicas da JVM.

Exemplo:

```json
{
  "sucesso": true,
  "mensagem": "Status de monitoramento retornado com sucesso.",
  "dados": {
    "status": "UP",
    "aplicacao": "caixa-hub-backend",
    "ambiente": "dev",
    "banco": "h2",
    "bancoDisponivel": true,
    "timestamp": "2026-05-20T10:30:00",
    "detalhes": {
      "javaVersion": "17",
      "availableProcessors": 8,
      "freeMemoryMb": 120,
      "totalMemoryMb": 256
    }
  },
  "timestamp": "2026-05-20T10:30:00"
}
```

## Arquivos alterados/criados

```txt
pom.xml
src/main/resources/application.properties
src/main/java/br/caixa/gov/hackathon/service/PasswordService.java
src/main/java/br/caixa/gov/hackathon/service/AuthService.java
src/main/java/br/caixa/gov/hackathon/dto/ApiErroResponse.java
src/main/java/br/caixa/gov/hackathon/exception/ApiExceptionMapper.java
src/main/java/br/caixa/gov/hackathon/filter/LoggingFilter.java
src/main/java/br/caixa/gov/hackathon/dto/MonitoramentoDTOs.java
src/main/java/br/caixa/gov/hackathon/resource/MonitoramentoResource.java
docs/backend-etapa-20.md
```

## Validação rápida

```bash
./mvnw clean test
./mvnw quarkus:dev
```

Testes manuais:

```http
GET  /api/health
GET  /api/monitoramento/status
POST /api/auth/login
```

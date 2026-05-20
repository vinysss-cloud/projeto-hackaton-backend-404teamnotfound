# Etapa 18 — Revisão final de consistência

Esta etapa revisa o projeto atual para deixá-lo mais seguro para integração com o frontend externo e com o banco.

## Ajustes realizados

### 1. Correção do path base da API

O projeto tinha:

```properties
quarkus.http.root-path=/api
```

E também:

```java
@ApplicationPath("/api")
```

Isso poderia gerar rotas duplicadas como:

```txt
/api/api/health
```

Foi ajustado para:

```java
@ApplicationPath("/")
```

Assim a API pública permanece:

```txt
/api/health
/api/auth/login
/api/portal/inicial/{usuarioId}
```

### 2. Consistência entre DTO, Entity e Banco

A API aceitava valores maiores do que algumas colunas permitiam em `cotacoes_propostas`.

Foram alinhados:

```txt
produto:    VARCHAR(120)
modalidade: VARCHAR(120)
observacao: VARCHAR(1000)
```

Arquivos ajustados:

```txt
src/main/java/br/caixa/gov/hackathon/entity/CotacaoProposta.java
src/main/resources/db/migration/V5__fix_backend_consistency.sql
database/sqlserver/05_fix_backend_consistency.sql
```

### 3. README atualizado

O README agora reflete o projeto real até a Etapa 18:

- backend puro;
- sem Angular no repositório;
- sem gamificação;
- endpoints disponíveis;
- scripts de banco;
- SQL Server com dados hardcoded;
- resposta padrão;
- tabelas existentes.

### 4. Contratos REST atualizados

O arquivo abaixo foi atualizado para orientar a integração com o frontend externo:

```txt
docs/backend-contratos.md
```

## Arquivos alterados/criados

```txt
src/main/java/br/caixa/gov/hackathon/resource/ApplicationResource.java
src/main/java/br/caixa/gov/hackathon/entity/CotacaoProposta.java
src/main/resources/db/migration/V5__fix_backend_consistency.sql
database/sqlserver/05_fix_backend_consistency.sql
README.md
docs/backend-contratos.md
docs/backend-etapa-18.md
```

## Validação local

Execute:

```bash
./mvnw clean test
```

Ou suba a aplicação:

```bash
./mvnw quarkus:dev
```

Health check esperado:

```http
GET http://localhost:8080/api/health
```

## Observação

Neste ambiente não foi possível executar Maven porque o wrapper tentou acessar `repo.maven.apache.org` e não houve resolução DNS. A validação final precisa ser executada localmente.

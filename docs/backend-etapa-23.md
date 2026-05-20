# Etapa 23 — Separação de pacotes por domínio

Esta etapa reorganiza o backend por domínio funcional, mantendo o projeto como backend puro e sem alterar os contratos REST.

## Objetivo

Melhorar organização, manutenção e leitura do projeto, evitando pacotes genéricos muito grandes como `entity`, `dto`, `service`, `repository` e `resource`.

## Nova estrutura

```txt
br.caixa.gov.hackathon
├── anotacao
│   ├── dto
│   ├── entity
│   ├── repository
│   ├── resource
│   └── service
├── assistente
├── auditoria
├── auth
├── common
│   ├── context
│   ├── dto
│   ├── exception
│   ├── filter
│   └── resource
├── conteudo
├── cotacao
├── favorito
├── monitoramento
├── portal
├── processo
├── servico
├── sistema
└── usuario
```

## Regras aplicadas

- Cada domínio passou a ter suas próprias `entities`, `repositories`, `services`, `resources` e `DTOs`.
- Classes transversais ficaram em `common`.
- `Auth`, `Cotação`, `Portal`, `Usuário`, `Auditoria`, `Processos`, `Sistemas`, `Favoritos` e `Anotações` ficaram isolados em pacotes próprios.
- Nenhum endpoint foi alterado.
- Nenhuma tabela foi alterada.
- Nenhum script SQL novo é necessário.

## Atenção ao aplicar

Como esta etapa move arquivos de lugar, não basta copiar por cima. Antes de aplicar, remova os pacotes antigos:

```txt
src/main/java/br/caixa/gov/hackathon/dto
src/main/java/br/caixa/gov/hackathon/entity
src/main/java/br/caixa/gov/hackathon/repository
src/main/java/br/caixa/gov/hackathon/resource
src/main/java/br/caixa/gov/hackathon/service
src/main/java/br/caixa/gov/hackathon/context
src/main/java/br/caixa/gov/hackathon/filter
src/main/java/br/caixa/gov/hackathon/exception
```

Depois copie a nova pasta `src/main/java/br/caixa/gov/hackathon` deste pacote.

## Validação

Após aplicar:

```bash
./mvnw clean test
```

ou:

```bash
./mvnw quarkus:dev
```

Teste rápido:

```http
GET /api/health
POST /api/auth/login
GET /api/portal/inicial/{usuarioId}
GET /api/cotacoes-propostas?pagina=0&tamanho=10
```

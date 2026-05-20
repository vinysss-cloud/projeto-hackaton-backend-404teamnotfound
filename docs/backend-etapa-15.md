# Etapa 15 — Validações de entrada e mensagens funcionais

Esta etapa mantém o projeto como **somente backend** e adiciona validações formais para melhorar a integração com o frontend Angular externo.

## Objetivo

Padronizar validações de entrada para evitar que o frontend envie dados incompletos ou mal formatados para o backend.

## Arquivos alterados

```txt
pom.xml
src/main/java/br/caixa/gov/hackathon/dto/AuthDTOs.java
src/main/java/br/caixa/gov/hackathon/dto/UsuarioDTOs.java
src/main/java/br/caixa/gov/hackathon/dto/CotacaoPropostaDTOs.java
src/main/java/br/caixa/gov/hackathon/exception/ApiExceptionMapper.java
src/main/java/br/caixa/gov/hackathon/resource/AuthResource.java
src/main/java/br/caixa/gov/hackathon/resource/UsuarioResource.java
src/main/java/br/caixa/gov/hackathon/resource/CotacaoPropostaResource.java
```

## Dependência adicionada

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-hibernate-validator</artifactId>
</dependency>
```

## Validações adicionadas

### Login

Endpoint:

```http
POST /api/auth/login
```

Valida:

```txt
matricula obrigatória
matricula entre 3 e 30 caracteres
matricula com caracteres seguros
senha obrigatória
senha entre 4 e 80 caracteres
nomeExibicao com até 120 caracteres
```

Exemplo inválido:

```json
{
  "matricula": "",
  "senha": "12"
}
```

Resposta esperada:

```json
{
  "sucesso": false,
  "status": 400,
  "codigo": "REQUISICAO_INVALIDA",
  "mensagem": "Existem campos inválidos na requisição.",
  "erros": [
    {
      "campo": "matricula",
      "mensagem": "Matrícula é obrigatória."
    },
    {
      "campo": "senha",
      "mensagem": "Senha deve ter entre 4 e 80 caracteres."
    }
  ],
  "timestamp": "2026-05-20T10:30:00"
}
```

### Usuário e preferências

Valida:

```txt
id maior que zero
nomeExibicao entre 2 e 120 caracteres
preferência de tema: CLARO, ESCURO ou SISTEMA
ultimaRota com até 200 caracteres e iniciando com /
```

### Cotações e propostas

Valida:

```txt
id maior que zero
agência obrigatória e numérica na criação
nome do cliente obrigatório na criação
produto obrigatório na criação
responsável obrigatório na criação
valor maior ou igual a zero
status obrigatório na alteração de status
comentário obrigatório ao comentar proposta
paginação com página >= 0 e tamanho entre 1 e 50
limite de recentes entre 1 e 20
```

## Resposta padronizada para erro de validação

A partir desta etapa, erros de validação caem no `ApiExceptionMapper` e retornam sempre:

```json
{
  "sucesso": false,
  "status": 400,
  "codigo": "REQUISICAO_INVALIDA",
  "mensagem": "Existem campos inválidos na requisição.",
  "erros": [
    {
      "campo": "campoComErro",
      "mensagem": "Mensagem funcional para o frontend exibir."
    }
  ],
  "timestamp": "2026-05-20T10:30:00"
}
```

## Impacto para o frontend externo

O frontend deve tratar erro de validação usando:

```ts
error.error.codigo
error.error.mensagem
error.error.erros
```

Para exibir mensagens por campo:

```ts
for (const erro of error.error.erros) {
  console.log(erro.campo, erro.mensagem);
}
```

## Testes rápidos sugeridos

### Login inválido

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"matricula":"","senha":"12"}'
```

### Paginação inválida

```bash
curl "http://localhost:8080/api/cotacoes-propostas?pagina=-1&tamanho=100"
```

### Criar proposta inválida

```bash
curl -X POST http://localhost:8080/api/cotacoes-propostas \
  -H "Content-Type: application/json" \
  -d '{"agencia":"ABC","nomeCliente":"","produto":"","responsavel":""}'
```

## Próxima etapa recomendada

**Etapa 16 — Auditoria automática nos fluxos principais.**

Objetivo:

```txt
registrar automaticamente login
registrar acesso ao portal inicial
registrar criação/edição/status de proposta
registrar inclusão de comentário
registrar busca do assistente
```

Assim o backend passa a gerar histórico útil sem depender do frontend chamar manualmente endpoints de auditoria.

# Etapa 22 — Paginação padrão, Repository e Correlation ID

Esta etapa mantém o projeto como **somente backend** e melhora integração, rastreabilidade e organização interna.

## 1. Paginação padrão

Foi criado o contrato genérico:

```txt
src/main/java/br/caixa/gov/hackathon/dto/PaginacaoDTOs.java
```

Formato padrão para endpoints de listagem:

```json
{
  "sucesso": true,
  "mensagem": "Registros carregados com sucesso.",
  "dados": {
    "itens": [],
    "totalElementos": 0,
    "totalPaginas": 0,
    "pagina": 0,
    "tamanho": 10,
    "primeira": true,
    "ultima": true,
    "possuiProxima": false,
    "possuiAnterior": false,
    "ordenarPor": "dataAtualizacao",
    "direcao": "desc"
  },
  "timestamp": "..."
}
```

Endpoints atualizados para aceitar `pagina` e `tamanho`:

```http
GET /api/cotacoes-propostas?pagina=0&tamanho=10
GET /api/cotacoes-propostas/{id}/comentarios?pagina=0&tamanho=10
GET /api/cotacoes-propostas/{id}/historico?pagina=0&tamanho=10
GET /api/auditoria/usuario/{usuarioId}?pagina=0&tamanho=10
GET /api/auditoria/recentes?pagina=0&tamanho=10
GET /api/acessos-recentes/usuario/{usuarioId}?pagina=0&tamanho=10
GET /api/anotacoes/usuario/{usuarioId}?pagina=0&tamanho=10
GET /api/favoritos/usuario/{usuarioId}?pagina=0&tamanho=10
GET /api/sistemas/usuario/{usuarioId}?pagina=0&tamanho=10
GET /api/servicos?pagina=0&tamanho=10
GET /api/conteudos?pagina=0&tamanho=10
GET /api/normativos?pagina=0&tamanho=10
GET /api/processos?pagina=0&tamanho=10
```

## 2. Camada Repository

Foram criados repositories para concentrar consultas Panache e tirar queries das services/resources/entities.

Novos repositories:

```txt
AcessoServicoRepository
AnotacaoUsuarioRepository
AuditoriaAcessoRepository
ChecklistProcessoRepository
ComentarioPropostaRepository
ConteudoInternoRepository
CotacaoPropostaRepository
FavoritoUsuarioRepository
HistoricoPropostaRepository
PreferenciaUsuarioRepository
ProcessoInternoRepository
ServicoInternoRepository
SistemaUsuarioRepository
UsuarioChecklistProcessoRepository
UsuarioRepository
```

As services continuam responsáveis por regra de negócio, auditoria e montagem de DTOs, mas não montam mais JPQL/HQL diretamente.

## 3. Remoção de queries das entities

Foram removidos métodos estáticos de busca das entities, como:

```txt
Usuario.buscarPorMatricula
FavoritoUsuario.buscar
AcessoServico.buscar
PreferenciaUsuario.buscarPorUsuario
UsuarioChecklistProcesso.buscar
```

Essas consultas agora ficam nos repositories correspondentes.

## 4. CorrelationIdFilter

Criado:

```txt
src/main/java/br/caixa/gov/hackathon/filter/CorrelationIdFilter.java
src/main/java/br/caixa/gov/hackathon/context/CorrelationIdContext.java
```

Funcionamento:

```txt
Frontend envia X-Correlation-Id, se tiver.
Backend reutiliza esse valor.
Se não tiver, backend gera um UUID.
O ID entra no MDC dos logs.
O mesmo ID volta nos headers X-Correlation-Id e X-Request-Id.
```

## 5. correlationId nas respostas de erro

A resposta de erro agora inclui `correlationId`:

```json
{
  "sucesso": false,
  "status": 400,
  "codigo": "REQUISICAO_INVALIDA",
  "mensagem": "Existem campos inválidos na requisição.",
  "path": "/auth/login",
  "correlationId": "d9a4f4a5-2d50-4cb5-912f-a6d22f5d1c77",
  "erros": [],
  "timestamp": "..."
}
```

Isso facilita o diagnóstico no Angular e no backend: o frontend pode exibir ou enviar o `correlationId` para suporte, e o backend localiza a mesma requisição no log.

## 6. application.properties

A configuração CORS agora permite:

```txt
x-correlation-id
x-request-id
```

E os logs passam a usar MDC `%X{correlationId}`.

## Validação recomendada

```bash
./mvnw clean test
```

ou:

```bash
./mvnw quarkus:dev
```

Teste rápido:

```bash
curl -H "X-Correlation-Id: teste-123" http://localhost:8080/api/health -i
```

A resposta deve trazer:

```http
X-Correlation-Id: teste-123
X-Request-Id: teste-123
```

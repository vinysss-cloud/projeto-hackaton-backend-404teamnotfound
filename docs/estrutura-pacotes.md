# Estrutura de pacotes por domínio

## common

Classes reutilizadas por todo o backend:

- DTOs padrões: `ApiResponse`, `ApiErroResponse`, `ErroCampoResponse`, `PaginacaoDTOs`.
- Exceptions e mapper global de erros.
- Filtros de `CorrelationId` e logging estruturado.
- Resources técnicos: `HealthResource` e `ApplicationResource`.

## auth

Responsável pelo login por matrícula, auto-cadastro no primeiro acesso e validação de senha com BCrypt.

## usuario

Responsável por usuário, preferências, busca por matrícula e atualização de dados básicos.

## portal

Responsável por consolidar dados do portal inicial consumido pelo frontend externo.

## cotacao

Responsável por cotações/propostas, filtros, paginação, comentários e histórico.

## auditoria

Responsável por auditoria automática e consulta dos registros por usuário ou recentes.

## assistente

Responsável pela busca orientada simples por termo.

## servico

Responsável por serviços internos e acessos recentes.

## sistema

Responsável por sistemas do usuário.

## favorito

Responsável por favoritos do usuário.

## anotacao

Responsável por anotações do usuário.

## conteudo

Responsável por conteúdos internos e normativos.

## processo

Responsável por processos internos e checklist.

## monitoramento

Responsável pelo endpoint de status técnico e saúde operacional.

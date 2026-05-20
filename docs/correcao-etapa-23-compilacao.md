# Correção da Etapa 23 - Compilação

Correções aplicadas após erro no `mvn clean test`:

1. Removido import incorreto de `br.caixa.gov.hackathon.usuario.entity.UsuarioChecklistProcesso`.
   A entidade correta fica em `br.caixa.gov.hackathon.processo.entity.UsuarioChecklistProcesso`.

2. Corrigido `AssistenteService` para usar `response.quantidade()` no lugar de `response.totalResultados()`.
   O record `BuscaResponse` possui o campo `quantidade`.

3. Corrigida ambiguidade no overload de `ApiErroResponse.of(...)`, fazendo cast explícito para `(String) null` no correlationId.

4. Atualizado `pom.xml` para trocar `quarkus-junit5` por `quarkus-junit`, removendo o warning de relocação do Quarkus 3.33.1.

Arquivos alterados:

- `pom.xml`
- `src/main/java/br/caixa/gov/hackathon/processo/dto/ProcessoDTOs.java`
- `src/main/java/br/caixa/gov/hackathon/processo/repository/UsuarioChecklistProcessoRepository.java`
- `src/main/java/br/caixa/gov/hackathon/processo/service/ProcessoService.java`
- `src/main/java/br/caixa/gov/hackathon/assistente/service/AssistenteService.java`
- `src/main/java/br/caixa/gov/hackathon/common/dto/ApiErroResponse.java`

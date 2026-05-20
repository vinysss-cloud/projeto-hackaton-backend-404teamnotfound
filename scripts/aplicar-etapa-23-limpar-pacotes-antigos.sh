#!/usr/bin/env bash
set -euo pipefail
BASE="src/main/java/br/caixa/gov/hackathon"
for dir in dto entity repository resource service context filter exception; do
  if [ -d "$BASE/$dir" ]; then
    echo "Removendo $BASE/$dir"
    rm -rf "$BASE/$dir"
  fi
done

echo "Pacotes antigos removidos. Agora copie a nova pasta src/main/java/br/caixa/gov/hackathon da Etapa 23 para o projeto."

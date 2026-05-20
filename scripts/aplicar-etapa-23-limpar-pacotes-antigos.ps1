$base = "src/main/java/br/caixa/gov/hackathon"
$pastasAntigas = @("dto", "entity", "repository", "resource", "service", "context", "filter", "exception")

foreach ($pasta in $pastasAntigas) {
    $caminho = Join-Path $base $pasta
    if (Test-Path $caminho) {
        Write-Host "Removendo $caminho"
        Remove-Item $caminho -Recurse -Force
    }
}

Write-Host "Pacotes antigos removidos. Agora copie a nova pasta src/main/java/br/caixa/gov/hackathon da Etapa 23 para o projeto."

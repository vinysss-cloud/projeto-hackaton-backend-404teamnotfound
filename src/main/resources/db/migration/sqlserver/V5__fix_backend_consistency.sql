-- Ajustes finais de consistência entre DTOs, entidades JPA e banco H2.
-- Mantém os tamanhos aceitos pela API para evitar truncamento de dados.

ALTER TABLE cotacoes_propostas ALTER COLUMN produto VARCHAR(120) NOT NULL;
ALTER TABLE cotacoes_propostas ALTER COLUMN modalidade VARCHAR(120);
ALTER TABLE cotacoes_propostas ALTER COLUMN observacao VARCHAR(1000);

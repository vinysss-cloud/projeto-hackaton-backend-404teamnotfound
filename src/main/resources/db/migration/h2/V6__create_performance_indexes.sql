-- V6 - Índices de performance compatíveis com H2

CREATE INDEX IF NOT EXISTS ix_auditoria_usuario_criado ON auditoria_acessos(usuario_id, criado_em DESC);
CREATE INDEX IF NOT EXISTS ix_auditoria_criado ON auditoria_acessos(criado_em DESC);
CREATE INDEX IF NOT EXISTS ix_auditoria_matricula_criado ON auditoria_acessos(matricula, criado_em DESC);
CREATE INDEX IF NOT EXISTS ix_auditoria_acao_criado ON auditoria_acessos(acao, criado_em DESC);

CREATE INDEX IF NOT EXISTS ix_cotacoes_ativo_atualizacao ON cotacoes_propostas(ativo, data_atualizacao DESC, id DESC);
CREATE INDEX IF NOT EXISTS ix_cotacoes_ativo_status_atualizacao ON cotacoes_propostas(ativo, status, data_atualizacao DESC, id DESC);
CREATE INDEX IF NOT EXISTS ix_cotacoes_ativo_agencia_atualizacao ON cotacoes_propostas(ativo, agencia, data_atualizacao DESC, id DESC);
CREATE INDEX IF NOT EXISTS ix_cotacoes_ativo_prioridade_atualizacao ON cotacoes_propostas(ativo, prioridade, data_atualizacao DESC, id DESC);
CREATE INDEX IF NOT EXISTS ix_cotacoes_ativo_data_criacao ON cotacoes_propostas(ativo, data_criacao DESC, id DESC);
CREATE INDEX IF NOT EXISTS ix_cotacoes_ativo_status_prioridade_agencia ON cotacoes_propostas(ativo, status, prioridade, agencia, data_atualizacao DESC, id DESC);
CREATE INDEX IF NOT EXISTS ix_historico_proposta_criado ON historico_proposta(proposta_id, criado_em DESC);
CREATE INDEX IF NOT EXISTS ix_comentarios_proposta_ativo_criado ON comentarios_proposta(proposta_id, ativo, criado_em DESC);

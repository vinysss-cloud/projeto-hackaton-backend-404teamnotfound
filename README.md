# SICAS (Sistema Central de Acessos e Serviços) Backend
 
Backend Quarkus para atender o protótipo SICAS, mantendo o frontend em outro repositório.
 
---
 
## Escopo
 
Este projeto é **somente backend**. Ele não contém Angular, React, HTML, CSS ou telas.
 
O backend entrega APIs REST para o sistema frontend consumir:
 
- login por matrícula com auto-cadastro no primeiro acesso;
- validação de senha a partir do segundo acesso;
- usuário e preferências básicas;
- portal inicial consolidado;
- header e menu;
- serviços internos;
- favoritos do usuário;
- sistemas do usuário;
- acessos recentes;
- normativos;
- anotações;
- processos internos e checklist;
- busca orientada simples;
- cotações/propostas da agência;
- comentários e histórico de propostas;
- auditoria automática dos fluxos principais;
- criação de tabelas por scripts SQL/Flyway.
 
Não há regra de gamificação, badges, ranking, desafios ou endpoints específicos de Angular.
 
---
 
## 🚀 Escalonamento e Evolução
 
O backend do SICAS foi estruturado para permitir crescimento progressivo, suportando a evolução de um MVP funcional para uma plataforma corporativa de integração operacional.
 
### 🔧 Escalonamento Técnico
 
- arquitetura em camadas (controllers → serviços → repositórios);
- desacoplamento completo via APIs REST;
- possibilidade de evolução para microsserviços independentes;
- suporte à troca de banco de dados (H2 → SQL Server → outros);
- versionamento de schema com Flyway;
- preparo para uso de cache (ex: Redis);
- possibilidade de integração com mensageria (event-driven);
- compatibilidade com containerização (Docker) e orquestração (Kubernetes);
- suporte a balanceamento de carga.
 
---
 
### 📈 Escalonamento Funcional
 
- inclusão gradual de novos módulos sem impacto nos existentes;
- expansão contínua do portal consolidado;
- evolução da busca orientada;
- ampliação de processos internos e checklists;
- crescimento do módulo de cotações e propostas;
- integração progressiva com sistemas corporativos.
 
---
 
### 👤 Escalonamento de Usuários
 
- suporte a múltiplos perfis de usuário;
- personalização por preferências individuais;
- evolução para controle de acesso por perfil (RBAC);
- adaptação para diferentes unidades e áreas.
 
---
 
### 🤝 Escalonamento Colaborativo
 
- compartilhamento de rotinas e processos;
- reutilização de fluxos operacionais;
- suporte a substituições (férias/afastamentos);
- uso em grupos de trabalho (GTs) e equipes.
 
---
 
### 🔐 Evolução de Segurança
 
- integração com autenticação institucional (SSO);
- uso de JWT ou sessão;
- controle de tentativas de login;
- auditoria centralizada;
- controle de acesso baseado em perfil.
 
---
 
### 📌 Visão de Crescimento
 
A arquitetura permite que o SICAS evolua para uma **camada central de orquestração de sistemas internos**, consolidando dados e operações em um único ponto, reduzindo a fragmentação e aumentando a eficiência operacional.
 
---
 
## Rodar localmente
 
```bash
./mvnw quarkus:dev

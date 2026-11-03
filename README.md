# Portal Frigotec

Portal de pós-venda, garantias e assistência técnica para equipamentos de cozinha profissional. O cliente cadastra seus equipamentos e abre chamados; o técnico atende, analisa a garantia, agenda a visita e encerra; o administrador cadastra a equipe.

**Versão atual:** [v1.0.0](https://github.com/JuanSanchezVll/Frigotec/releases/tag/v1.0.0)

## Estrutura

```
backend/    API em Java 21 + Spring Boot + JPA + H2   (backend/backend-frigotec/frigotec)
frontend/   Interface em React + Vite (Feature-Sliced Design)
```

## Como rodar

```bash
# Back-end (porta 8080)
cd backend/backend-frigotec/frigotec
./mvnw spring-boot:run
```

```bash
# Front-end (porta 5173)
cd frontend
npm install
npm run dev
```

- Console do banco: `http://localhost:8080/h2-console` (JDBC `jdbc:h2:mem:frigotec`, usuário `sa`, senha vazia). O banco fica em memória e zera a cada reinício.
- Documentação da API: `http://localhost:8080/swagger-ui.html`.

## Acesso e dados de exemplo

| Perfil | Como obtém acesso |
|---|---|
| Cliente | Cadastra-se sozinho em `/cadastro` |
| Técnico e Administrador | Criados por um administrador em `/admin` |

Ao subir, o banco já traz um administrador inicial e dados de exemplo. Senha de todos: `senha123`.

| Usuário | E-mail |
|---|---|
| Administrador | `admin@frigotec.com` |
| Técnico | `davi@frigotec.com` |
| Clientes | `mendesgusta58@gmail.com`, `guimesquita.0612@gmail.com` |

Os exemplos incluem equipamentos, garantias (ativa, em análise e vencida), orçamento e 7 chamados em situações diferentes.

**Variáveis de ambiente:** `SEED_DATA` (`classpath:data.sql` carrega só o admin e o catálogo), `SEED_MODE=never` (sem dados), `JWT_SECRET`, `APP_AMBIENTE` (`prod` exige `JWT_SECRET` próprio), `JWT_EXPIRACAO_MINUTOS`, `MAIL_HOST`, `MAIL_PORT`, `MAIL_USER`, `MAIL_PASSWORD`.

## O que já funciona (v1.0.0)

- Login com token JWT e três perfis (cliente, técnico, administrador), com permissões por rota.
- Cadastro de cliente (pessoa física e jurídica) e cadastro de técnico e administrador pelo admin.
- Catálogo de marcas e modelos, equipamentos do cliente e garantia (só a marca própria, FrigoPro), com situação calculada.
- Chamado completo: protocolo `PR` + 8 dígitos, atribuição e redesignação, avanço em ordem, visita, encerramento e histórico.
- Orçamento no back-end: um pendente por vez, resposta do cliente.
- CRUD do equipamento do cliente: cadastrar, listar, corrigir série/nota fiscal (`PATCH /equipamentos/{id}`) e excluir (`DELETE /equipamentos/{id}`; recusa com 409 se a unidade já teve chamado).
- E-mail (JavaMail): orçamento e mudança de status do chamado, com link pelo protocolo.
- Painel do técnico com KPIs reais: `GET /dashboard/tecnico` (chamados novos, carteira, visitas da semana, orçamentos pendentes).
- Upload de arquivos de verdade: fotos e vídeo do chamado e nota fiscal da unidade (`POST /chamados/{id}/anexos`, `POST /equipamentos/{id}/documentos`), com tipo conferido pelo conteúdo e limites por variável de ambiente.
- Editar equipamento no front (série e nota fiscal) e excluir, com as regras do back-end.
- Contrato da API: Swagger UI e YAML OpenAPI 3.1 públicos em `/v3/api-docs.yaml`; cópia versionada em [docs/openapi.yaml](docs/openapi.yaml).
- Testes: 25 testes de integração do back (`./mvnw test`); front com `npm run lint` e `npm run build`.

## Próximas entregas

| Prioridade | Item | Situação |
|---|---|---|
| 1 | Telas de orçamento (técnico cria, cliente responde) | API pronta, falta a tela |
| 2 | Tela para a equipe cadastrar marcas e modelos | API pronta, falta a tela |
| 3 | Banco persistente em produção | Pronto: perfil `mysql` + `docker-compose.yml` (falta só o servidor de produção) |
| 4 | Integração com o Bling (ERP) | Próximos passos |
| 5 | Conversa entre cliente e técnico | Simulada no front; falta o back |
| 6 | Avaliação do atendimento e cancelamento de chamado | Simulados; cancelamento depende de decisão do SDD (RN-CHM-20) |
| 7 | Decisões abertas do SDD: fases entre visita e finalizado, orçamento sem custo em garantia ativa | Pendente de definição da squad |
| 8 | Qualidade: testes do front e reduzir os 10 avisos de lint | A fazer (CI no GitHub Actions já roda back e front) |
| 9 | Produção: servidor de banco e domínio | A definir (o perfil `mysql` já desliga o console do H2, o SQL e o debug de e-mail) |
| 10 | Exigir 1 aprovação nos PRs quando todos forem colaboradores | A fazer |

## Equipe e contribuição

Juan Sánchez, Davi Jeronimo e Guilherme Mesquita.

- Documentação das entregas: [docs/entregaveis-sprint-2.md](docs/entregaveis-sprint-2.md), [DER](docs/der/README.md), [conformidade com o SDD](docs/conformidade-sdd.md), [protótipo](docs/prototipo/README.md)
- Guia rápido de branch, commit e PR (1 página): [docs/documentacao_branch.docx](docs/documentacao_branch.docx)
- Arquitetura do front: [frontend/docs/ARQUITETURA.md](frontend/docs/ARQUITETURA.md) e funcionamento: [frontend/docs/GUIA-DE-FUNCIONAMENTO.md](frontend/docs/GUIA-DE-FUNCIONAMENTO.md)

Regra do time: task pequena vai direto na `develop`; task grande ou relevante ganha branch própria (`feature/back-*` ou `feature/front-*`) e PR para a `develop`. A `main` recebe só as versões entregues.

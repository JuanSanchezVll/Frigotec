# Portal Frigotec

Portal de pós-venda, garantias e assistência técnica para equipamentos de cozinha profissional. O cliente cadastra seus equipamentos, abre chamados e acompanha o atendimento; o técnico da Frigotec atende os chamados, analisa garantias e envia agendamento, orçamento e prazo.

## Estrutura

```
backend/    API em Java + Spring Boot (backend/backend-frigotec/frigotec)
frontend/   Interface em React + Vite, organizada em Feature-Sliced Design
```

## Como rodar

**Backend** (porta 8080, banco H2 em memória com dados de exemplo):

```bash
cd backend/backend-frigotec/frigotec
./mvnw spring-boot:run
```

Console do H2 em `http://localhost:8080/h2-console`. O envio de e-mail usa variáveis de ambiente (`MAIL_HOST`, `MAIL_PORT`, `MAIL_USER`, `MAIL_PASSWORD`); sem elas, usa um servidor local na porta 1025.

**Frontend** (porta 5173):

```bash
cd frontend
npm install
npm run dev
```

Detalhes da arquitetura do front em [`frontend/docs/ARQUITETURA.md`](frontend/docs/ARQUITETURA.md).

## Equipe

Juan Sánchez, Davi Jeronimo e Guilherme Mesquita.

## Como contribuir

Branches, padrão de commit e Pull Requests estão em [CONTRIBUTING.md](CONTRIBUTING.md). Resumo: nunca commitar direto em `main` ou `develop`; criar `feature/back-*` ou `feature/front-*` a partir da `develop` e abrir PR.

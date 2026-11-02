# Portal Frigotec · front-end

Front-end do Portal Frigotec (pós-venda, garantias e assistência técnica), feito com **React + Vite** (template `react`, JavaScript) e organizado em **Feature-Sliced Design (FSD)**, seguindo o `modelo-fsd` das aulas.

## Como rodar

```bash
npm install
npm run dev
```

O `npm run dev` sobe o front (Vite, `http://localhost:5173/`) e a API de mentira (json-server, `http://localhost:8080`, lendo `db.json`) ao mesmo tempo.

| Comando | O que faz |
|---|---|
| `npm run dev` | front + API mock |
| `npm run dev:web` | só o front |
| `npm run api` | só a API mock |
| `npm run build` | build de produção |
| `npm run lint` | ESLint (inclui as regras de camada do FSD) |

## Estrutura

```
src/
  app/        # main, App (rotas), providers, estilos globais e tokens
  pages/      # uma pasta por tela (17 telas)
  widgets/    # blocos grandes reutilizáveis (cabeçalhos, menu lateral, tabelas...)
  features/   # ações do usuário (cadastro, login, abrir chamado...)
  entities/   # dados de domínio (usuario, equipamento, chamado...)
  shared/     # ui, api (axios), lib, config, assets, styles
docs/
  ARQUITETURA.md         # regras do FSD e onde cada parte do Figma entra
  exemplo-modelo-fsd/    # cópia do modelo das aulas (exemplo de produtos), só referência
```

Leia o `docs/ARQUITETURA.md` antes de colocar código novo.

## Estado atual

Código exportado do Figma Make alocado nas camadas do FSD e convertido para JavaScript. As 17 telas abrem e navegam com dados simulados em memória (sem validações). Veja as decisões da alocação em `docs/ARQUITETURA.md`.

No PowerShell com a execução de scripts bloqueada, use `npm.cmd run dev` no lugar de `npm run dev`.

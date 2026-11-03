# Portal Frigotec · front-end

Front-end do Portal Frigotec (pós-venda, garantias e assistência técnica), feito com **React + Vite** (JavaScript) e organizado em **Feature-Sliced Design (FSD)**.

## Como rodar

Suba antes o back-end (porta 8080). O Vite repassa `/api` para ele.

```bash
npm install
npm run dev
```

Abra `http://localhost:5173/`.

| Comando | O que faz |
|---|---|
| `npm run dev` | front em modo desenvolvimento |
| `npm run build` | build de produção |
| `npm run lint` | ESLint (inclui as regras de camada do FSD) |

No PowerShell com a execução de scripts bloqueada, use `npm.cmd run dev`.

## Estrutura

```
src/
  app/        # main, rotas, layouts, estilos globais e tokens
  pages/      # uma pasta por tela
  widgets/    # blocos grandes reutilizáveis (cabeçalhos, menu, tabelas)
  features/   # ações do usuário (cadastro, login, abrir chamado...)
  entities/   # dados de domínio (usuario, equipamento, chamado...) e suas chamadas à API
  shared/     # ui, api (axios), lib
docs/
  ARQUITETURA.md          # regras do FSD
  GUIA-DE-FUNCIONAMENTO.md # fluxos arquivo por arquivo
  exemplo-modelo-fsd/     # modelo das aulas, só referência
```

Regra de importação: `app > pages > widgets > features > entities > shared` (cada camada só importa das de baixo).

## Estado atual

Login, cadastro, equipamentos, chamados e garantia usam a API. Conversa, avaliação, cancelamento e envio de arquivos ainda são simulados. O mapa do que falta está no README da raiz.

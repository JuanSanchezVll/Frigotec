# Arquitetura (FSD) do Portal Frigotec

## Camadas e regra de dependência

`app` → `pages` → `widgets` → `features` → `entities` → `shared`

Uma camada **só importa das camadas abaixo**. O ESLint bloqueia o contrário (`npm run lint`). Entre slices de `entities`, `features` e `widgets`, o import é sempre pela API pública do slice (`index.js`), nunca por caminho interno.

| Camada | O que guarda |
|---|---|
| `app` | `main.jsx`, `App.jsx` (BrowserRouter + provider), `routes/` (as 17 rotas), `layouts/` (público, cliente, técnico), `index.css` e `styles/tokens.css` |
| `pages` | uma tela por pasta (`index.jsx`); pop-ups e blocos da tela ficam em `ui/` |
| `widgets` | blocos grandes (cabeçalhos, menu lateral, tabelas, conversa, agenda...) em `ui/` |
| `features` | uma ação do usuário (cadastro, login, abrir chamado, garantia...) em `ui/` |
| `entities` | dados do negócio e mocks em `model/`; `estado` guarda o estado simulado da aplicação |
| `shared` | `ui` (componentes base com Tailwind), `lib` (motion, reveal, img, contexto de layout), `api` (axios), `config`, `assets`, `styles` |

## Telas (`pages`) e rotas

| Pasta | Rota | Perfil |
|---|---|---|
| `landing` | `/` | público |
| `cadastro` | `/cadastro` | público |
| `login` | `/login` | público |
| `home-cliente` | `/home` | cliente |
| `meus-equipamentos` | `/equipamentos` | cliente |
| `equipamento-novo` | `/equipamentos/novo` | cliente |
| `chamado-novo` | `/chamados/novo` | cliente |
| `chamados` | `/chamados` | cliente |
| `chamado-detalhe` | `/chamados/:id` | cliente |
| `conversa-cliente` | `/chamados/:id/conversa` | cliente |
| `tecnico-dashboard` | `/tecnico` | técnico |
| `tecnico-chamados` | `/tecnico/chamados` | técnico |
| `tecnico-chamado-detalhe` | `/tecnico/chamados/:id` | técnico |
| `tecnico-conversa` | `/tecnico/chamados/:id/conversa` | técnico |
| `tecnico-agenda` | `/tecnico/agenda` | técnico |
| `tecnico-orcamentos` | `/tecnico/orcamentos` | técnico |
| `tecnico-historico` | `/tecnico/historico` | técnico |

Pop-up nunca é tela: vive dentro da página ou da feature que o abre.

## Slices atuais

- **widgets:** agenda-semana, brand-carousel, brand-panel, conversa, footer, header-app (cabeçalho do cliente e do técnico), header-publico, hero-video, kpi-cards, linha-do-tempo, painel-conversas, sidebar-app, tabela-chamados, tabela-orcamentos.
- **features:** abrir-chamado, aceitar-orcamento, analisar-garantia, atribuir-chamado, avaliar-atendimento, avancar-etapa, cadastrar-equipamento, cadastro, cancelar-chamado, enviar-proposta, finalizar-atendimento, login.
- **entities:** chamado, equipamento, estado, mensagem, orcamento, proposta, usuario, visita.

## Decisões da alocação do código do Figma Make

1. **TypeScript virou JavaScript.** Os `.tsx/.ts` foram convertidos para `.jsx/.js` (tipos removidos), como no projeto das aulas.
2. **HashRouter virou BrowserRouter** e `react-router` virou `react-router-dom`. As URLs agora são `/home`, não `/#/home`.
3. **O estado simulado** (`AppProvider`, `useApp`) saiu de `app/providers` e foi para `entities/estado`, porque features, widgets e páginas dependem dele e camadas de baixo não podem importar de `app`. Quando a API real entrar, ele é substituído pelas chamadas de `entities/*/api`.
4. **`LayoutCtx`** (abrir o painel de conversas) foi para `shared/lib/layout-context.jsx`.
5. **Imports entre slices** passam pelo `index.js` de cada slice.
6. **Slices vazios foram removidos** (header-cliente, header-tecnico e enviar-mensagem não existem no Figma: o cabeçalho é um só, `header-app`, e a mensagem é parte do widget `conversa`).
7. **Páginas renomeadas** para ficar claras: `home` → `home-cliente`, `equipamentos` → `meus-equipamentos`, `tecnico-chamado` → `tecnico-chamado-detalhe`.
8. **Componentes base do modelo das aulas** (Badge, Button, Card, Input, Modal em CSS Modules) foram para `docs/exemplo-modelo-fsd`, para não colidir com os do Figma (`shared/ui`, Tailwind).
9. **ESLint:** três regras estritas do React (`set-state-in-effect`, `refs`, `only-export-components`) estão como aviso, porque o código do Figma funciona mas as viola. A regra de camadas do FSD continua como erro.

## Tokens e estilo

`src/app/styles/tokens.css` é a única fonte de cores, fonte e espaçamentos (`--cor-*`), e `src/app/index.css` expõe esses tokens ao Tailwind v4 (`bg-primaria`, `text-texto-secundario`...). Estilos novos usam os tokens, nunca valores soltos.

## Próximos passos

1. Ligar o estado simulado à API (`json-server`, `db.json`) por `entities/*/api`.
2. Validações dos formulários (marcadas com `// VALIDACAO:`).
3. Resolver os avisos do lint tela a tela.
4. Dividir o bundle por rota (`React.lazy`), pois o build avisa de chunk acima de 500 kB.

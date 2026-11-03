# Guia do time: branches, commits e Pull Requests

Regra de ouro: **ninguém faz commit direto em `main` nem em `develop`**. Todo código novo entra por uma branch de trabalho e um Pull Request (PR).

## 1. Branches

### Fixas (nunca se apagam)

| Branch | Para que serve | Quem escreve nela |
|---|---|---|
| `main` | Versão estável e testável, a que se apresenta ou entrega. Cada versão ganha uma tag (`v1.0.0`) | Só recebe PR vindo de `develop` (ou de um `hotfix/`) |
| `develop` | Integração: junta tudo o que o time terminou e já foi revisado | Só recebe PR de branches de trabalho |

### De trabalho (curtas: nascem de `develop` e morrem no merge)

Formato: `<tipo>/<area>-<descricao-curta>`, tudo em minúsculas, com hífen, sem acento.

| Prefixo | Quando usar | Exemplo |
|---|---|---|
| `feature/` | Funcionalidade nova (uma task do quadro) | `feature/back-orcamento-resposta` |
| `fix/` | Correção de bug encontrado em `develop` | `fix/front-login-token-vencido` |
| `hotfix/` | Bug urgente em `main`. Nasce de `main` e volta para `main` **e** `develop` | `hotfix/back-cpf-duplicado` |
| `refactor/` | Reorganizar código sem mudar comportamento | `refactor/back-chamado-service` |
| `docs/` | Só documentação | `docs/guia-de-testes` |
| `chore/` | Configuração, dependências, build, `.gitignore` | `chore/atualiza-spring-boot` |
| `test/` | Só testes | `test/back-fluxo-chamado` |

`<area>` é `back`, `front` ou `db`. **Uma branch cuida de uma responsabilidade só.**

## 2. Quando criar uma branch nova

- **Uma branch por task.** Task nova = branch nova, sempre a partir de `develop` atualizada.
- **Back e front nunca na mesma branch.** Se a task precisa dos dois, são duas branches (`feature/back-...` e `feature/front-...`) e dois PRs. Combinem antes o contrato da API (rota, corpo, códigos de resposta).
- Bug achado em `develop` → `fix/`. Bug em produção (`main`) → `hotfix/`.
- Não reaproveite branch de task já mergeada: crie outra.

## 3. Onde sobe cada coisa

| O que você fez | Branch | PR vai para |
|---|---|---|
| Endpoint, regra de negócio, entidade, segurança (Java) | `feature/back-*` | `develop` |
| Tela, componente, integração com a API (React) | `feature/front-*` | `develop` |
| Tabela, seed, `data.sql` | `feature/db-*` ou dentro da branch do back, se for da mesma task | `develop` |
| README, guias, diagramas | `docs/*` | `develop` |
| Dependência, script, `.gitignore`, config | `chore/*` | `develop` |
| Bug urgente em `main` | `hotfix/*` | `main` e depois `develop` |
| Versão pronta para entrega | PR `develop` → `main` + tag | `main` |

## 4. Passo a passo do dia a dia

```bash
# 1. Atualize a develop
git checkout develop
git pull origin develop

# 2. Crie a branch da task
git checkout -b feature/back-orcamento-resposta

# 3. Trabalhe e faça commits pequenos (veja a seção 5)
git add <arquivos>
git commit -m "feat(back): permite o cliente aceitar ou recusar orçamento"

# 4. Antes de abrir o PR, traga as novidades da develop e teste
git fetch origin
git rebase origin/develop          # se der conflito: resolva, git add, git rebase --continue
cd backend/backend-frigotec/frigotec && ./mvnw test      # back
cd frontend && npm run lint && npm run build              # front

# 5. Suba a branch e abra o PR para a develop
git push -u origin feature/back-orcamento-resposta
```

Depois do merge, apague a branch (o GitHub oferece o botão) e volte para o passo 1.

## 5. Padrão de commit (Conventional Commits)

```
<tipo>(<escopo>): <descrição curta>

[corpo opcional: o que e por quê]

[rodapé opcional: Closes #12]
```

| Tipo | Uso |
|---|---|
| `feat` | Funcionalidade nova para o usuário |
| `fix` | Correção de bug |
| `docs` | Só documentação |
| `style` | Formatação que não muda o código (espaço, ponto e vírgula) |
| `refactor` | Reestruturação sem bug corrigido nem funcionalidade nova |
| `perf` | Melhora de desempenho |
| `test` | Testes novos ou corrigidos |
| `chore` | Build, dependências, configuração, `.gitignore` |

**Escopos do projeto:** `back`, `front`, `db`, `auth`, `docs`.

Regras para a frase:

- Verbo no presente, em minúsculas, sem ponto final, **até 72 caracteres**.
- Diga **o que mudou**, não "ajustes" nem "alterações".
- Um commit = uma ideia. Se a frase precisa de "e" demais, quebre em dois commits.

| Ruim | Bom |
|---|---|
| `ajustes` | `fix(front): mostra aviso quando o token vence` |
| `atualizei o back` | `feat(back): bloqueia visita em data passada` |
| `mudanças no login` | `refactor(front): move regras do login para useLogin` |

Para o Git já abrir o modelo no commit: `git config commit.template .gitmessage`.

## 6. Pull Request

- Título no mesmo padrão do commit: `feat(back): permite responder orçamento`.
- Use o modelo que o GitHub abre sozinho (`.github/PULL_REQUEST_TEMPLATE.md`).
- Um PR de 1 a 3 commits e menos de ~400 linhas é revisado rápido. Maior que isso, divida.
- **Pelo menos 1 aprovação** de outra pessoa do time antes do merge. Quem escreveu não aprova o próprio PR.
- Merge de branch de trabalho em `develop`: **Squash and merge** (um commit limpo, com o título do PR).
  No Squash, o GitHub sugere o nome da branch como título: **edite o título** para o padrão de commit (`feat(back): permite responder orçamento`).
- Merge de `develop` em `main`: **Create a merge commit**, seguido da tag `vX.Y.Z`.

## 7. O que nunca subir

- Senhas, tokens, chaves (`JWT_SECRET`, `MAIL_PASSWORD`), arquivos `.env`.
- `target/`, `node_modules/`, `dist/`, pastas de IDE (`.idea/`, `.vscode/`).
- Arquivos pessoais ou exportações grandes (Figma, vídeos, PDFs da faculdade).

Se subiu um segredo sem querer: avise o time na hora e troque a senha. Apagar o arquivo depois **não** remove do histórico.

## 8. Proteção das branches (configurar uma vez)

No GitHub: **Settings → Branches → Add branch ruleset** para `main` e `develop`:

1. Marcar *Require a pull request before merging* com 1 aprovação.
2. Marcar *Block force pushes* e *Restrict deletions*.
3. Em *Settings → General → Pull Requests*, deixar liberados *Squash merging* e *Merge commits* e ativar *Automatically delete head branches*.

## 9. Resumo de uma linha

`develop` atualizada → branch `tipo/area-descricao` → commits pequenos no padrão → `rebase` + testes → PR para `develop` → 1 aprovação → squash → apaga a branch.

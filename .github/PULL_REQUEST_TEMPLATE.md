## O que mudou
<!-- Uma ou duas frases. Título do PR no padrão: tipo(escopo): descrição curta -->

## Por quê
<!-- Link da task/issue, se houver: Closes #00 -->

## Como testar
<!-- Passos curtos: tela, rota ou comando -->

## Checklist
- [ ] A branch segue o padrão `tipo/area-descricao` e saiu da `develop` atualizada
- [ ] Back **ou** front (não os dois na mesma branch)
- [ ] Commits no padrão Conventional Commits
- [ ] Back: `./mvnw test` passou
- [ ] Front: `npm run lint` (sem erros) e `npm run build` passaram
- [ ] Nenhuma senha, token, `.env`, `target/`, `node_modules/` ou `dist/` no PR

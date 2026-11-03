# Roteiro de apresentação — o que mostrar no site

Roteiro de cerca de 15 minutos, na ordem dos slides. Cada demonstração aponta o slide e o entregável que ela prova. Todos os passos foram executados de ponta a ponta contra o sistema rodando (cadastro, login, equipamento, CRUD, chamado, atendimento até FINALIZADO).

## 0. Antes de apresentar (10 minutos antes)

1. Back-end (porta 8080), no terminal:
   ```
   cd backend/backend-frigotec/frigotec
   ./mvnw spring-boot:run
   ```
   Espere a linha `Started FrigotecApplication`.
2. Front-end (porta 5173), em outro terminal:
   ```
   cd frontend
   npm run dev
   ```
3. (Opcional, para mostrar o e-mail saindo) terceiro terminal, na raiz do repositório:
   ```
   python docs/demo/smtp_sink.py docs/demo/emails.log
   ```
   Sem isso o portal funciona do mesmo jeito; o e-mail só fica registrado com status ERRO na tabela `mensagem_email`.
4. Abra em abas do navegador: `http://localhost:5173` (site), `http://localhost:8080/swagger-ui.html` (Swagger) e `http://localhost:8080/h2-console` (banco; JDBC URL `jdbc:h2:mem:frigotec`, usuário `sa`, senha vazia).
5. Deixe prontos: uma foto JPG ou PNG pequena (do defeito) e um PDF ou imagem qualquer (nota fiscal).
6. **Banco em memória:** reiniciar o back-end apaga o que foi cadastrado e volta ao seed. Faça o cadastro de teste uma vez antes, para saber que funciona, e reinicie o back-end para começar limpo.

## 1. Contas e dados

**Contas que já existem (senha de todas: `senha123`)**

| Perfil | E-mail |
|---|---|
| Administrador | `admin@frigotec.com` (na tela de login há o botão "Administrador") |
| Técnico | `davi@frigotec.com` |
| Cliente com equipamentos e chamados | `mendesgusta58@gmail.com` (João) |
| Cliente | `guimesquita.0612@gmail.com` (Maria) |

**Cliente novo para o cadastro ao vivo (pessoa física)**

| Campo | Valor |
|---|---|
| Nome completo | Carlos Eduardo Lima |
| CPF | `529.982.247-25` |
| Celular | `(11) 98877-6655` |
| Telefone | `(11) 3333-4444` |
| CEP | `01310-100` |
| Estado / Cidade | SP / São Paulo |
| Rua / Número | Avenida Paulista / 1578 |
| E-mail | `carlos.teste@exemplo.com` |
| Senha | `teste123` |

**Técnico novo, criado pelo administrador na tela `/admin`**

| Campo | Valor |
|---|---|
| Perfil | Técnico |
| Nome completo | Rafael Souza |
| E-mail | `rafael.tecnico@frigotec.com` |
| CPF | `111.444.777-35` |
| Celular | `(11) 97766-5544` |
| CEP / Estado / Cidade | `04538-132` / SP / São Paulo |
| Rua / Número | Avenida Brigadeiro Faria Lima / 3477 |
| Senha | `teste123` |

**Cuidado com o CPF:** `390.533.447-05` já é do Davi no seed e dá erro 409. CPF e CNPJ têm dígito verificador: números inventados são recusados.

**Equipamento para cadastrar:** marca FrigoPro, modelo Chapa Bifteira FP-600, série `TESTE-0001`, garantia comum, data da compra `01/10/2026`, nota fiscal = o arquivo que você separou.

**Chamado para abrir:** categoria "Temperatura irregular", descrição "A chapa esquenta só de um lado", com 1 foto.

## 2. Sequência da demonstração (15 min)

### Bloco A. Entrar no sistema (slides 30 e 31, ~3 min)
**Prova:** Login JWT e as telas de cadastro e login integradas ao back-end.

1. Landing page (`/`): mostre a home, o carrossel de marcas (vem do back-end, `GET /fabricantes`) e o botão "Cadastre-se".
2. **Cadastro do cliente** (3 etapas): tipo de pessoa (Pessoa Física), dados pessoais, dados de acesso. Preencha com o Carlos. Ao terminar, o site avisa "Cadastro concluído".
3. **Login do cliente:** entre com `carlos.teste@exemplo.com`. Isso mostra a tela de login e o redirecionamento para a Home do cliente.
4. **O que dizer:** "A senha vai para o back-end, é comparada com o hash BCrypt e, se estiver certa, o servidor devolve um token JWT. A partir daí todo pedido leva esse token."

### Bloco B. CRUD completo do equipamento (slide 28, ~4 min)
**Prova:** CRUD com relacionamento (Usuário → Equipamento → Garantia).

1. **Create:** menu "Meus equipamentos" → "Cadastrar equipamento". Marca FrigoPro (aparece a área de garantia), modelo Chapa Bifteira FP-600, série `TESTE-0001`, garantia comum, data da compra, anexe a nota fiscal. Clique em "Cadastrar". No pop-up escolha "Cadastrar novo equipamento" e cadastre um segundo (série `DESCARTE-1`).
2. **Read:** em "Meus equipamentos" aparecem os cards com marca, modelo, série e o selo da garantia (calculada pela data).
3. **Update:** no primeiro card, "Editar equipamento", troque a série para `TESTE-0002` e salve. A tela atualiza.
4. **Delete:** no card `DESCARTE-1`, "Excluir equipamento" e confirme. O card some.
5. **Regra de negócio (mostrar depois do Bloco C):** tentar excluir o equipamento que já tem chamado. O aviso vermelho do topo mostra a mensagem do back-end (409: o histórico é preservado).
6. **O que dizer:** "O relacionamento é Usuário, Equipamento do usuário e Garantia, que é 1 para 1 com a unidade. Cada operação é um endpoint: POST, GET, PATCH e DELETE em `/equipamentos`."

### Bloco C. Abrir e acompanhar um chamado (slide 33, ~2 min)
**Prova:** telas integradas (abertura de chamado, histórico e detalhe).

1. "Abrir chamado": etapa 1, escolha o equipamento; etapa 2, categoria, descrição e **uma foto** (o upload é real).
2. Ao enviar aparece o **número do chamado** (formato `PR` + 8 dígitos, gerado pelo servidor).
3. "Meus chamados": mostre o histórico e abra o detalhe: a linha do tempo de 6 etapas e a foto enviada.
4. **Volte ao Bloco B, passo 5** e mostre a exclusão recusada.

### Bloco D. Lado da equipe e dashboard (slides 29, 32 e 34, ~4 min)
**Prova:** login do técnico, dashboard em React com Spring Boot e o padrão Observer.

1. Saia do cliente ("Sair") e clique em "Administrador" na tela de login. Vá em **Colaboradores** (`/admin`) e cadastre o técnico Rafael. Mostre que ele aparece na lista. (Técnico e administrador não se cadastram sozinhos: é regra do SDD.)
2. Saia e entre como **Rafael** (login do técnico). O sistema leva ao painel do técnico.
3. **Dashboard:** mostre os 4 KPIs (chamados novos, meus chamados, visitas da semana, orçamentos aguardando). Diga: "Esses números vêm do endpoint `GET /dashboard/tecnico`, calculados no banco."
4. Em "Chamados novos" clique em **Atribuir a mim** no chamado do Carlos. Volte ao dashboard: **chamados novos cai e meus chamados sobe**.
5. Abra o chamado e clique em **Avançar etapa** (vai para "Contato do técnico").
6. **Observer:** "A cada mudança de status o serviço só publica um evento; um ouvinte separado manda o e-mail depois que o status foi gravado." Mostre uma das duas provas:
   - o arquivo `docs/demo/emails.log` (se rodou o `smtp_sink.py`), com o assunto "Chamado PR…: Contato do técnico"; ou
   - no H2 Console: `SELECT assunto, status FROM mensagem_email ORDER BY id DESC;`
7. Volte ao cliente (Carlos) e mostre que a linha do tempo mudou. Ele ainda pode clicar em **Aprovar serviço** (a próxima etapa é dele).
8. (Se sobrar tempo) volte ao técnico: **Agendar visita** (data futura) e **Finalizar atendimento** (laudo). O chamado vai a FINALIZADO.

### Bloco E. Swagger, OpenAPI e JWT (slide 30, ~2 min)
**Prova:** documentação Swagger, exportação OpenAPI YAML e Login JWT.

1. Abra `http://localhost:8080/swagger-ui.html`: mostre o título "Portal Frigotec — API", as 32 operações agrupadas por controller.
2. `POST /usuarios/login` → "Try it out" → e-mail `davi@frigotec.com`, senha `senha123` → copie o `token` da resposta.
3. Botão **Authorize** (cadeado) → cole o token → `GET /dashboard/tecnico` → Execute: resposta **200** com os números.
4. Mostre a segurança: sem o token, ou com o token de um cliente, o mesmo endpoint responde **401** ou **403**.
5. Abra `http://localhost:8080/v3/api-docs.yaml` e mostre que a exportação OpenAPI-YAML é pública. A cópia versionada está em `docs/openapi.yaml`.

### Bloco F (opcional). Banco e DER (slide 21, ~1 min)
No H2 Console, rode `SHOW TABLES;` e depois `SELECT id, nome_completo, perfil FROM usuario;` (a coluna `senha` guarda o hash BCrypt, nunca a senha). Mostre que o banco tem as mesmas tabelas do slide do DER.

## 3. Mapa: slide → o que aparece no site

| Slide | Conteúdo | Mostrar no site |
|---|---|---|
| 1–20 | Contexto, problema, persona, solução, ODS, metodologia, requisitos, jornada, BPMN | Nada. Se quiser um gancho: abra a landing no slide 11 (Solução) |
| 21 | DER | H2 Console: `SHOW TABLES;` (opcional) |
| 22–24 | C4, classes, planilha de arquitetura | Nada |
| 25–27 | SDD, wireframe/usabilidade, protótipo | Nada. O protótipo final é o próprio site |
| 28 | CRUD com relacionamento | **Bloco B** |
| 29 | Observer | **Bloco D**, passo 6 |
| 30 | Swagger e OpenAPI YAML | **Bloco E** |
| 31 | Site FrigoTec (landing, cadastro, login) | **Bloco A** |
| 32 | API externa (e-mail) | **Bloco D**, passo 6 (log do e-mail) |
| 33 | 5 telas integradas | **Blocos A, B e C** |
| 34 | Dashboard React + Spring Boot | **Bloco D**, passos 3 e 4 |
| 35–37 | Resultados, próximos passos, encerramento | Nada |

## 4. Mapa: entregável → onde provar

| Entregável | Prova ao vivo |
|---|---|
| Login JWT | Blocos A (site) e E (Swagger com Authorize) |
| Swagger + OpenAPI YAML | Bloco E |
| CRUD com relacionamento | Bloco B |
| Padrão de projeto (Observer) | Bloco D, passo 6 |
| 5 telas integradas | Login, cadastro, meus equipamentos, abrir chamado e detalhe do chamado (Blocos A, B e C) |
| Dashboard React + Spring Boot | Bloco D, passos 3 e 4 |
| API externa | E-mail: `emails.log` ou tabela `mensagem_email` |
| DER | Bloco F e a imagem em `docs/der` |
| Protótipo, C4, planilhas, SDD, usabilidade | Só nos slides e nos arquivos da pasta `docs` |

## 5. O que NÃO mostrar (ainda é simulado)

- **Conversa com o técnico** (menu "Conversas"), **avaliação** e **cancelamento** de chamado: simulados no front, dependem de decisão do SDD.
- **Telas de orçamento:** a API está pronta e testada, mas a tela ainda não existe. Se perguntarem, mostre `POST /orcamentos` no Swagger.
- **Bling:** é proposta (slide 32), sem integração.
- **Caixa "Conversas com mensagem nova"** do dashboard: simulada (os 4 KPIs são reais).

## 6. Respostas rápidas para a banca

- **Como o login funciona?** BCrypt no banco, token JWT assinado no login, o front manda `Authorization: Bearer` em toda requisição e o back valida e checa o perfil em cada rota.
- **Um cliente enxerga dados de outro?** Não. O id de quem pede vem do token, e recurso alheio devolve 404.
- **Por que o Observer?** O serviço do chamado não conhece e-mail; falha de e-mail não desfaz o atendimento e novos ouvintes (WhatsApp) entram sem mexer no serviço.
- **Por que a garantia não é um campo "ativa"?** É calculada pela data de fim; um campo gravado ficaria desatualizado.
- **O banco some ao reiniciar?** Sim, é H2 em memória na demonstração. Há perfil `mysql` e `docker-compose.yml` para banco persistente.
- **O que falta?** Telas de orçamento e de catálogo, testes automatizados do front, integração com o Bling e o servidor de produção.

## 7. Se algo der errado

| Sintoma | O que fazer |
|---|---|
| Site mostra "Não foi possível falar com o servidor" | O back-end não subiu. Verifique o terminal; suba de novo e recarregue (F5) |
| Erro 409 ao cadastrar | E-mail ou CPF já usados (o banco guarda até reiniciar). Reinicie o back-end ou use outro e-mail e CPF válido |
| Login falha | Senha dos usuários de exemplo é `senha123`; os que você cadastrou usam a senha que digitou |
| Upload recusado | Tipo ou tamanho: fotos JPG/PNG/MP4 até 10 MB (máximo 5 fotos e 1 vídeo por chamado); nota fiscal PDF/JPG/PNG, só para marca FrigoPro |
| Imagem do upload não aparece | Recarregue a página do detalhe do chamado |
| Tudo travou | Reinicie o back-end (o banco volta ao seed) e refaça o cadastro de teste |

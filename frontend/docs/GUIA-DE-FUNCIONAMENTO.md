# Portal Frigotec: guia de funcionamento (do zero)

Guia para entender como o back-end (Java + Spring Boot) e o front-end (React + Vite) conversam, passo a passo, usando como exemplo o **cadastro** e o **login** do cliente e do técnico.

---

## 1. Visão geral

```
 Navegador (React + Vite)  ──HTTP/JSON──▶  API (Spring Boot)  ──JPA──▶  Banco H2 (memória)
 http://localhost:5173        axios          http://localhost:8080
```

- **Back-end** (`backend/backend-frigotec/frigotec`): roda no IntelliJ, porta **8080**, expõe os endpoints (`/usuarios`, `/chamados`, ...).
- **Front-end** (`frontend`): roda no VS Code, porta **5173**. Tudo que começa com `/api` é repassado pelo Vite para `http://localhost:8080` (proxy em `frontend/vite.config.js`). Isso evita o erro de CORS, porque o navegador só enxerga a porta 5173.
- **Banco**: H2 em memória. Ao reiniciar o back-end, os dados somem e voltam ao que está em `src/main/resources/data.sql`.

## 2. Como iniciar tudo (passo a passo)

1. **IntelliJ**: abra `backend/backend-frigotec/frigotec`. Como o `pom.xml` mudou (módulo de criptografia BCrypt), clique em **Reload All Maven Projects** (ícone "m" no canto direito). Rode `FrigotecApplication` (▶). Espere `Started FrigotecApplication`. Teste: `http://localhost:8080/fabricantes` mostra JSON.
2. **VS Code**: abra a pasta `frontend`. No terminal, na primeira vez: `npm install`. Depois: `npm run dev` (no PowerShell com bloqueio de scripts, use `npm.cmd run dev`). Abra `http://localhost:5173`.
3. **Ver os dados no H2**: abra `http://localhost:8080/h2-console`, JDBC URL `jdbc:h2:mem:frigotec`, usuário `sa`, senha vazia. Rode `SELECT ID, PERFIL, NOME_COMPLETO, EMAIL FROM USUARIO;`. A coluna `SENHA` mostra o **hash BCrypt** (começa com `$2a$10$`), nunca a senha pura.

**Usuários de exemplo** (senha de todos: `senha123`)

| E-mail | Perfil |
|---|---|
| `mendesgusta58@gmail.com` | CLIENTE |
| `guimesquita.0612@gmail.com` | CLIENTE |
| `davi@frigotec.com` | TECNICO |
| `admin@frigotec.com` | ADMIN |

**Quem cria técnico e administrador:** só um ADMIN, pela tela `/admin`. Quem se cadastra sozinho em `/cadastro` é sempre CLIENTE.

---

## 3. Back-end: as camadas do Spring (e quem chama quem)

```
HTTP  ▶ Controller ▶ Service ▶ Repository ▶ Banco
              │         │
              │         └─ Mapper (Entity ⇄ DTO)
              └─ DTO (entrada/saída)        Exception → GlobalExceptionHandler (erro em JSON)
```

Pasta base: `src/main/java/com/frigo/frigotec/`

| Pasta | Papel | Exemplo no cadastro/login |
|---|---|---|
| `controller` | Recebe a requisição HTTP, valida o corpo (`@Valid`) e chama o service. **Não tem regra de negócio.** | `UsuarioController` (`POST /usuarios`, `POST /usuarios/login`, `POST /usuarios/colaboradores`) |
| `dto` | Formato do JSON que entra e sai. Protege a entidade: a senha nunca sai. | `UsuarioCadastroDto` (entrada), `UsuarioLoginDto`, `ColaboradorCadastroDto`, `UsuarioDto` (saída com `id` e `perfil`) |
| `service` | **Regras de negócio** e transação. | `UsuarioService`: `cadastrar` (sempre CLIENTE), `cadastrarColaborador` (exige a chave), `autenticar` (compara a senha) |
| `mapper` | Converte DTO ⇄ entidade. | `UsuarioMapper.toEntity`, `toDto` |
| `repository` | Acesso ao banco com Spring Data JPA (sem SQL escrito à mão). | `UsuarioRepository.findByEmail`, `existsByEmail` |
| `entity` | Tabelas (JPA). | `Usuario` (campo `perfil`), `Perfil` (enum CLIENTE, TECNICO, ADMIN), `Endereco`, `Empresa` |
| `exception` | Erros de negócio com status HTTP. | `EmailJaCadastradoException` (409), `CredenciaisInvalidasException` (401), `AcessoNegadoException` (403) |
| `exception/GlobalExceptionHandler` | Transforma qualquer erro em JSON `{status, mensagem, campos}`. O front lê `mensagem`. | ver seção 6 |
| `config` | Beans de configuração. | `SegurancaConfig` cria o `PasswordEncoder` (BCrypt) |

**Regra de ouro das camadas:** Controller → Service → Repository, sempre nessa ordem. O Controller nunca fala com o Repository direto.

## 4. Front-end: Feature-Sliced Design (FSD)

```
src/
  app/        inicialização: rotas, layouts, guarda de rota, estilos globais
  pages/      uma pasta por tela (só compõe widgets e features)
  widgets/    blocos grandes de interface (cabeçalho, menu lateral, tabelas)
  features/   ações do usuário (login, cadastro, abrir chamado...)
  entities/   dados do negócio (usuario, chamado, equipamento...)
  shared/     reutilizável sem regra de negócio (ui, api/axios, lib)
```

**Regra de importação (a mais cobrada na avaliação):** uma camada só importa das camadas **abaixo** dela.
`app → pages → widgets → features → entities → shared`
- `features` pode importar `entities` e `shared`. **Não** pode importar `widgets`, `pages` nem `app`.
- `entities` **não** importa `features`.
- `shared` não importa ninguém (é a base). Regra de negócio **não** fica em `shared`.
- O ESLint bloqueia a violação (`npm run lint`).

**Dentro de cada fatia (slice) existem segmentos:**
- `ui/`: componentes React (a tela em si).
- `model/`: estado, hooks, validações e regras (a "lógica").
- `api/`: chamadas HTTP (axios).

Cada fatia expõe só o que está no seu `index.js` (a API pública). Quem importa usa `.../features/login`, nunca o caminho interno.

### Onde está cada parte do login e do cadastro

| Arquivo | Camada / segmento | O que faz |
|---|---|---|
| `shared/api/http.js` | shared/api | Cria a instância do axios com `baseURL: "/api"` |
| `shared/api/erro.js` | shared/api | Converte o erro do axios em mensagem (lê `mensagem` do backend) |
| `entities/usuario/api/usuarioApi.js` | entities/api | `login()`, `cadastrarUsuario()`, `cadastrarColaborador()`: **chamam o backend** |
| `entities/usuario/model/mapeadores.js` | entities/model | Monta o JSON do cadastro a partir do formulário (só números em CPF/CNPJ/telefone) |
| `entities/usuario/model/validadores.js` | entities/model | Regra da senha (mínimo 6, confirmação igual) |
| `entities/usuario/model/perfil.js` | entities/model | Perfis e `rotaInicial(perfil)`: cliente → `/home`, técnico → `/tecnico` |
| `entities/usuario/model/sessao.js` | entities/model | Guarda, lê e apaga o usuário logado (`localStorage`) |
| `entities/estado/model/state.jsx` | entities/model | Provider global: `usuario`, `entrar()`, `sair()` |
| `features/login/model/useLogin.js` | features/model | Estado do formulário e regra de entrar |
| `features/login/ui/LoginForm.jsx` | features/ui | Tela do formulário de login |
| `features/cadastro/model/useCadastro.js` | features/model | Passos, campos e envio do cadastro do cliente |
| `features/cadastro/ui/CadastroForm.jsx` | features/ui | Formulário em 3 passos |
| `features/cadastro-colaborador/...` | features | Cadastro do técnico (model + ui) |
| `pages/login/index.jsx`, `pages/cadastro/index.jsx`, `pages/admin-colaboradores/index.jsx` | pages | Montam a página usando as features |
| `app/routes/index.jsx` | app | Define as URLs e quais são protegidas |
| `app/routes/RotaProtegida.jsx` | app | Guarda de rota por perfil |

## 5. Onde o axios foi instalado e onde é usado

- **Instalação:** está em `frontend/package.json`, em `dependencies` (`"axios"`). Veio junto com o modelo FSD das aulas; o `npm install` instala tudo que o `package.json` lista (o mesmo efeito de `npm i axios`).
- **Criação (1 vez):** `src/shared/api/http.js` faz `axios.create({ baseURL: "/api" })`.
- **Uso:** só nas funções de `entities/*/api/*.js`. As telas **não** chamam o axios direto: elas chamam o hook do `model`, que chama a função da `entities/.../api`.
- **Proxy:** o navegador chama `http://localhost:5173/api/usuarios/login`; o Vite repassa para `http://localhost:8080/usuarios/login`.

## 6. Fluxos passo a passo

### 6.1 Login (cliente e técnico, o mesmo endpoint)

1. O usuário digita e-mail e senha em `features/login/ui/LoginForm.jsx` e clica **Acessar**.
2. `LoginForm` chama `entrar()` do hook `features/login/model/useLogin.js`.
3. O hook chama `login(email, senha)` de `entities/usuario/api/usuarioApi.js`.
4. O axios envia: `POST /api/usuarios/login` com `{ "email": "...", "senha": "..." }`.
5. O Vite repassa para o Spring: `UsuarioController.login` (`@PostMapping("/login")`).
6. O controller chama `UsuarioService.autenticar`:
   - busca o usuário no `UsuarioRepository.findByEmail`;
   - compara a senha com o hash: `passwordEncoder.matches(...)`;
   - se e-mail ou senha estiverem errados: `CredenciaisInvalidasException` → **401** com a mesma mensagem nos dois casos (não revela qual errou).
7. Se der certo, o `UsuarioMapper.toDto` devolve `{ id, perfil, nomeCompleto, email, ... }` (sem senha).
8. De volta ao front, `useLogin` chama `entrar(usuario)` do `entities/estado` (grava a sessão) e navega para `rotaInicial(usuario.perfil)`:
   - `CLIENTE` → `/home` (Home do cliente);
   - `TECNICO` → `/tecnico` (Dashboard do técnico);
   - `ADMIN` → `/admin` (gestão de colaboradores).
9. `app/routes/RotaProtegida.jsx` garante que cliente não abre `/tecnico` e vice-versa: quem tenta é redirecionado para a própria home; sem login vai para `/login`.

A diferença entre as duas personas **não está no endpoint**, está no campo `perfil` que o backend devolve.

### 6.2 Cadastro do cliente

1. `/cadastro` → `pages/cadastro/index.jsx` → `features/cadastro/ui/CadastroForm.jsx` (3 passos: tipo de pessoa, dados + endereço, acesso).
2. Ao clicar **Cadastrar**, `useCadastro.cadastrar()` valida a senha (`validarSenha` em `entities/usuario/model`) e monta o JSON com `montarCadastroCliente` (`mapeadores.js`).
3. `cadastrarUsuario()` envia `POST /api/usuarios`:
   ```json
   { "nomeCompleto": "Cliente Teste", "email": "cliente@exemplo.com", "senha": "abc123",
     "cpf": "12345678909", "celular": "11987654321",
     "endereco": { "cep": "01001000", "estado": "SP", "cidade": "Sao Paulo", "rua": "Rua A", "numero": "10" } }
   ```
   (pessoa jurídica envia `cnpj`, `razaoSocial`, `inscricaoEstadual` e `empresa`).
4. `UsuarioController.cadastrar` (`@Valid` valida e-mail, senha mínima 6, CPF/CNPJ, celular só números) → `UsuarioService.cadastrar` → `salvar(dto, Perfil.CLIENTE)`:
   - exige CPF **ou** CNPJ (nunca os dois, nunca nenhum);
   - checa e-mail, CPF e CNPJ únicos (409 se repetir);
   - `passwordEncoder.encode(senha)` grava o **hash BCrypt**;
   - perfil sempre `CLIENTE` (o corpo nunca aceita perfil: evita escalar privilégio).
5. Resposta `201` com o usuário (com `id` e `perfil`). O front mostra "Cadastro concluído" e abre `/login`.

### 6.3 Cadastro de técnico e de administrador (feito pelo ADMIN)

O técnico **não** se cadastra sozinho (SDD RN-USR-03). Um ADMIN logado cria técnicos e outros administradores.

1. O ADMIN entra (`admin@frigotec.com`), vai para `/admin` (`pages/admin-colaboradores`), que monta a lista (`widgets/tabela-colaboradores`) e o formulário (`features/cadastro-colaborador`).
2. A lista vem de `useColaboradores` (`entities/usuario/model`) → `listarColaboradores()` → `GET /api/usuarios/colaboradores`.
3. Ao cadastrar, `useCadastroColaborador` chama `cadastrarColaborador(dados)` → `POST /api/usuarios/colaboradores`. O axios (`shared/api/http.js`) anexa sozinho o cabeçalho `Authorization: Bearer <token>` do login.
4. No back-end, `UsuarioController` chama `UsuarioService.cadastrarColaborador`, depois que o `SegurancaConfig` já barrou quem não é ADMIN: sem token ou token inválido = **401**; perfil diferente de ADMIN = **403**. No service: perfil CLIENTE = **422**; senão reaproveita `salvar(...)` (hash BCrypt, e-mail/CPF únicos).
5. A página recarrega a lista e o novo colaborador aparece. Ele entra com o e-mail e a senha definidos e vai para `/tecnico` (ou `/admin`).

O perfil vem do **token JWT assinado pelo servidor** (SDD RN-USR-06): quem não tem a chave secreta não consegue forjar o perfil nem o id.

### 6.4 Como o erro chega na tela

O `GlobalExceptionHandler` devolve sempre `{ status, erro, mensagem, caminho, campos }`. Exemplo de e-mail repetido:
`409 { "mensagem": "E-mail já cadastrado no sistema." }`. O `shared/api/erro.js` lê `mensagem` (e a lista `campos` quando é erro de validação) e o hook coloca no `<Alert>` da tela.

| Status | Quando |
|---|---|
| 400 | corpo inválido ou validação (e-mail, senha curta, CPF, CEP) |
| 401 | e-mail ou senha errados no login |
| 401 | também: sem token, token vencido ou adulterado |
| 403 | perfil sem permissão para a rota |
| 404 | recurso não encontrado |
| 409 | e-mail, CPF ou CNPJ já cadastrados |
| 422 | regra de negócio violada |

---

## 7. O que já segue o SDD (Regras de Negócio dos Endpoints)

| Regra | Situação |
|---|---|
| RN-USR-01 Perfis CLIENTE/TECNICO/ADMIN, devolvidos no `UsuarioDto` | feito (`Perfil`, `Usuario.perfil`) |
| RN-USR-02 Autocadastro só cria CLIENTE | feito |
| RN-USR-03 Técnico/Admin criados por endpoint próprio, CPF obrigatório | feito com `POST /usuarios/colaboradores` só para ADMIN (token JWT) |
| RN-USR-04 CPF ou CNPJ, únicos | já existia, mantido |
| RN-USR-05 Senha mín. 6, hash BCrypt, login compara senha com 401 genérico | feito |
| Seção 9 Padrão de erros (`@RestControllerAdvice`) | feito para usuário (400, 401, 403, 404, 409, 422) |
| Seed: coluna `perfil` e senhas com BCrypt | feito (`data.sql`) |
| Corrigidos: `findbyUsuarioId` (impedia subir), `numeroSerie` não gravado, colunas do `data.sql` | feito |

**Ainda NÃO feito (fica para as próximas etapas):**
- **Token (JWT) e RN-USR-06** (identidade vem do token, 403 por perfil nos endpoints de chamado/orçamento). Hoje o login devolve o usuário sem token; a guarda do front é só de interface.
- **Chamado:** protocolo `PR` + 8 dígitos, técnico responsável, categoria, anexos, `visitaAgendadaEm`, transições em sequência (RN-CHM-10..20), filtro pelo status **atual** (hoje casa qualquer status do histórico), `LocalDateTime` no histórico.
- **Garantia** ligada a `EquipamentoUsuario` (hoje liga ao catálogo `Equipamento`) e cálculo da situação.
- **Catálogo:** fabricantes da Frigotec no seed (FrigoPro, Rational, ITW, Tramontina, Skymsen), flag de fabricante próprio, `GET /fabricantes/{id}/equipamentos`, `GET /usuarios/{id}/equipamentos`.
- **Orçamento:** validar status/perfil, `PATCH /orcamentos/{id}/resposta`.
- Trocar `BuscaEncontradaException` usada como "não encontrado" (SDD seção 9).
- No front, as telas de chamados, equipamentos, orçamentos, conversa e agenda ainda usam dados simulados (`entities/estado`).

## 8. Boas práticas aplicadas

**Front-end (FSD, conforme a Avaliação Continuada 2)**
- Camadas na ordem `app, pages, widgets, features, entities, shared`; importação só para baixo (ESLint reforça).
- Segmentos `ui`, `model`, `api` dentro de cada fatia; API pública no `index.js`.
- Regra de negócio fora do `shared`: a regra da senha está em `entities/usuario/model`; a chamada HTTP está em `entities/usuario/api`.
- Uma nova funcionalidade vira uma nova fatia em `features` (ex.: `cadastro-colaborador`), sem mexer nas outras.
- Componentes só desenham; hooks do `model` guardam estado e regras.
- Axios criado uma vez (`shared/api`), erros tratados em um lugar só (`erro.js`).
- Sessão em um lugar (`entities/usuario/model/sessao.js`), não espalhada pelo código.

**Back-end (Java + Spring + JPA)**
- Camadas Controller → Service → Repository; DTO separado da entidade; Mapper para conversão.
- Senha com BCrypt e nunca devolvida em DTO.
- Validação com Bean Validation (`@Valid`, `@Email`, `@CPF`, `@CNPJ`, `@Size`).
- Tratamento de erro centralizado (`@RestControllerAdvice`) com status HTTP corretos.
- Configuração sensível por variável de ambiente (`CHAVE_EQUIPE`, `MAIL_*`), nunca no código de produção.
- `@Transactional` nos serviços que gravam; JPA sem SQL manual.

## 9. Resumo do que foi alterado nesta etapa

**Back-end (modificados):** `pom.xml` (spring-security-crypto), `Usuario`, `UsuarioDto`, `UsuarioMapper`, `UsuarioService`, `UsuarioController`, `application.properties`, `data.sql`, mais as correções anteriores (`ChamadoRepository`, `ChamadoService`, `ChamadoController`, `ChamadoDto`/`ChamadoMapper`, `EquipamentoUsuarioDto`/`Mapper`/`Repository`, `EquipamentoService`/`Controller`, `FabricanteService`/`Controller`).
**Back-end (novos):** `entity/Perfil`, `dto/ColaboradorCadastroDto`, `config/SegurancaConfig`, `exception/GlobalExceptionHandler`, `CredenciaisInvalidasException`, `AcessoNegadoException`, `RegraDeNegocioException`.
**Front-end (novos):** `entities/usuario/{api,model}`, `features/cadastro-colaborador`, `features/{login,cadastro}/model`, `pages/cadastro-colaborador`, `app/routes/RotaProtegida.jsx`, `shared/api/erro.js`, e as APIs/adaptadores de `chamado`, `equipamento`, `orcamento` (ainda sem uso nas telas).
**Front-end (modificados):** `LoginForm`, `CadastroForm`, `state.jsx`, `routes/index.jsx`, `HeaderApp`, `AppHeader`, `SidebarApp`, `home-cliente`, `vite.config.js` (proxy), `package.json` (script `dev`), `shared/api/http.js`.

Nada disso foi commitado nem enviado ao GitHub.

---

## 10. Token JWT e regras do chamado (atualização)

### 10.1 O que é o token e quem o usa
1. `POST /usuarios/login` confere e-mail e senha (BCrypt) e devolve `{ token, expiraEm, perfil, usuario }`. O token é gerado por `TokenService` (HS256, 120 min por padrão) com o id do usuário em `sub` e o perfil na claim `perfil`.
2. O front guarda tudo em `localStorage` (`frigotec.usuario`) e o interceptor de requisição do axios (`shared/api/http.js`) coloca `Authorization: Bearer <token>` em toda chamada.
3. No back, o `SegurancaConfig` (Spring Security + resource server) valida a assinatura e a validade, transforma a claim `perfil` em `ROLE_<perfil>` e aplica as regras por rota. Os controllers recebem o usuário com `@AuthenticationPrincipal Jwt` e `AutorAutenticado.de(jwt)`; **nenhum id de usuário é lido do corpo da requisição**.
4. Token vencido/adulterado → 401 → o interceptor de resposta encerra a sessão e a `RotaProtegida` leva ao `/login`.
5. Em produção defina `JWT_SECRET` (≥ 32 caracteres) e `APP_AMBIENTE=prod`; `SEED_MODE=never` desliga as senhas conhecidas do seed. O app **não sobe** com a chave de desenvolvimento fora de `dev`.

### 10.2 Quem pode o quê (SegurancaConfig + services)
| Rota | Perfil | Regra extra no service |
|---|---|---|
| `POST /usuarios`, `POST /usuarios/login`, `GET /fabricantes` | público | — |
| `/usuarios/colaboradores` (POST/GET) | ADMIN | — |
| `POST /fabricantes`, `POST /equipamentos` (catálogo) | TECNICO, ADMIN | modelo único por marca |
| `POST /equipamentos/{idUsuario}`, `POST /chamados` | CLIENTE | o id do caminho/dono é o do token |
| `GET /usuarios/{id}/equipamentos`, `GET /chamados/usuarios/{id}` | CLIENTE (próprio), equipe (qualquer) | 403 se o cliente pedir de outro |
| `GET /chamados`, `/chamados/status/{s}`, `/chamados/tecnicos/{id}` | TECNICO, ADMIN | técnico só vê os próprios |
| `GET /chamados/{id}` | qualquer logado | cliente de outro dono recebe 404 |
| `PATCH /chamados/{id}/status` | conforme a transição (RN-CHM-11) | só o próximo status; senão 409 com o estado atual e o permitido |
| `POST /chamados/{id}/atribuicao` | TECNICO (para si), ADMIN (qualquer) | chamado atribuído só muda por redesignação com motivo |
| `PATCH /chamados/{id}/visita` | TECNICO responsável | data/hora futuras; a partir de APROVACAO_DO_SERVICO |
| `POST /chamados/{id}/encerramento` | TECNICO responsável, ADMIN | só em VISITA_AGENDADA e sem orçamento pendente |
| `POST /orcamentos` | TECNICO responsável, ADMIN | chamado em VISITA_AGENDADA, visita já passada, um PENDENTE por vez |
| `GET /orcamentos/{id}` | dono (cliente), equipe | cliente de outro dono recebe 404 |
| `PATCH /orcamentos/{id}/resposta` | CLIENTE dono | só PENDENTE; resposta definitiva |
| `PATCH /garantias/{id}/validacao` | TECNICO, ADMIN | VALIDADA exige data de fim ≥ hoje |

### 10.3 Modelo de dados do catálogo e do chamado
- `Fabricante.propria` (FrigoPro = true): só equipamentos dessa marca aceitam tipo de garantia, data da compra e nota fiscal; nas demais marcas esses campos retornam 422.
- `EquipamentoUsuario` é a entidade associativa (id próprio, número de série opcional e único por modelo). **Chamado e Garantia apontam para ela**, não para o catálogo.
- A situação da garantia (`ATIVA`, `EM_ANALISE`, `VENCIDA`, `RECUSADA`, `NAO_SE_APLICA`) é **calculada** pelo `GarantiaMapper` a partir de `validacao` e `dataFimGarantia`; não é gravada.
- `Chamado`: `protocolo` (PR + 8 dígitos, único), `tecnico`, `visitaAgendadaEm`. `Historico` guarda status, data **e hora** e o responsável, e nunca é alterado: **o status atual é o último registro**. `Redesignacao` audita a troca de técnico. `Encerramento` guarda motivo e descrição da solução.

### 10.4 Telas ligadas à API e o que continua simulado
- **Ligado ao backend:** login, cadastro (cliente e colaborador), meus equipamentos, cadastro de equipamento (marcas e modelos do catálogo), abrir chamado, lista/detalhe do chamado (cliente e técnico), atribuir, avançar etapa, aprovar serviço (cliente), agendar/reagendar visita, finalizar atendimento, análise de garantia.
- **Ainda simulado (sem endpoint no SDD):** conversa cliente↔técnico, avaliação do atendimento, cancelamento de chamado (RN-CHM-20, decisão pendente), upload de fotos/nota fiscal (anexos), e-mail ao mudar de status (`EmailEventListener` vazio).
- **Backend pronto, sem tela ainda:** criar orçamento (técnico) e responder orçamento (cliente) — as telas atuais de orçamento são derivadas da conversa simulada.

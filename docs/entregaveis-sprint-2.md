# Entregáveis da Sprint 2 — situação e evidências

Legenda: **OK** = existe e foi verificado · **RASCUNHO** = documento criado com os dados conhecidos; faltam dados que só o grupo ou a empresa têm (marcados A CONFIRMAR) · **PENDENTE** = depende de algo que não existe nos arquivos.

## Engenharia de Software

| Entregável | Situação | Onde está |
|---|---|---|
| Pesquisa de usabilidade (usa IA) | RASCUNHO | [pesquisa-de-usabilidade.md](pesquisa-de-usabilidade.md): avaliação heurística feita; teste com usuários com roteiro pronto e resultados a preencher |
| Protótipo em alta resolução, versão final | OK | [prototipo/](prototipo/README.md): link do Excalidraw, 21 capturas das telas, fluxo de navegação e o front-end em React integrado |
| Diagrama de solução de software — Container (usa IA) | OK | Slide de arquitetura (Canva, Sprint 2) |
| Planilha de arquitetura | OK | [Planilha_de_Arquitetura_Frigotec.xlsx](Planilha_de_Arquitetura_Frigotec.xlsx) (Guia de Engenharia preenchido, pontos críticos e endpoints) |
| Planilha de especificação (SDD) + arquivo MD (usa IA) | OK | SDD Word, `Briefing_Front-End_Portal-Frigotec.xlsx`, SDD de UI e de fluxo técnico em `.md` (pasta PI/SDD-Figma) |

## Programação Web

| Entregável | Situação | Evidência |
|---|---|---|
| Login JWT | OK | `POST /usuarios/login`; Spring Security resource server; testes |
| Swagger + exportação OpenAPI YAML | OK | `/swagger-ui.html`; `/v3/api-docs.yaml` público; cópia em [openapi.yaml](openapi.yaml) (32 operações) |
| CRUD com relacionamento | OK | Usuário → Equipamento → Garantia: POST, GET, PATCH, DELETE em `/equipamentos` (`EquipamentoCrudTest`) |
| 1 padrão de projeto | OK | Observer: `StatusChamadoAlteradoEvento` + `EmailEventListener` (`EmailObserverTest`) |

## Front-end acelerado com IA

| Entregável | Situação | Evidência |
|---|---|---|
| 5 telas integradas ao back | OK | Login/cadastro, meus equipamentos, cadastro de equipamento, abertura e detalhe de chamado, histórico, painel do técnico |
| 1 tela com dashboard em React integrada ao Spring Boot | OK | Painel do técnico: os 4 KPIs e as próximas visitas vêm de `GET /dashboard/tecnico`, calculados no banco (`DashboardTest`). A caixa “Conversas com mensagem nova” ainda é simulada (não é KPI) |

## Documentação do projeto e demais itens

| Entregável | Situação | Onde está |
|---|---|---|
| Documentação do projeto atualizada | OK | README da raiz, `backend/README.md`, [conformidade-sdd.md](conformidade-sdd.md) |
| DER | OK | [der/](der/README.md): imagem refeita a partir do código e script MySQL validado |
| ODS — revisado | PROPOSTA | Manter a ODS 12 (meta 12.5) e acrescentar ODS 9 (meta 9.4); ODS 8 (meta 8.3) opcional. Entrega nos slides |
| API externa | OK | JavaMail (e-mails de orçamento e de mudança de status). Bling nos próximos passos |
| Termo de representação | OK | `TERMO_DE_REPRESENTACAO_GRUPO-5_assinado.pdf` (Downloads): Lavínia, representante; Juan, suplente; 24/08/2026 a 25/06/2027 |
| Termo de cooperação | RASCUNHO | [documentos/Termo_de_Cooperacao_RASCUNHO.docx](documentos/Termo_de_Cooperacao_RASCUNHO.docx): faltam dados da empresa e assinaturas |
| Prospecção do tema + entrevista | RASCUNHO | [documentos/Prospeccao_do_Tema_e_Entrevista.docx](documentos/Prospeccao_do_Tema_e_Entrevista.docx): roteiro pronto; faltam as respostas do entrevistado |
| Abertura do projeto e fotos | RASCUNHO | [documentos/Abertura_do_Projeto.docx](documentos/Abertura_do_Projeto.docx): faltam as fotos |

## Corrigido nesta revisão

- OpenAPI YAML público, com título, versão e esquema Bearer.
- CRUD do equipamento com PATCH e DELETE (back-end e botão no front).
- E-mail de mudança de status (SDD RN-NOT-01..03) e link do e-mail pelo protocolo.
- Dashboard do técnico com dados reais (`GET /dashboard/tecnico`).
- Back-end alinhado ao DER: removidas as tabelas sem uso `roles` e `status`.
- Upload de arquivos de verdade (SDD RN-CHM-03 e RN-EUS-05), tela de editar equipamento, DER com tabela `anexo` e campos obrigatórios, CI no GitHub Actions e perfil MySQL.
- Testes de integração: de 10 para 25, todos passando.

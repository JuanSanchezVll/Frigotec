# Conformidade do back-end com o SDD

Base: SDD “Regras de Negócio dos Endpoints (MVP)”, versão 0.1 (02/11/2026). Situação conferida no código e nos testes de integração (`./mvnw test`, 20 testes).

| Regra | Situação | Onde |
|---|---|---|
| RN-USR-01/02/03 Perfis; autocadastro só CLIENTE; ADMIN cria colaboradores | OK | `Perfil`, `UsuarioService`, `POST /usuarios/colaboradores` |
| RN-USR-05 Senha com BCrypt; login falha com 401 sem revelar qual campo | OK | `UsuarioService` (“E-mail ou senha inválidos.”) |
| RN-USR-06 Identidade vem do token | OK | `AutorAutenticado.de(jwt)` em todos os controllers |
| RN-EQP-01/02 Marca e modelo únicos ignorando maiúsculas; 404 e 409 | OK | `existsByNomeIgnoreCase`, `existsByModeloIgnoreCaseAndFabricanteId` |
| RN-EQP-03/05 Indicador `propria` (FrigoPro) e seed de marcas | OK | `Fabricante.propria`, `data.sql` |
| RN-EQP-04 Listas para os selects | OK | `GET /fabricantes`, `GET /fabricantes/{id}/equipamentos` |
| RN-EUS-01/02/03 Unidade do cliente, várias do mesmo modelo, série única por modelo | OK | `EquipamentoService` |
| RN-EUS-04 Campos de garantia só para marca própria (422) | OK | `EquipamentoService` |
| RN-EUS-05 Upload de documentos (multipart, PDF/JPG/PNG) | **NÃO IMPLEMENTADO** | Hoje simulado no front |
| RN-EUS-06 Listagem com situação de garantia | OK | `GET /usuarios/{id}/equipamentos` |
| RN-GAR-01/02/03 Garantia por unidade, situação calculada, validação pela equipe | OK | `Garantia`, `GarantiaMapper`, `PATCH /garantias/{id}/validacao` |
| RN-CHM-01/04/05 Pertencimento, protocolo PR+8 dígitos, nasce ENVIADO | OK | `ChamadoService.salvar` |
| RN-CHM-03 Anexos do chamado (JPG, PNG, MP4) | **NÃO IMPLEMENTADO** | Hoje simulado no front |
| RN-CHM-10/11 Sequência e quem executa cada transição | OK | `ChamadoService.mudarStatus` (409 em transição inválida) |
| RN-CHM-12 Atribuição e redesignação com motivo | OK | `POST /chamados/{id}/atribuicao`, `Redesignacao` |
| RN-CHM-14 Visita futura, a partir de APROVACAO_DO_SERVICO | OK | `PATCH /chamados/{id}/visita` |
| RN-CHM-15 Encerramento com motivo e descrição | OK | `POST /chamados/{id}/encerramento` |
| RN-CHM-16/17/18 Histórico imutável com responsável e data-hora; filtros pelo status atual; finalizado só leitura | OK | `Historico`, `Chamado.statusAtual()` |
| RN-CHM-19 / RN-NOT-01..03 Evento após a gravação; e-mail com protocolo; falha não reverte | OK | `ChamadoService` publica; `EmailEventListener` envia (AFTER_COMMIT, assíncrono) |
| RN-CHM-20 Cancelamento pelo cliente | Decisão pendente do grupo | Simulado no front |
| RN-ORC-01..06,08,10 Orçamento: quem cria, após a visita, um pendente, resposta do cliente, e-mail, visibilidade | OK | `OrcamentoService`, `PATCH /orcamentos/{id}/resposta` |
| RN-ORC-09 Garantia ativa e orçamento (valor zero ou dispensa) | Decisão pendente do grupo | `@Positive` impede valor zero |
| Seção 9 Padrão de erros (400, 401, 403, 404, 409, 422) | OK | `GlobalExceptionHandler`, `SegurancaConfig` |

## Resumo

- Cumpridas: todas as regras do SDD que dependem só do código.
- Falta implementar: upload de arquivos (RN-EUS-05 e RN-CHM-03). É o único item técnico em aberto; hoje o front simula o envio.
- Dependem de decisão do grupo ou da Frigotec: cancelamento (RN-CHM-20), orçamento em garantia ativa (RN-ORC-09), fases entre visita e finalizado, o que o cliente aprova em “Aprovação do serviço” e o número de série obrigatório (seção 10 do SDD).
- Divergência de nomes do DER anterior: veja `docs/der/README.md`.

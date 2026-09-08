# 05 · Requisitos

Cada requisito aponta as regras de negócio que o originam
([`02-regras-de-negocio.md`](02-regras-de-negocio.md)) e a fase.

**Fase MVP** = consta da lista de 15 itens que a própria Frigotec declarou
(documento A, seção 9). **Fase 2** = evolução mapeada pelo cliente.

---

## Requisitos funcionais — Portal do Cliente

| ID | Requisito | Regras | Fase |
|---|---|---|---|
| RF-01 | Criar conta e autenticar | RN-80, RN-81 | MVP |
| RF-02 | Manter dados de contato | RN-81 | MVP |
| RF-03 | Cadastrar equipamento, informando tipo, marca e dado identificador | RN-03, RN-51 | MVP |
| RF-04 | Localizar o equipamento **por tipo ou por marca**, com filtro cruzado | RN-01, RN-02 | MVP |
| RF-05 | Formulário que **exige o dado identificador correto** conforme o tipo | RN-03, RN-13 | MVP |
| RF-06 | Anexar foto da etiqueta quando o tipo exigir | RN-04 | MVP |
| RF-07 | Abrir chamado com descrição obrigatória do defeito | RN-10, RN-11 | MVP |
| RF-08 | Anexar fotos e vídeo do problema | RN-12 | MVP |
| RF-09 | Solicitar atendimento em garantia — **apenas para marca própria** | RN-20 | MVP |
| RF-10 | Ao solicitar garantia, exigir data da compra, NF e anexo da NF | RN-21 | MVP |
| RF-11 | Exibir aviso de que a solicitação **não é aprovação** | RN-22 | MVP |
| RF-12 | Acompanhar o status do chamado | RN-47 | MVP |
| RF-13 | Aprovar ou reprovar orçamento | RN-44 | MVP |
| RF-14 | Consultar histórico dos próprios equipamentos | RN-53, RN-82 | MVP |
| RF-15 | Aceitar ou recusar oferta de garantia estendida | RN-30, RN-33 | Fase 2 |
| RF-16 | Consultar ou solicitar peças do seu equipamento | RN-62 | Fase 2 |
| RF-17 | Fornecer informações adicionais durante o atendimento | RN-47 | MVP |

---

## Requisitos funcionais — Painel Interno

| ID | Requisito | Regras | Fase |
|---|---|---|---|
| RF-20 | Autenticar com perfil de equipe | RN-80 | MVP |
| RF-21 | Listar chamados com filtros e ordenação | RN-40 | MVP |
| RF-22 | Fazer triagem: classificar, priorizar, atribuir responsável | RN-40 | MVP |
| RF-23 | Registrar diagnóstico, observações técnicas, peças e serviços necessários | RN-41 | MVP |
| RF-24 | Analisar documentação e **decidir** sobre a garantia | RN-22, RN-42 | MVP |
| RF-25 | Elaborar orçamento de serviço e/ou peças | RN-43 | MVP |
| RF-26 | Registrar serviço executado e peças utilizadas | RN-45 | MVP |
| RF-27 | Encerrar chamado | RN-46 | MVP |
| RF-28 | Consultar prontuário completo de qualquer equipamento | RN-53 | MVP |
| RF-29 | Validar e complementar dados técnicos informados pelo cliente | RN-52 | MVP |
| RF-30 | **Administrar o catálogo** de tipos, marcas e vínculos | RN-05 | MVP |
| RF-31 | Oferecer garantia estendida, registrando período e valor | RN-30, RN-31 | Fase 2 |
| RF-32 | Registrar aceite/recusa e vigência da garantia estendida | RN-31, RN-33 | Fase 2 |
| RF-33 | Indicar peças compatíveis a partir do diagnóstico | RN-60 | Fase 2 |
| RF-34 | Executar follow-up e manutenção preventiva | RN-48 | Fase 2 |

> **RF-30 costuma ser esquecido.** Sem tela de administração do catálogo, RN-05 não é
> atendida e a Frigotec depende da equipe de desenvolvimento para incluir uma marca.
> Isso mata o sistema quando o projeto acadêmico terminar.

---

## Requisitos funcionais — Sistema

| ID | Requisito | Regras | Fase |
|---|---|---|---|
| RF-40 | Manter trilha de mudanças de status do chamado | rastreabilidade (doc. A) | MVP |
| RF-41 | Calcular a situação de garantia do equipamento a partir dos registros e da vigência | RN-23 | MVP |
| RF-42 | Preservar o histórico do equipamento após o encerramento do chamado | RN-46, RN-56 | MVP |
| RF-43 | Isolar dados: cliente só acessa o que é seu | RN-82 | MVP |
| RF-44 | Armazenar anexos (imagem, vídeo, PDF) com vínculo ao chamado/equipamento | RN-12, RN-21 | MVP |
| RF-45 | Estrutura preparada para integração com o Bling | RN-73 | MVP |
| RF-46 | Integração efetiva com a API do Bling | RN-72 | Fase 2 |
| RF-47 | Notificações por e-mail / WhatsApp | doc. A, seção 10 | Fase 2 |
| RF-48 | Dashboards de manutenção, garantias e recorrência de defeitos | doc. A, seção 10 | Fase 2 |
| RF-49 | Indicadores de tempo de atendimento e produtividade | doc. A, seção 10 | Fase 2 |

---

## Requisitos não funcionais

O documento A cita três prioridades textuais: **segurança, rastreabilidade e
possibilidade de evolução**. Os demais são propostos pela equipe.

| ID | Requisito | Origem |
|---|---|---|
| RNF-01 | **Segurança** — senhas com hash, controle de acesso por perfil, isolamento entre clientes | 📄 documento A |
| RNF-02 | **Rastreabilidade** — toda mudança de status registra quem, quando e o quê | 📄 documento A |
| RNF-03 | **Evolutibilidade** — catálogo administrável, sem regra de negócio codificada em constante | 📄 documento A + RN-05 |
| RNF-04 | Aplicação **web**, com front-end, back-end, banco de dados, autenticação e APIs | 📄 documento A |
| RNF-05 | Acessível pelo **domínio disponibilizado pela empresa** | 📄 documento A |
| RNF-06 | Upload de imagem e **vídeo** — exige limite de tamanho e política de armazenamento | 🔗 de RN-12 |
| RNF-07 | Responsivo — o cliente provavelmente abre o chamado do celular, na cozinha | 🔗 proposta da equipe |
| RNF-08 | Dados pessoais e documentos fiscais sob LGPD | 🔗 proposta da equipe |
| RNF-09 | Custo zero de infraestrutura (projeto acadêmico) | 🔗 restrição do projeto |

> **RNF-06 merece atenção.** Vídeo é o requisito mais caro do material: pesa, exige
> política de retenção e encarece qualquer hospedagem gratuita. Definir limite antes
> de implementar. Ver P-09.

> **RNF-07 não está escrito**, mas o cenário de uso sugere fortemente: o defeito
> acontece na cozinha, no meio do serviço, e a foto da etiqueta é tirada com o celular.
> Confirmar com a Frigotec (P-10).

---

## Cobertura do MVP declarado pelo cliente

Os 15 itens da seção 9 do documento A, mapeados:

| # | Item declarado | Requisitos |
|---|---|---|
| 1 | Autenticação e perfis | RF-01, RF-20 |
| 2 | Cadastro de clientes | RF-01, RF-02 |
| 3 | Cadastro de equipamentos pelo cliente | RF-03, RF-04, RF-05 |
| 4 | Upload de documentos e imagens | RF-06, RF-08, RF-44 |
| 5 | Abertura e gestão de chamados | RF-07, RF-21 |
| 6 | Status e acompanhamento | RF-12, RF-40 |
| 7 | Triagem e atribuição | RF-22 |
| 8 | Registro de diagnóstico | RF-23 |
| 9 | Análise / registro de garantia | RF-09, RF-10, RF-11, RF-24, RF-41 |
| 10 | Orçamento e aprovação | RF-13, RF-25 |
| 11 | Registro de serviço e peças | RF-26 |
| 12 | Encerramento | RF-27 |
| 13 | Histórico por cliente e equipamento | RF-14, RF-28, RF-42 |
| 14 | Painel interno básico | RF-21, RF-28 |
| 15 | Preparado para o Bling | RF-45 |

✅ **Cobertura completa** — os 15 itens estão endereçados.

**Dois requisitos do MVP que o cliente não listou, mas são necessários:**

| RF | Por quê |
|---|---|
| **RF-30** — administrar catálogo | RN-05 é textual; sem tela, não é atendida |
| **RF-43** — isolamento entre clientes | segurança básica; RNF-01 é textual |

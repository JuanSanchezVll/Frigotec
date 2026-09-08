# 01 · Briefing consolidado

> Síntese dos três documentos entregues pela Frigotec na reunião.
> Tudo aqui é **[FATO]** — extraído dos documentos. Nada foi inferido.
> Interpretações e lacunas estão marcadas e ficam em
> [`06-lacunas-e-perguntas.md`](06-lacunas-e-perguntas.md).

**Fontes** (originais em [`../00-fontes/`](../00-fontes/)):

| # | Documento | O que traz |
|---|---|---|
| A | `Portal_Frigotec_Dor_e_Proposta_Inicial.docx` | Contexto, dor, conceito do sistema, jornada, MVP, evoluções |
| B | `Orientações para o formulário de abertura de chamado.pdf` | Regras detalhadas do formulário, garantia Frigopro, garantia estendida |
| C | `MAQUINA QUE TRABALHAMOS.pdf` | Tabela de equipamentos × marcas × dado identificador |

---

## 1. Quem é o cliente

**Frigotec** — empresa do grupo que atua com **assistência técnica, manutenção e
comercialização de peças de reposição** para equipamentos profissionais.

- Atende equipamentos **Frigopro** (marca do grupo) e de **diversas outras marcas**
- **A Frigo encaminha para a Frigotec** clientes que precisam de suporte técnico,
  manutenção ou peças
- Atende equipamentos **comprados em outras empresas**, não só os vendidos pelo grupo
- Usa o **Bling** como ERP

> A Frigo (varejo) e a Frigotec (assistência) são empresas distintas do mesmo grupo.
> O projeto é **da Frigotec**.

---

## 2. A dor real — nas palavras do cliente

> *"A necessidade não é apenas registrar uma manutenção. O problema é centralizar e
> acompanhar a relação entre cliente, equipamento e Frigotec ao longo do tempo."*

Desdobramentos citados no documento A:

| # | Dor |
|---|---|
| D1 | O cliente não tem canal organizado para cadastrar equipamento, solicitar assistência e acompanhar o atendimento |
| D2 | A equipe não tem um ambiente único para receber, organizar, priorizar, atualizar e encerrar chamados |
| D3 | Não há histórico por equipamento (defeitos, diagnósticos, serviços, peças, atendimentos anteriores) |
| D4 | Quando há garantia, falta como analisar documentos e controlar vigência |
| D5 | Depois do atendimento, falta base para manter relacionamento (preventiva, peças, garantia estendida, recompra) |
| D6 | Hoje a informação fica dispersa entre ERP, contatos, documentos e atendimento |

---

## 3. O que a Frigotec pediu

**Um Portal de Pós-Venda, Garantias e Assistência Técnica**, acessível pelo domínio
da empresa, com **dois ambientes**:

| Ambiente | Para quem |
|---|---|
| **Portal do Cliente** | Cliente proprietário do equipamento |
| **Painel Interno** | Equipe da Frigotec |

**Objeto central declarado:** *"o equipamento cadastrado pelo cliente e todo o seu
histórico de relacionamento com a assistência técnica"*.

Esse ponto é decisivo para a modelagem: o sistema **não gira em torno do chamado**,
gira em torno do **equipamento**. O chamado é um evento na linha do tempo dele.

---

## 4. O prontuário do equipamento

O documento A pede explicitamente uma **ficha por equipamento, funcionando como
prontuário técnico**, criada com dados do cliente e enriquecida pela Frigotec a cada
atendimento. Conteúdo listado:

- Proprietário / cliente atual
- Marca e modelo
- Número de série, quando informado
- Data e origem da compra
- Documentos e fotos
- Situação de garantia
- Chamados abertos e encerrados
- Diagnósticos
- Serviços executados
- Peças utilizadas ou recomendadas
- Orçamentos
- Histórico de manutenção
- Próximas ações de pós-venda

> **"Proprietário/cliente atual"** implica que o equipamento pode **trocar de dono**.
> Isso é uma pista de modelagem: a relação cliente↔equipamento é histórica, não fixa.
> Ver pergunta P-07 em [`06-lacunas-e-perguntas.md`](06-lacunas-e-perguntas.md).

---

## 5. O formulário de abertura de chamado (documento B)

É a parte mais especificada de todo o material. Regras:

### 5.1 Seleção do equipamento — filtro cruzado bidirecional

O cliente localiza o equipamento **por tipo** (ex.: Liquidificador, Máquina de Gelo,
Chapa) **ou por marca** (ex.: Frigopro, Skymsen, CAF). Ao escolher um, o sistema
mostra **somente as opções correspondentes** do outro:

```
  seleciona MARCA     ->  lista só os TIPOS atendidos para aquela marca
  seleciona TIPO      ->  lista só as MARCAS atendidas para aquele tipo
```

### 5.2 Dado identificador varia por equipamento

A coluna **"Observação"** do documento C define **o que a Frigotec precisa** para
identificar cada equipamento. Não é o mesmo para todos:

| Dado exigido | Quantos tipos |
|---|---|
| Etiqueta / nº de série | 15 |
| Modelo | 4 |
| Marca | 1 |

Isso significa **formulário dinâmico**: o campo obrigatório muda conforme o tipo
escolhido. Ver o catálogo estruturado em [`../../dados/`](../../dados/).

### 5.3 Anexos

- **Foto da etiqueta** do equipamento — quando necessária
- **Descrição do defeito/problema** — obrigatória
- **Fotos** e, se possível, **vídeo** do problema

---

## 6. Garantia Frigopro — regra específica

Quando a marca selecionada for **Frigopro**, o cliente pode indicar que está
**solicitando atendimento em garantia**. Nesse caso são exigidos também:

- Data da compra
- Nota fiscal / comprovante de compra
- **Anexo** da nota fiscal
- Identificação do equipamento conforme a tabela

> ⚠️ **Regra crítica, textual no documento B:**
> *"A solicitação de garantia feita pelo cliente **não significa que a garantia foi
> aprovada**. A validação será realizada posteriormente pela equipe da Frigotec."*

O sistema precisa separar claramente **solicitação** de **aprovação** — são dois
estados distintos, com atores distintos.

A Frigotec também atende normalmente:
- equipamentos Frigopro **fora da garantia**
- equipamentos das **demais marcas**, mesmo comprados em outras empresas

---

## 7. Garantia estendida

Fluxo **inverso** ao da garantia comum — quem oferece é a Frigotec, não o cliente:

> *"Essa oferta é feita pela Frigotec durante o processo de atendimento, não pelo
> cliente na abertura do chamado."*

O sistema precisa registrar:

| Campo |
|---|
| Se foi oferecida |
| Período da extensão |
| Valor |
| Aceite ou recusa do cliente |
| Vigência da nova garantia |

---

## 8. O catálogo vai mudar — requisito estrutural

Ponto explícito e de impacto arquitetural direto:

> *"Essa relação poderá mudar no futuro, com inclusão ou retirada de equipamentos e
> marcas. Portanto, é importante considerar essa necessidade na solução proposta."*

**Consequência:** tipos de equipamento, marcas, o vínculo entre eles e o dado
identificador exigido são **dados administráveis em banco**, com tela de manutenção
no painel interno. Nunca `enum`, nunca constante em código, nunca `if` por marca.

---

## 9. Papel do Bling (ERP)

- O Bling **continua sendo o ERP** da Frigotec
- O portal **não deve duplicar** funções financeiras, fiscais, de estoque ou faturamento
- O projeto **deve estudar a integração** para consultar ou sincronizar informações
  relevantes, respeitando as possibilidades da API e as regras da empresa
- O portal é o sistema **especializado em assistência e relacionamento pós-venda**

> Redação do documento é deliberadamente cautelosa: *"estudar a integração"*,
> *"quando tecnicamente possível"*. Integração é **objetivo de estudo**, não
> promessa de entrega. Tratar como risco, não como requisito fechado.

---

## 10. MVP declarado pelo cliente

Lista textual do documento A, seção 9 — 15 itens:

1. Autenticação e perfis de acesso (cliente e equipe Frigotec)
2. Cadastro de clientes
3. Cadastro de equipamentos pelo cliente
4. Upload de documentos e imagens
5. Abertura e gestão de chamados
6. Status e acompanhamento do chamado
7. Triagem e atribuição interna
8. Registro de diagnóstico
9. Análise / registro de garantia
10. Orçamento e aprovação do cliente
11. Registro do serviço e peças utilizadas
12. Encerramento do chamado
13. Histórico por cliente e por equipamento
14. Painel interno básico para a equipe
15. Estrutura preparada para integração com o Bling

---

## 11. Evoluções mapeadas (fora do MVP)

Follow-up automático · lembretes de manutenção preventiva · garantia estendida ·
catálogo de peças compatíveis por equipamento · venda ou solicitação de peças ·
recompra e reposição · notificações por e-mail/WhatsApp · dashboards de manutenção,
garantias e recorrência de defeitos · indicadores de tempo de atendimento e
produtividade · integrações com outras plataformas do grupo.

> ⚠️ A **garantia estendida** aparece aqui como evolução, mas o documento B a
> especifica em detalhe. Contradição entre os documentos — ver P-01.

---

## 12. O que a Frigotec espera da equipe

Trecho relevante, porque muda o tipo de trabalho esperado:

> *"A equipe não receberá apenas uma lista fechada de telas. Espera-se que, a partir
> desta necessidade, ajude a transformar o processo em requisitos técnicos, proponha
> arquitetura, banco de dados, fluxos, interfaces e estratégia de integração."*

> *"Antes do desenvolvimento, o fluxo atual da Frigotec deverá ser levantado com as
> pessoas que executam o atendimento diariamente."*

Entrega esperada: **aplicação web funcional** com front-end, back-end, banco de dados,
autenticação, controle de acesso e APIs/integrações — priorizando **segurança,
rastreabilidade e possibilidade de evolução**.

### Duas obrigações que isso cria

| # | Obrigação |
|---|---|
| 1 | **Levantar o fluxo real com quem atende hoje** — está escrito, é entregável, e ainda não foi feito |
| 2 | Propor arquitetura e modelagem — não esperar especificação pronta |

---

## 13. Resultado esperado

> *"Transformar um atendimento pontual em relacionamento contínuo. A Frigotec passa a
> conhecer o histórico dos equipamentos que atende, o cliente ganha um canal
> organizado de suporte e a empresa cria uma base estruturada para garantia,
> manutenção, peças e pós-venda."*

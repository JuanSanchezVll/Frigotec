# 04 · Modelo de domínio

> **Proposta da equipe**, derivada das regras em [`02-regras-de-negocio.md`](02-regras-de-negocio.md).
> Não é especificação do cliente. Independe de tecnologia — vale para qualquer banco
> relacional. Ajustar quando a stack for definida.

---

## 1. O princípio que organiza tudo

> *"Objeto central do sistema: o equipamento cadastrado pelo cliente e todo o seu
> histórico de relacionamento com a assistência técnica."* — documento A, seção 3

Isso descarta a modelagem intuitiva, em que **Chamado** seria a entidade principal.
Aqui o **Equipamento** é o agregado central e o chamado é um **evento na linha do
tempo dele**. Consequência prática: o histórico sobrevive ao encerramento do chamado
e — se o equipamento trocar de dono — precisa sobreviver à troca de cliente também.

---

## 2. Entidades

### Núcleo

| Entidade | Papel |
|---|---|
| **Usuario** | Autenticação e perfil. Base para Cliente e Colaborador |
| **Cliente** | Proprietário de equipamentos |
| **Colaborador** | Integrante da equipe Frigotec. Faz triagem, diagnóstico, orçamento e encerramento |
| **Equipamento** | ⭐ Agregado central. É o prontuário |
| **Chamado** | Solicitação de assistência sobre um equipamento |

### Catálogo (administrável — RN-05)

| Entidade | Papel |
|---|---|
| **TipoEquipamento** | Liquidificador, Chapa, Fogão… (20 na carga inicial) |
| **Marca** | Skymsen, Frigopro, CAF… (29 na carga inicial) |
| **TipoEquipamentoMarca** | Vínculo N:N — quais marcas são atendidas para cada tipo (64 pares) |

### Ciclo de atendimento

| Entidade | Papel |
|---|---|
| **Diagnostico** | Avaliação técnica registrada no chamado |
| **Orcamento** | Proposta de serviço/peças, com aprovação do cliente |
| **ItemOrcamento** | Linha do orçamento — peça ou serviço |
| **ServicoExecutado** | O que foi efetivamente realizado |
| **PecaUtilizada** | Peças consumidas no atendimento |
| **HistoricoChamado** | Trilha de mudanças de status — rastreabilidade |

### Garantia

| Entidade | Papel |
|---|---|
| **SolicitacaoGarantia** | Pedido do cliente + documentos + decisão da Frigotec |
| **GarantiaEstendida** | Oferta da Frigotec + resposta do cliente |

### Apoio

| Entidade | Papel |
|---|---|
| **Anexo** | Arquivo genérico: foto da etiqueta, NF, foto/vídeo do defeito |
| **Peca** | Catálogo de peças (base para evolução comercial — RN-61) |

---

## 3. Diagrama de relacionamento

```
  ┌──────────────┐
  │   Usuario    │ 1
  └───────┬──────┘
          │ 1
   ┌──────┴───────┐
   │              │
┌──▼──────┐  ┌────▼────────┐
│ Cliente │  │ Colaborador │  (equipe Frigotec)
└────┬────┘  └──────┬──────┘
     │ 1            │ 1
     │              │ atribuído a
     │ N            │ N
┌────▼─────────────────────────┐        ┌──────────────────┐
│        EQUIPAMENTO           │ N ───┬─│ TipoEquipamento  │
│   ⭐ agregado central         │      │ └────────┬─────────┘
│                              │      │          │ N:N
│  · marca / modelo            │      │ ┌────────▼─────────┐
│  · nº série (opcional!)      │ N ───┴─│      Marca       │
│  · data e origem da compra   │        │  marcaPropria ☑  │
│  · situação de garantia      │        └──────────────────┘
└──────────┬───────────────────┘
           │ 1
           │ N
┌──────────▼───────────────────┐
│         CHAMADO              │
│  · status                    │───N──┐
│  · descrição do defeito      │      │
│  · prioridade                │      │
└──┬────────┬────────┬─────────┘      │
   │1       │1       │1               │N
   │N       │N       │N        ┌──────▼──────────────┐
┌──▼────┐ ┌─▼──────┐ ┌▼──────┐ │ HistoricoChamado    │
│Diagnós│ │Orçamento│ │Serviço│ │ (trilha de status)  │
│ tico  │ └─┬───────┘ │Execut.│ └─────────────────────┘
└───────┘   │1        └───┬───┘
            │N            │N
      ┌─────▼──────┐  ┌───▼────────┐
      │ItemOrçamen.│  │PecaUtilizada│
      └─────┬──────┘  └───┬─────────┘
            │N            │N
            └──────┬──────┘
              ┌────▼────┐
              │  Peca   │
              └─────────┘

  ┌──────────────────────┐        ┌──────────────────────┐
  │ SolicitacaoGarantia  │        │  GarantiaEstendida   │
  │ cliente PEDE →       │        │ Frigotec OFERECE →   │
  │ Frigotec DECIDE      │        │ cliente RESPONDE     │
  └──────────┬───────────┘        └──────────┬───────────┘
             │ 1:1 chamado                   │ N:1 equipamento
             └───────────┬───────────────────┘
                         │
                    ┌────▼─────┐
                    │  Anexo   │  NF, foto etiqueta, foto/vídeo defeito
                    └──────────┘
```

---

## 4. Atributos das entidades críticas

### Equipamento

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| id | UUID/serial | ✅ | |
| cliente_id | FK Cliente | ✅ | proprietário **atual** — ver nota abaixo |
| tipo_equipamento_id | FK | ✅ | |
| marca_id | FK | ✅ | |
| modelo | texto | condicional | obrigatório se o tipo exigir MODELO (RN-03) |
| numero_serie | texto | condicional | obrigatório se o tipo exigir NUMERO_SERIE; **pode faltar** (RN-54) |
| data_compra | data | ❌ | obrigatório só na solicitação de garantia (RN-21) |
| origem_compra | texto | ❌ | pode ter sido comprado em outra empresa (RN-06) |
| situacao_garantia | enum | ✅ | derivado — ver seção 5 |
| criado_por | enum | ✅ | CLIENTE / EQUIPE — quem cadastrou |
| dados_validados | booleano | ✅ | a equipe valida/complementa (RN-52) |

> **Sobre `cliente_id`:** RN-55 sugere que o equipamento pode trocar de dono. Se isso
> se confirmar (P-07), o correto é uma tabela `EquipamentoProprietario` com período de
> vigência, e não uma FK direta. **Decisão adiada até a resposta do cliente** — mas o
> código deve isolar esse acesso para permitir a troca sem refatoração ampla.

> **Sobre `numero_serie`:** não é chave, não é único, não é obrigatório para todos os
> tipos. Nunca usar como identificador do registro.

### Chamado

| Campo | Tipo | Obrigatório | Observação |
|---|---|---|---|
| id | UUID/serial | ✅ | |
| numero | texto | ✅ | número legível para o cliente (ex.: #1042) |
| equipamento_id | FK | ✅ | |
| aberto_por | FK Usuario | ✅ | |
| responsavel_id | FK Colaborador | ❌ | definido na triagem (RN-40) |
| status | enum | ✅ | ver máquina de estados em `03-atores-e-fluxos.md` |
| prioridade | enum | ❌ | definida na triagem |
| descricao_defeito | texto | ✅ | RN-11 |
| solicitou_garantia | booleano | ✅ | só pode ser `true` se marca própria (RN-20) |
| aberto_em / encerrado_em | timestamp | | |

### TipoEquipamento

| Campo | Tipo | Observação |
|---|---|---|
| id | | |
| nome | texto | "Liquidificador" |
| identificador_obrigatorio | enum | `NUMERO_SERIE` \| `MODELO` \| `MARCA` — dirige o formulário dinâmico (RN-03) |
| exige_foto_etiqueta | booleano | RN-04 |
| ativo | booleano | permite retirar do catálogo sem apagar histórico (RN-05) |

### Marca

| Campo | Tipo | Observação |
|---|---|---|
| id | | |
| nome | texto | |
| **marca_propria** | booleano | ⭐ dirige a regra de garantia (RN-20) — **não comparar com a string "Frigopro"** |
| ativo | booleano | RN-05 |

### SolicitacaoGarantia

| Campo | Tipo | Observação |
|---|---|---|
| chamado_id | FK | |
| data_compra | data | obrigatório (RN-21) |
| numero_nota_fiscal | texto | obrigatório (RN-21) |
| anexo_nf_id | FK Anexo | obrigatório (RN-21) |
| status | enum | `SOLICITADA` → `EM_ANALISE` → `APROVADA` \| `RECUSADA` (RN-22) |
| analisado_por | FK Colaborador | |
| justificativa | texto | obrigatória na recusa |

### GarantiaEstendida

| Campo | Tipo | Observação |
|---|---|---|
| equipamento_id | FK | |
| oferecida_por | FK Colaborador | RN-30 |
| oferecida_em | timestamp | |
| periodo_meses | inteiro | RN-31 |
| valor | decimal | RN-31 |
| resposta | enum | `PENDENTE` \| `ACEITA` \| `RECUSADA` (RN-33) |
| respondida_em | timestamp | |
| vigencia_inicio / vigencia_fim | data | RN-31 |

---

## 5. Situação de garantia — campo derivado, não digitado

`situacao_garantia` do equipamento **não deve ser um campo que alguém edita à mão**.
É resultado de:

```
  SEM_GARANTIA          nenhuma garantia registrada
  EM_ANALISE            existe SolicitacaoGarantia em aberto
  EM_GARANTIA           solicitação APROVADA e dentro da vigência
  GARANTIA_ESTENDIDA    GarantiaEstendida ACEITA e dentro da vigência
  GARANTIA_EXPIRADA     houve garantia, mas a vigência passou
```

Calcular a partir dos registros evita o problema clássico de um campo de status que
diverge dos fatos. RN-23 pede controle de **vigência** — ou seja, o valor muda com o
tempo, sozinho.

---

## 6. Decisões de modelagem já tomadas

| # | Decisão | Por quê |
|---|---|---|
| MD-01 | Catálogo (tipos, marcas, vínculo) em **tabela**, não em enum | RN-05 — a relação vai mudar |
| MD-02 | `Marca.marca_propria` como flag, não string `"Frigopro"` | RN-20 sobrevive à entrada de uma segunda marca própria |
| MD-03 | `identificador_obrigatorio` no **tipo**, dirigindo validação dinâmica | RN-03/RN-13 |
| MD-04 | Desativar (`ativo = false`) em vez de apagar itens do catálogo | preserva histórico de chamados antigos |
| MD-05 | `situacao_garantia` **derivada** | RN-23 — vigência muda com o tempo |
| MD-06 | Garantia comum e estendida em **entidades separadas** | fluxos e atores invertidos (RN-22 vs RN-30) |
| MD-07 | `HistoricoChamado` como trilha de status | "rastreabilidade" é requisito explícito do documento A |
| MD-08 | `Anexo` genérico com tipo, em vez de colunas de arquivo espalhadas | NF, etiqueta, foto e vídeo têm o mesmo ciclo de vida |

---

## 7. Carga inicial do catálogo

Já estruturada a partir do documento C:

| Arquivo | Conteúdo |
|---|---|
| [`../../dados/catalogo-equipamentos.json`](../../dados/catalogo-equipamentos.json) | 20 tipos, 29 marcas, vínculos e identificador exigido |
| [`../../dados/catalogo-equipamentos.csv`](../../dados/catalogo-equipamentos.csv) | 64 linhas — um par tipo×marca por linha |

Serve direto como *seed* do banco assim que a stack for definida.

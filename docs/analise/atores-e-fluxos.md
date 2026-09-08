# 03 · Atores, permissões e fluxos

---

## 1. Atores

Extraídos da tabela "Quem faz o quê" (documento A, seção 4) e do formulário (documento B).

| Ator | Tipo | O que faz |
|---|---|---|
| **Cliente** | Usuário externo | Cria conta, cadastra equipamento, anexa comprovantes, abre chamado, acompanha, aprova/reprova orçamento, aceita/recusa garantia estendida, consulta histórico e peças |
| **Equipe Frigotec** | Usuário interno | Triagem, atribuição, diagnóstico, análise de garantia, orçamento, registro de serviço e peças, encerramento, follow-up, oferta de garantia estendida |
| **Bling (ERP)** | Sistema externo | Fonte/destino de dados comerciais, fiscais e de estoque. Integração a estudar |

> **[EM ABERTO]** O documento trata "equipe Frigotec" como bloco único. Não define se
> há papéis internos distintos — atendente que faz triagem, técnico que diagnostica,
> gestor que aprova orçamento. Ver P-03.

---

## 2. Matriz de permissões — proposta inicial

Derivada da tabela "Quem faz o quê". **Precisa de validação com a Frigotec.**

| Ação | Cliente | Equipe Frigotec |
|---|:---:|:---:|
| Criar conta própria | ✅ | — |
| Manter dados de contato próprios | ✅ | 👁 consulta |
| Cadastrar equipamento | ✅ | ✅ complementa |
| Validar dados técnicos do equipamento | — | ✅ |
| Anexar nota fiscal e fotos | ✅ | ✅ |
| Analisar documentação e determinar garantia | — | ✅ |
| Abrir chamado | ✅ | ❓ ver P-04 |
| Classificar, priorizar e atribuir chamado | — | ✅ |
| Registrar diagnóstico e observações técnicas | — | ✅ |
| Elaborar orçamento | — | ✅ |
| Aprovar / reprovar orçamento | ✅ | — |
| Registrar serviço executado e peças | — | ✅ |
| Encerrar chamado | — | ✅ |
| Oferecer garantia estendida | — | ✅ |
| Aceitar / recusar garantia estendida | ✅ | — |
| Ver histórico do próprio equipamento | ✅ | ✅ |
| Ver histórico de qualquer equipamento | — | ✅ |
| Administrar catálogo de tipos e marcas | — | ✅ |
| Consultar / solicitar peças | ✅ | ✅ indica |

Legenda: ✅ executa · 👁 apenas consulta · ❓ indefinido

**Duas assimetrias de propósito no fluxo:**

1. **Garantia comum** — o cliente **solicita**, a Frigotec **decide** (RN-22)
2. **Garantia estendida** — a Frigotec **oferece**, o cliente **decide** (RN-30)

São fluxos espelhados. Confundir os dois é o erro de modelagem mais provável aqui.

---

## 3. Jornada principal — 11 passos do cliente

Textual do documento A, seção 5:

| # | Passo | Ator | Observação |
|---|---|---|---|
| 1 | Acessa o portal e cria sua conta | Cliente | |
| 2 | Cadastra o equipamento | Cliente | A Frigotec não necessariamente vendeu — é o cliente quem informa |
| 3 | Anexa comprovantes | Cliente | Quando necessários para análise de garantia |
| 4 | Abre chamado descrevendo o problema | Cliente | |
| 5 | Recebe o chamado, faz triagem, define responsável | Frigotec | |
| 6 | Registra diagnóstico e determina cobertura de garantia | Frigotec | |
| 7 | Gera orçamento de serviço e/ou peças | Frigotec | *Quando necessário* |
| 8 | Acompanha e aprova o orçamento | Cliente | |
| 9 | Executa o atendimento e registra o realizado | Frigotec | |
| 10 | Chamado é encerrado e compõe o histórico permanente | Sistema | |
| 11 | Habilita ações de pós-venda | Sistema | Preventiva, peças, novo atendimento, garantia estendida, recompra |

---

## 4. Máquina de estados do chamado — proposta

Derivada dos passos acima. **É proposta da equipe, não especificação do cliente.**

```
                    ┌──────────────┐
                    │   ABERTO     │  cliente abriu, aguarda triagem
                    └──────┬───────┘
                           │ equipe assume
                    ┌──────▼───────┐
                    │  EM TRIAGEM  │  classificação, prioridade, responsável
                    └──────┬───────┘
                           │
                    ┌──────▼───────────┐
                    │  EM DIAGNÓSTICO  │  técnico avalia
                    └──────┬───────────┘
                           │
                  ┌────────▼─────────┐
                  │ análise garantia │
                  └───┬──────────┬───┘
             coberto  │          │  não coberto
                      │          │
                      │   ┌──────▼──────────────┐
                      │   │ AGUARDANDO APROVAÇÃO│  orçamento enviado
                      │   └──┬───────────────┬──┘
                      │      │ aprova        │ reprova
                      │      │               │
                      │      │        ┌──────▼───────┐
                      │      │        │  REPROVADO   │
                      │      │        └──────┬───────┘
                      │      │               │
                 ┌────▼──────▼────┐          │
                 │  EM EXECUÇÃO   │          │
                 └────────┬───────┘          │
                          │                  │
                 ┌────────▼───────┐          │
                 │   ENCERRADO    │◄─────────┘
                 └────────┬───────┘
                          │
                 ┌────────▼───────────────┐
                 │ histórico do equipamento│  permanente
                 └────────────────────────┘
```

### Transições que os documentos não cobrem

| Situação | Status |
|---|---|
| Cliente desiste depois de aberto | ❓ não previsto |
| Chamado precisa de informação adicional do cliente | ❓ o passo 5 do cliente diz *"fornece informações adicionais"*, mas não há estado para isso |
| Peça sem disponibilidade — atendimento suspenso | ❓ não previsto |
| Reabertura de chamado encerrado | ❓ não previsto |
| Orçamento reprovado — encerra ou fica pendente? | ❓ não previsto |
| Prazo / SLA de atendimento | ❓ não previsto — mas "indicadores de tempo de atendimento" aparecem nas evoluções |

Todas em [`06-lacunas-e-perguntas.md`](06-lacunas-e-perguntas.md).

---

## 5. Fluxo do formulário de abertura — detalhado

O ponto mais especificado do material. Sequência:

```
 1. Cliente escolhe COMO quer localizar o equipamento
    ├── por TIPO   ──> lista de 20 tipos
    └── por MARCA  ──> lista de 29 marcas

 2. Sistema filtra o outro lado (RN-02)
    tipo escolhido  ──> só as marcas daquele tipo
    marca escolhida ──> só os tipos daquela marca

 3. Sistema exige o DADO IDENTIFICADOR daquele tipo (RN-03)
    ├── nº de série / etiqueta  (15 tipos)  + oferece upload da foto da etiqueta
    ├── modelo                  (4 tipos)
    └── marca                   (1 tipo — Refresqueira)

 4. Cliente descreve o DEFEITO  (obrigatório)
    + anexa fotos e vídeo  (opcional)

 5. SE marca == Frigopro:
    └── exibe opção "solicitar atendimento em garantia"
        └── SE marcado:
            ├── data da compra          (obrigatório)
            ├── nota fiscal             (obrigatório)
            ├── anexo da nota fiscal    (obrigatório)
            └── AVISO VISÍVEL: "a solicitação será analisada
                pela equipe e não representa aprovação"   (RN-22)

 6. Chamado criado com status ABERTO
```

### Observação de projeto

O passo 5 é **condicional à marca**, e a marca vem do catálogo administrável (RN-05).
Se amanhã a Frigotec passar a ter uma segunda marca própria, a regra "só Frigopro"
quebra. **Modelar como flag `marcaPropria` na entidade Marca**, não como comparação
literal com a string `"Frigopro"`.

O catálogo já foi gerado com esse campo:
[`../../dados/catalogo-equipamentos.json`](../../dados/catalogo-equipamentos.json).

---

## 6. Fluxo da garantia estendida

Espelhado, iniciado pela Frigotec:

```
  durante o atendimento
          │
  ┌───────▼──────────────────┐
  │ Frigotec avalia se o     │  RN-32: só "determinados equipamentos"
  │ equipamento é elegível   │        (critério não definido — P-02)
  └───────┬──────────────────┘
          │ elegível
  ┌───────▼──────────────────┐
  │ OFERECE                  │  registra: período + valor
  └───────┬──────────────────┘
          │
  ┌───────▼──────────────────┐
  │ cliente ACEITA ou RECUSA │  ambos são registro válido (RN-33)
  └───┬──────────────────┬───┘
      │ aceita           │ recusa
  ┌───▼──────────────┐   │
  │ nova vigência    │   │
  │ passa a valer    │   │
  └──────────────────┘   │
                    ┌────▼─────────────┐
                    │ registro da recusa│
                    └───────────────────┘
```

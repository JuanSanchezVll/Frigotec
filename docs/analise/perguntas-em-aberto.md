# 06 · Lacunas e perguntas para a Frigotec

O que os três documentos **não** respondem. Ordenado por impacto: as primeiras
bloqueiam modelagem ou desenvolvimento; as últimas dá para decidir sozinho e validar
depois.

> A própria Frigotec pediu isto no documento A, seção 11:
> *"Antes do desenvolvimento, o fluxo atual da Frigotec deverá ser levantado com as
> pessoas que executam o atendimento diariamente."*
> Esta lista é o roteiro dessa conversa.

---

## 🔴 Bloqueia modelagem — resolver antes de escrever schema

### P-01 · Garantia estendida entra no MVP?

**A contradição:** o documento A lista garantia estendida em *"evoluções posteriores"*
(seção 10). O documento B a especifica em detalhe, com os campos exigidos.

**Por que importa:** são duas entidades e um fluxo inteiro. Muda o tamanho do MVP.

**Perguntar:** *"A garantia estendida precisa estar na primeira entrega, ou pode ficar
para depois? O documento do formulário detalha bastante, mas a proposta lista como
evolução."*

---

### P-02 · Quais equipamentos são elegíveis à garantia estendida?

O documento B diz *"determinados equipamentos"* e não define o critério.

**Perguntar:** *"A garantia estendida vale para quais equipamentos? É por tipo, por
marca, por faixa de valor, ou vocês decidem caso a caso no atendimento?"*

**Impacto:** se for caso a caso, é um botão. Se for por regra, é uma configuração no
catálogo.

---

### P-03 · A equipe Frigotec tem papéis internos distintos?

Os documentos tratam "equipe Frigotec" como bloco único. Mas o fluxo sugere funções
diferentes: quem faz triagem, quem diagnostica, quem aprova orçamento, quem decide
garantia.

**Perguntar:** *"Quem faz a triagem é a mesma pessoa que faz o diagnóstico? Quem
aprova um orçamento antes de mandar para o cliente? Alguém precisa autorizar a decisão
de garantia?"*

**Impacto:** define se o controle de acesso tem 2 perfis ou 4-5.

---

### P-04 · A equipe pode abrir chamado em nome do cliente?

Cenário real e provável: o cliente liga em vez de usar o portal.

**Perguntar:** *"Se o cliente ligar ou aparecer na loja, alguém da equipe abre o
chamado por ele? Ou ele é orientado a abrir pelo portal?"*

**Impacto:** se sim, muda `aberto_por`, muda permissão e muda a tela de abertura.
Muito provável que a resposta seja sim — e isso vira requisito de MVP.

---

### P-05 · Cliente é pessoa física ou jurídica?

O documento fala em "cliente" sem qualificar. O segmento é cozinha profissional, o que
sugere CNPJ, mas o documento não diz.

**Perguntar:** *"Os clientes de assistência são empresas ou também pessoa física?
Precisamos guardar CNPJ, razão social, inscrição estadual?"*

**Impacto:** campos de cadastro e integração com o Bling.

---

### P-06 · Como identificar um equipamento sem número de série?

O documento A diz *"número de série, quando disponível"*. RN-54 confirma que pode
faltar. Mas então dois liquidificadores Skymsen do mesmo cliente ficam indistinguíveis.

**Perguntar:** *"Quando o equipamento não tem etiqueta legível, como vocês identificam
qual é qual? Dão um apelido, usam o local de instalação?"*

**Impacto:** pode exigir campo `apelido` ou `identificacao_interna`.

---

### P-07 · Um equipamento pode trocar de dono?

O prontuário registra *"proprietário/cliente atual"* — a palavra "atual" sugere que sim.

**Perguntar:** *"Acontece de vocês atenderem um equipamento que mudou de dono? O
histórico anterior deve ficar visível para o novo proprietário?"*

**Impacto:** FK simples versus tabela de propriedade com vigência. E questão de
privacidade: o novo dono não deveria ver dados comerciais do anterior.

---

## 🟠 Bloqueia desenvolvimento — resolver antes de implementar o fluxo

### P-08 · O que acontece quando o cliente reprova o orçamento?

Não previsto em nenhum documento.

**Perguntar:** *"Se o cliente não aprovar o orçamento, o chamado encerra? Fica
aguardando? Vocês cobram algo pela visita ou pelo diagnóstico?"*

---

### P-09 · Limite de tamanho e retenção de anexos

O documento B pede foto **e vídeo**. Nenhum limite é mencionado.

**Perguntar:** *"Que tamanho de vídeo faz sentido? Uns 30 segundos resolvem? E por
quanto tempo esses arquivos precisam ficar guardados?"*

**Impacto:** direto no custo de hospedagem — restrição real num projeto sem orçamento.

---

### P-10 · O cliente vai usar pelo celular?

Não está escrito. Mas o defeito acontece na cozinha e a foto da etiqueta é tirada com
o celular.

**Perguntar:** *"Vocês imaginam o cliente abrindo o chamado pelo computador ou pelo
celular?"*

---

### P-11 · Existe prazo ou SLA de atendimento?

As evoluções citam *"indicadores de tempo de atendimento"*, o que pressupõe uma meta.

**Perguntar:** *"Vocês trabalham com prazo de retorno? Existe diferença entre um
chamado urgente e um normal?"*

---

### P-12 · Onde o chamado vira Ordem de Serviço no Bling?

Ponto levantado pelo próprio representante em conversa anterior: aproveitar os dados
do chamado para abrir a OS automaticamente no Bling.

**Perguntar:** *"Hoje vocês abrem a OS no Bling manualmente? Em que momento do
atendimento? Faz sentido o portal já criar essa OS?"*

**Impacto:** define o escopo real da integração. Também exige credencial de API.

---

## 🟡 Dá para decidir e validar depois

| ID | Pergunta |
|---|---|
| P-13 | Peça é catálogo próprio ou vem do Bling? |
| P-14 | O orçamento precisa de validade (ex.: 15 dias)? |
| P-15 | O atendimento é em domicílio, em bancada, ou os dois? Muda o fluxo? |
| P-16 | Um chamado pode envolver mais de um equipamento? |
| P-17 | Existe garantia sobre o **serviço executado**, além da garantia do produto? |
| P-18 | Quem cadastra os colaboradores da Frigotec no sistema? |
| P-19 | Precisa de anexo de laudo técnico assinado? |
| P-20 | Qual o volume atual — quantos chamados por mês? |

> **P-20 parece secundária e não é.** Define se o painel precisa de busca e paginação
> sérias ou se uma lista simples resolve. Vale perguntar cedo.

---

## Dados que precisamos pedir

| # | O que | Para quê |
|---|---|---|
| 1 | **Fluxo real de atendimento**, levantado com quem atende | Exigência textual do documento A |
| 2 | Modelo da OS que usam hoje no Bling | Entender campos reais e planejar integração |
| 3 | Exemplo real de chamado (anonimizado) | Validar se o modelo cobre o caso concreto |
| 4 | Lista de colaboradores e funções | Definir perfis de acesso |
| 5 | Credencial de API do Bling — **só se a integração for confirmada** | RF-46 |
| 6 | Domínio disponibilizado pela empresa | RNF-05 |

---

## Como conduzir

- Enviar a pauta antes. Não chegar com 20 perguntas de surpresa.
- Priorizar os 🔴 — os 12 primeiros cabem em uma hora.
- Pedir para **ver** o processo, não só ouvir descrição. Como a Frigotec pediu:
  falar com *"as pessoas que executam o atendimento diariamente"*.
- Registrar em ata e atualizar [`02-regras-de-negocio.md`](02-regras-de-negocio.md),
  promovendo as regras 🔗 derivadas para 📄 textuais quando confirmadas.

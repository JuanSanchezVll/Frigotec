# 07 · Riscos

Escala: probabilidade × impacto no projeto.

---

## 🔴 Críticos

### R-01 · Construir sem levantar o fluxo real

**Probabilidade: alta.** A Frigotec pediu textualmente que o fluxo atual seja
levantado *"com as pessoas que executam o atendimento diariamente"*. Isso ainda não
aconteceu. A tentação de partir direto para o código é grande — o material recebido
parece completo.

Ele não é. Doze perguntas 🔴 e 🟠 seguem abertas
([`06-lacunas-e-perguntas.md`](06-lacunas-e-perguntas.md)).

**Mitigação:** tratar a conversa com a operação como **entregável da fase 1**, não
como formalidade. Nenhuma tabela é criada antes das perguntas P-01 a P-07.

---

### R-02 · Catálogo virar código

**Probabilidade: alta** — é o caminho mais fácil. Vinte tipos e vinte e nove marcas
cabem confortavelmente em dois `enum`, e funciona na demonstração.

Quebra na primeira vez que a Frigotec quiser incluir uma marca, depois que o semestre
terminar e o grupo tiver se dispersado.

**Mitigação:** RF-30 (tela de administração do catálogo) é item de MVP, não de
evolução. Foi decidido em MD-01.

---

### R-03 · Cliente entender que a garantia foi aprovada

**Probabilidade: média · Impacto: alto** — cria conflito comercial real para a
Frigotec, com o cliente cobrando um direito que não tem.

O documento B é explícito: *"A solicitação de garantia feita pelo cliente não
significa que a garantia foi aprovada."*

**Mitigação:** RF-11 — aviso visível na tela, não em letra miúda. Estado do pedido
sempre à mostra no acompanhamento. Nunca usar verde antes da aprovação.

---

### R-04 · A integração com o Bling não se confirmar

**Probabilidade: média.** Depende de credencial de API, de decisão interna da
Frigotec e de o que a API realmente permite. Nada disso está confirmado.

**Mitigação:** já endereçada pelo próprio cliente — RN-73 pede estrutura *preparada*,
não integração pronta. Manter RF-46 na Fase 2 e desenhar a camada de integração
isolada atrás de uma interface, para que a ausência dela não bloqueie nada.

---

## 🟠 Relevantes

| Risco | Prob. | Mitigação |
|---|---|---|
| **R-05** — Escopo crescer: garantia estendida, peças e catálogo comercial puxam o MVP para cima | Alta | RN-61 é explícito: *"não transformar o MVP em e-commerce"*. Resolver P-01 cedo |
| **R-06** — Vídeo estourar a hospedagem gratuita | Média | Definir limite (P-09) antes de implementar upload. Considerar só foto no MVP |
| **R-07** — Equipamento sem nº de série virar duplicata no cadastro | Média | Resolver P-06. Possível campo `apelido` |
| **R-08** — Modelar o chamado como entidade central, e não o equipamento | Média | Contraria RN-50 explicitamente. Já registrado em MD-01..MD-08 |
| **R-09** — Perfis internos mal dimensionados | Média | P-03. Desenhar autorização por permissão, não por perfil fixo |
| **R-10** — LGPD: NF, CNPJ e documentos fiscais em projeto acadêmico | Média | RNF-08. Nunca usar dado real de cliente em ambiente de teste |

---

## 🟡 Menores

| Risco | Mitigação |
|---|---|
| **R-11** — Logo sem versão vetorial nem fundo transparente | Pedido já registrado em [`../../design/identidade-visual.md`](../../design/identidade-visual.md) |
| **R-12** — Erro de transcrição na tabela de 64 linhas | Checklist de conferência em [`../../dados/README.md`](../../dados/README.md) |
| **R-13** — Time acadêmico com dedicação parcial | Fatiar por release; MVP declarado pelo cliente é a linha de corte |
| **R-14** — Stack ainda não definida | Modelo de domínio foi escrito sem depender de tecnologia |

---

## O risco que não aparece em matriz

> **O sistema ficar pronto e a Frigotec continuar atendendo por WhatsApp.**

É o desfecho mais comum deste tipo de projeto e não é falha técnica. Acontece quando o
portal exige mais esforço do que o canal informal que ele deveria substituir.

O ponto de atrito específico aqui: **abrir chamado pelo portal é mais trabalhoso do
que mandar mensagem.** O cliente precisa criar conta, cadastrar equipamento, achar o
tipo, achar a marca, digitar o número de série, tirar foto da etiqueta. Uma mensagem
no WhatsApp é uma frase.

**Duas mitigações concretas:**

1. **Resolver P-04.** Se a equipe puder abrir o chamado em nome do cliente, o portal
   captura o atendimento que chegou por telefone em vez de perdê-lo. Isso
   provavelmente é requisito de MVP, não de evolução.
2. **O segundo chamado tem que ser fácil.** O equipamento já está cadastrado — abrir
   um novo chamado sobre ele deve custar dois cliques e uma descrição. O esforço fica
   todo no primeiro cadastro, e o histórico é o que paga esse custo.

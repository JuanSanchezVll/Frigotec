<p align="center">
  <img src="docs/assets/logo-frigotec.png" alt="Frigotec" width="190">
</p>

<h1 align="center">Portal Frigotec</h1>

<p align="center">
  Pós-venda, garantias e assistência técnica para equipamentos de cozinha profissional
</p>

---

## Sobre

A Frigotec faz assistência técnica, manutenção e venda de peças de reposição para
equipamentos profissionais de cozinha. Atende a marca própria Frigopro e mais 28
marcas de terceiros — inclusive equipamentos que o cliente comprou em outro lugar.

Hoje esse atendimento acontece por telefone, WhatsApp e balcão. O registro fica
espalhado entre o ERP, conversas soltas e documentos avulsos. Não existe um lugar
onde a empresa consiga olhar um equipamento e ver tudo que já aconteceu com ele.

Este projeto é um portal web para resolver isso.

## O problema

A frase que resume, e que veio do próprio cliente:

> A necessidade não é apenas registrar uma manutenção. O problema é centralizar e
> acompanhar a relação entre cliente, equipamento e Frigotec ao longo do tempo.

Por isso o sistema não gira em torno do chamado. Gira em torno do **equipamento** —
cada um tem uma ficha própria, que vai acumulando defeitos, diagnósticos, serviços,
peças e garantias. O chamado é só um evento na linha do tempo dessa ficha.

## O que o portal faz

São dois ambientes.

**Portal do Cliente** — cadastra a conta e o equipamento, anexa nota fiscal e foto
da etiqueta, abre o chamado descrevendo o defeito, acompanha o andamento e aprova
ou reprova o orçamento.

**Painel da Equipe Frigotec** — recebe os chamados, faz triagem e atribuição,
registra diagnóstico, analisa garantia, monta orçamento, registra o serviço
executado e as peças usadas, encerra e mantém o histórico.

### Duas regras que definem o sistema

**A seleção do equipamento é um filtro cruzado.** O cliente busca por tipo
(liquidificador, chapa, máquina de gelo) ou por marca (Frigopro, Skymsen, CAF).
Ao escolher um lado, o sistema só mostra as opções válidas do outro. São 20 tipos,
29 marcas e 64 combinações — tudo em [`dados/`](dados/), como dado configurável,
porque a Frigotec já avisou que essa lista muda com o tempo.

**Pedir garantia não é ter garantia.** Quando a marca é Frigopro, o cliente pode
solicitar atendimento em garantia e anexar a nota fiscal. Isso abre uma
solicitação — quem decide é a equipe, depois, olhando os documentos. São dois
estados diferentes, com atores diferentes, e a interface precisa deixar isso claro
para não criar expectativa errada.

## Estrutura do repositório

```
docs/
  analise/       levantamento de requisitos, regras de negócio e modelagem
  diagramas/     storyboard e wireframe exportados
  assets/        logo e imagens usadas na documentação
dados/           catálogo de equipamentos e marcas atendidos (JSON e CSV)
prototipo/
  storyboard/    jornada do cliente em 9 quadros
  wireframe/     as 5 telas do fluxo, em baixa fidelidade
backend/         API — ainda não iniciado
frontend/        aplicação web — ainda não iniciado
```

## Documentação

| Arquivo | O que tem |
|---|---|
| [briefing.md](docs/analise/briefing.md) | Síntese do que o cliente pediu, separando fato de suposição |
| [regras-de-negocio.md](docs/analise/regras-de-negocio.md) | 49 regras catalogadas, cada uma com a fonte |
| [requisitos.md](docs/analise/requisitos.md) | Requisitos funcionais e não funcionais |
| [atores-e-fluxos.md](docs/analise/atores-e-fluxos.md) | Permissões, jornada e máquina de estados do chamado |
| [modelo-de-dominio.md](docs/analise/modelo-de-dominio.md) | Entidades e decisões de modelagem |
| [perguntas-em-aberto.md](docs/analise/perguntas-em-aberto.md) | O que ainda precisa ser respondido pelo cliente |
| [riscos.md](docs/analise/riscos.md) | Riscos mapeados e como pretendemos tratar |

## Protótipo

Os dois arquivos abrem direto no navegador, sem instalar nada.

- [`prototipo/storyboard/index.html`](prototipo/storyboard/index.html) — a jornada
  do cliente em 9 quadros, do equipamento quebrando até o histórico registrado
- [`prototipo/wireframe/index.html`](prototipo/wireframe/index.html) — home, login,
  cadastro, área do cliente e painel da equipe

As versões em imagem estão em [`docs/diagramas/`](docs/diagramas/).

## Integração com o ERP

A Frigotec usa o **Bling**. O portal não substitui e não duplica o que já está lá —
nada de financeiro, fiscal ou estoque. A ideia é consultar e sincronizar o que fizer
sentido, respeitando o que a API permite. Isso ainda está em estudo, não é promessa
de entrega.

## Situação atual

Estamos na fase de descoberta e modelagem. A análise dos documentos está fechada,
o storyboard e o wireframe estão prontos, e o catálogo de equipamentos já está
estruturado.

Ainda não começamos a codar. Antes disso falta uma reunião de levantamento com
quem faz o atendimento hoje na Frigotec — foi exigência do próprio cliente, e sete
das perguntas em aberto dependem dela para a modelagem do banco não sair errada.

A stack ainda não está fechada. O modelo de domínio foi escrito sem depender de
tecnologia, então serve para qualquer uma.

## Equipe

Davi Jeronimo · Guilherme Mesquita · Gustavo Mendes · Juan Sanches · Lavinia Todarelli

Projeto Integrador — Análise e Desenvolvimento de Sistemas.

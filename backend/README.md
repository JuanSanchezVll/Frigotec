# Backend

Ainda não iniciado.

A API vai expor os indicadores e o ciclo do chamado para o portal e para o painel
interno. O modelo de domínio já está definido em
[`../docs/analise/modelo-de-dominio.md`](../docs/analise/modelo-de-dominio.md) e não
depende de tecnologia — serve para a stack que o grupo escolher.

## Antes de começar

Duas coisas precisam estar resolvidas:

1. A reunião de levantamento com a equipe de atendimento da Frigotec. Sete das
   perguntas em aberto mudam o schema do banco.
2. A definição da stack.

## Regras que valem desde o primeiro commit

- O catálogo de tipos de equipamento e marcas é **dado em banco**, com tela de
  administração. Nunca `enum`, nunca constante em código. A Frigotec avisou que
  essa lista muda.
- A marca própria é uma **flag na entidade Marca**, não uma comparação com a string
  `"Frigopro"`. Se amanhã aparecer uma segunda marca do grupo, a regra de garantia
  não pode quebrar.
- Solicitação de garantia e aprovação de garantia são **estados distintos**.
- Valor monetário é decimal, nunca ponto flutuante.

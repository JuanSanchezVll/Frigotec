# 02 · Regras de negócio

Regras extraídas dos documentos, com identificador rastreável.
Cada regra aponta a **fonte** (A = docx da proposta, B = PDF do formulário,
C = PDF da tabela de máquinas) e a **origem** (textual = está escrito;
derivada = consequência lógica direta, marcada como tal).

| Origem | Significado |
|---|---|
| 📄 **Textual** | Está escrito no documento. Não é interpretação. |
| 🔗 **Derivada** | Consequência lógica direta de algo textual. Deve ser confirmada. |

---

## Catálogo de equipamentos e marcas

| ID | Regra | Fonte | Origem |
|---|---|---|---|
| **RN-01** | O cliente pode localizar o equipamento **pelo tipo** ou **pela marca** | B | 📄 |
| **RN-02** | Ao selecionar marca, o sistema exibe **somente** os tipos atendidos para ela; ao selecionar tipo, **somente** as marcas atendidas para ele | B | 📄 |
| **RN-03** | Cada tipo de equipamento define **qual dado identificador** é exigido: nº de série/etiqueta, modelo ou marca | B, C | 📄 |
| **RN-04** | Deve ser possível **solicitar foto da etiqueta** quando necessária | B | 📄 |
| **RN-05** | A relação de equipamentos e marcas **pode mudar** — inclusão e retirada devem ser suportadas sem alteração de código | B | 📄 |
| **RN-06** | A Frigotec atende equipamentos **comprados em outras empresas** | B | 📄 |
| **RN-07** | A Frigotec atende equipamentos Frigopro **fora da garantia** e de todas as demais marcas da relação | B | 📄 |
| **RN-08** | O vínculo tipo↔marca é **N:N** — um tipo tem várias marcas e uma marca atende vários tipos | C | 🔗 |

> **Verificado nos dados:** 20 tipos, 29 marcas, 64 pares. Skymsen e Frigopro
> aparecem em 6 tipos cada; 14 marcas aparecem em um único tipo.
> Ver [`../../dados/catalogo-equipamentos.json`](../../dados/catalogo-equipamentos.json).

---

## Abertura de chamado

| ID | Regra | Fonte | Origem |
|---|---|---|---|
| **RN-10** | O cliente deve informar **para qual equipamento** deseja assistência | B | 📄 |
| **RN-11** | A **descrição do defeito/problema** é obrigatória | B | 📄 |
| **RN-12** | O cliente pode anexar **fotos** e, se possível, **vídeo** | B | 📄 |
| **RN-13** | O dado identificador exigido no formulário **muda conforme o tipo** escolhido (formulário dinâmico) | B, C | 🔗 |
| **RN-14** | Quem abre o chamado é o **cliente**, pelo Portal do Cliente | A | 📄 |

---

## Garantia — solicitação pelo cliente

| ID | Regra | Fonte | Origem |
|---|---|---|---|
| **RN-20** | A opção de solicitar atendimento em garantia só aparece quando a marca for **Frigopro** | B | 📄 |
| **RN-21** | Ao solicitar garantia, são exigidos: **data da compra**, **nota fiscal/comprovante**, **anexo da NF** e **identificação do equipamento conforme a tabela** | B | 📄 |
| **RN-22** | ⚠️ **Solicitar garantia não é ter garantia aprovada.** A validação é feita depois pela equipe da Frigotec | B | 📄 |
| **RN-23** | A situação de garantia é atributo do **equipamento**, e a equipe precisa **controlar sua vigência** | A | 📄 |
| **RN-24** | Solicitação e aprovação de garantia são **estados distintos**, com **atores distintos** (cliente solicita, Frigotec decide) | B | 🔗 |

> **RN-22 é a regra mais fácil de implementar errado.** Se a interface deixar o
> cliente com a impressão de que a garantia foi concedida ao anexar a nota, o sistema
> cria um conflito comercial para a Frigotec. A tela precisa dizer, em texto visível,
> que o pedido será analisado.

---

## Garantia estendida — oferta pela Frigotec

| ID | Regra | Fonte | Origem |
|---|---|---|---|
| **RN-30** | A garantia estendida é **oferecida pela equipe da Frigotec**, durante o atendimento — **nunca solicitada pelo cliente na abertura** | B | 📄 |
| **RN-31** | O sistema deve registrar: **se foi oferecida**, **período**, **valor**, **aceite ou recusa** do cliente e **vigência** da nova garantia | B | 📄 |
| **RN-32** | A garantia estendida se aplica a **determinados equipamentos** — não a todos | B | 📄 |
| **RN-33** | Recusa do cliente também é registro válido — não é ausência de dado | B | 🔗 |

> **RN-32 está incompleta:** o documento não diz *quais* equipamentos são elegíveis,
> nem se o critério é por tipo, por marca ou caso a caso. Ver P-02.

---

## Ciclo do chamado

| ID | Regra | Fonte | Origem |
|---|---|---|---|
| **RN-40** | Após aberto, o chamado passa por **triagem** e recebe um **responsável** | A | 📄 |
| **RN-41** | A equipe registra **diagnóstico**, observações técnicas, peças e serviços necessários | A | 📄 |
| **RN-42** | A equipe determina se o atendimento **está ou não coberto por garantia** | A | 📄 |
| **RN-43** | Quando **não coberto**, a Frigotec gera **orçamento** de serviço e/ou peças | A | 📄 |
| **RN-44** | O cliente **aprova ou reprova** o orçamento pelo portal | A | 📄 |
| **RN-45** | A Frigotec executa o atendimento e registra **o que foi realizado** e **peças utilizadas** | A | 📄 |
| **RN-46** | Ao encerrar, o chamado **passa a compor o histórico permanente daquele equipamento** | A | 📄 |
| **RN-47** | O cliente **acompanha as atualizações** e pode fornecer informações adicionais durante o processo | A | 📄 |
| **RN-48** | Após o encerramento, o sistema habilita ações de pós-venda sobre aquele equipamento | A | 📄 |

---

## Equipamento e prontuário

| ID | Regra | Fonte | Origem |
|---|---|---|---|
| **RN-50** | O **equipamento é o objeto central** do sistema — não o chamado | A | 📄 |
| **RN-51** | É o **cliente quem cadastra** o equipamento e informa os dados, porque a Frigotec não necessariamente o vendeu | A | 📄 |
| **RN-52** | A equipe **valida e complementa** os dados técnicos informados pelo cliente | A | 📄 |
| **RN-53** | Cada equipamento tem uma **ficha própria** (prontuário) com todo o histórico acumulado | A | 📄 |
| **RN-54** | O número de série é registrado **"quando informado"** — ou seja, **pode faltar** | A | 📄 |
| **RN-55** | O prontuário registra o **proprietário/cliente atual**, o que implica possibilidade de **troca de dono** | A | 🔗 |
| **RN-56** | O chamado é encerrado, mas o histórico do equipamento é **permanente** | A | 🔗 |

> **RN-54 tem impacto direto no modelo.** O nº de série não serve como chave
> primária nem como identificador único obrigatório. Ver P-06.

---

## Peças e oportunidade comercial

| ID | Regra | Fonte | Origem |
|---|---|---|---|
| **RN-60** | A partir do equipamento e do diagnóstico, a Frigotec pode **identificar peças compatíveis** e apresentá-las ao cliente de forma contextualizada | A | 📄 |
| **RN-61** | O MVP **não** deve virar um e-commerce completo — a intenção é **estruturar os dados** para que a necessidade técnica gere oportunidade comercial | A | 📄 |
| **RN-62** | O cliente pode **consultar ou solicitar** peças relacionadas ao seu equipamento | A | 📄 |

---

## Integração com o Bling

| ID | Regra | Fonte | Origem |
|---|---|---|---|
| **RN-70** | O Bling **continua sendo o ERP** — o portal não o substitui | A | 📄 |
| **RN-71** | O portal **não deve duplicar** funções financeiras, fiscais, de estoque ou faturamento | A | 📄 |
| **RN-72** | A integração deve ser **estudada**, respeitando as possibilidades da API e as regras da empresa | A | 📄 |
| **RN-73** | A estrutura do MVP deve estar **preparada** para integração — a integração em si não é item do MVP | A | 📄 |

---

## Acesso e perfis

| ID | Regra | Fonte | Origem |
|---|---|---|---|
| **RN-80** | Existem no mínimo dois perfis: **cliente** e **equipe Frigotec** | A | 📄 |
| **RN-81** | O cliente **cria sua conta** e mantém seus dados de contato | A | 📄 |
| **RN-82** | O cliente só enxerga **seus próprios** equipamentos e chamados | — | 🔗 |
| **RN-83** | A equipe consulta e administra os cadastros necessários ao atendimento | A | 📄 |

> **RN-82 não está escrita em lugar nenhum**, mas é isolamento básico de dados entre
> clientes. Tratar como obrigatória e confirmar.

---

## Resumo por categoria

| Categoria | Regras | Textuais | Derivadas |
|---|---|---|---|
| Catálogo | 8 | 7 | 1 |
| Abertura de chamado | 5 | 4 | 1 |
| Garantia | 5 | 4 | 1 |
| Garantia estendida | 4 | 3 | 1 |
| Ciclo do chamado | 9 | 9 | 0 |
| Equipamento / prontuário | 7 | 5 | 2 |
| Peças | 3 | 3 | 0 |
| Bling | 4 | 4 | 0 |
| Acesso | 4 | 3 | 1 |
| **Total** | **49** | **42** | **7** |

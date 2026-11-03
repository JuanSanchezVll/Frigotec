# Pesquisa de usabilidade — Portal Frigotec

Esta pesquisa tem duas partes, e a diferença entre elas deve ficar clara na apresentação:

1. **Avaliação heurística com apoio de IA (feita).** Revisão do portal contra as 10 heurísticas de Nielsen, feita com IA a partir do código das telas e do SDD de UI. Aponta riscos e melhorias; **não é teste com usuários**.
2. **Teste com usuários (roteiro pronto, resultados a preencher).** O grupo aplica o roteiro com pessoas do perfil das personas e registra os resultados na tabela da seção 4. Nenhum resultado foi inventado.

## 1. Perfis avaliados (personas do SDD de UI)

- **Renata Moura, 41, proprietária de restaurante (PJ).** Cadastra o equipamento uma vez e volta para abrir chamados; quer saber em que etapa cada atendimento está.
- **Carlos Eduardo Lima, 52, cozinheiro autônomo (PF).** Não sabe onde fica o número de série; tira fotos do defeito com o celular.

## 2. Avaliação heurística (Nielsen)

| # | Heurística | O que o portal faz | Risco ou melhoria |
|---|---|---|---|
| 1 | Visibilidade do estado do sistema | Linha do tempo de 6 etapas no detalhe do chamado; selo de status no histórico; contadores no painel do técnico vindos do banco | Mostrar a data prevista da visita já no histórico |
| 2 | Correspondência com o mundo real | Linguagem do dia a dia (“Abrir seu chamado”, “Meus equipamentos”); protocolo no formato PR + 8 dígitos | Categorias do problema ainda dependem da validação da Frigotec |
| 3 | Controle e liberdade do usuário | Cliente corrige série e nota fiscal e pode excluir um equipamento | Cancelar chamado é simulado (decisão pendente, RN-CHM-20) |
| 4 | Consistência e padrões | Biblioteca de componentes única (botão, campo, selo, alerta, modal) e tokens de design | Menu da home logada difere do das telas internas (ponto levantado no wireframe) |
| 5 | Prevenção de erros | Excluir pede confirmação; transição de status só na ordem; série repetida e nota fiscal em marca sem garantia são recusadas pelo back-end | Validar CPF/CNPJ no cadastro com dígito verificador |
| 6 | Reconhecimento em vez de memorização | Seleção do equipamento por cards com marca, modelo e garantia | Lembrar a última marca escolhida no cadastro |
| 7 | Flexibilidade e eficiência | Cliente abre chamado direto do card do equipamento; técnico atribui da própria tela inicial | Atalhos de teclado para o técnico |
| 8 | Design estético e minimalista | Telas enxutas; formulário de chamado em duas etapas | Revisar densidade da tela de detalhe do técnico |
| 9 | Ajudar a reconhecer e corrigir erros | Mensagens do back-end (404, 409, 422) aparecem em um aviso no topo, em linguagem clara | Explicar ao cliente o que fazer após um 409 |
| 10 | Ajuda e documentação | Texto de ajuda no campo de série (onde achar a etiqueta) | Pequeno guia “como abrir um chamado” na home |

Acessibilidade (do SDD de UI): foco preso nos modais, Esc fecha, estados por texto + ícone + cor (não só cor), alvos de toque grandes e contraste definido nos tokens.

## 3. Roteiro do teste com usuários

Aplicar com 3 a 5 pessoas por perfil, uma de cada vez, sem ajudar. Medir tempo e erros; anotar frases.

| Tarefa | Critério de sucesso |
|---|---|
| T1. Criar uma conta como pessoa física (ou jurídica) | Conclui o cadastro sem ajuda |
| T2. Cadastrar um equipamento (FrigoPro com garantia) | Conclui e entende a situação da garantia |
| T3. Abrir um chamado para esse equipamento | Recebe o protocolo e entende o próximo passo |
| T4. Descobrir em que etapa o chamado está | Aponta a etapa correta na linha do tempo |
| T5. (Técnico) Pegar um chamado da fila e avançar o status | Atribui e avança sem ajuda |

Perguntas finais: o que foi mais difícil? Em algum momento ficou perdido? Nota de 1 a 5 para facilidade de uso.

## 4. Resultados do teste (preencher)

| Participante (perfil) | T1 | T2 | T3 | T4 | T5 | Nota (1–5) | Observações |
|---|---|---|---|---|---|---|---|
| A CONFIRMAR | | | | | | | |
| A CONFIRMAR | | | | | | | |
| A CONFIRMAR | | | | | | | |

## 5. Como foi usada a IA

A IA leu o SDD de UI, as personas e o código das telas e propôs a avaliação da seção 2 e o roteiro da seção 3. O grupo deve conferir cada linha da seção 2 abrindo a tela correspondente antes de apresentar.

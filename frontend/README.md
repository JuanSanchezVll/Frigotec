# Frontend

Ainda não iniciado.

As telas já estão desenhadas em baixa fidelidade no wireframe
([`../prototipo/wireframe/index.html`](../prototipo/wireframe/index.html)) — home,
login, cadastro, área do cliente e painel da equipe.

## Antes de começar

O wireframe precisa ser validado com a Frigotec. Enquanto isso não acontece, não
faz sentido partir para alta fidelidade.

## Pontos de atenção

- **O filtro cruzado é a peça mais fácil de errar.** Escolher o tipo filtra as
  marcas, escolher a marca filtra os tipos. Os 64 pares reais estão em
  [`../dados/catalogo-equipamentos.json`](../dados/catalogo-equipamentos.json).
- **O campo identificador muda conforme o tipo.** Quinze tipos pedem nº de série,
  quatro pedem modelo, um pede só a marca. O formulário é dinâmico.
- **O aviso de garantia precisa estar visível.** Ao marcar "solicitar atendimento
  em garantia", a tela tem que deixar claro, em texto, que o pedido será analisado
  e não representa aprovação.

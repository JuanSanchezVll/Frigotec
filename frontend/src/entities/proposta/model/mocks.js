// MOCK: itens iniciais da proposta de orçamento (valores fictícios)
export const itensIniciais = [
    { id: 1, descricao: 'Termostato industrial', qtd: 1, valor: 680 },
    { id: 2, descricao: 'Mão de obra e calibração', qtd: 2, valor: 300 },
];
export const brl = (n) => n.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

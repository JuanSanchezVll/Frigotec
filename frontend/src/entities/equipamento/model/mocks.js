export const equipamentosIniciais = [
    { id: 'chapa', tipo: 'Chapa', marca: 'Frigopro', serie: 'FP-2026-004512', garantia: 'Garantia em análise' },
    { id: 'gelo', tipo: 'Máquina de gelo', marca: 'Frigopro', serie: 'FP-2026-007781', garantia: 'Garantia ativa', validade: '18/11/2027' },
    { id: 'fritadeira', tipo: 'Fritadeira', marca: 'Skymsen', serie: 'SK-88213', garantia: 'Sem garantia' },
];
export const marcasLista = ['Frigopro', 'Skymsen', 'CAF', 'Vulcan', 'Venâncio', 'Metalcubas', 'Gelopar', 'Progás'];
export const modelosPorMarca = {
    Frigopro: ['Chapa', 'Máquina de gelo', 'Forno combinado', 'Câmara fria'],
    Skymsen: ['Fritadeira', 'Forno turbo'],
};
export const rotuloEquip = (e) => `${e.tipo} · ${e.marca}`;

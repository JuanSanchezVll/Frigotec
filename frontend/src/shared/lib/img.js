/** Fotos do Unsplash (alta resolução). Sem texto nem logos nas imagens. */
export const foto = (id, w = 2400) => `https://images.unsplash.com/photo-${id}?auto=format&fit=crop&q=85&w=${w}`;
export const FOTOS = {
    hero: { id: '1763219805214-91fa634e6006', alt: 'Chef preparando alimentos em uma chapa profissional de cozinha industrial' },
    cozinha: { id: '1589109807644-924edf14ee09', alt: 'Cozinha comercial com equipamentos e prateleiras de aço inox' },
    bancada: { id: '1708915965975-2a950db0e215', alt: 'Cozinha profissional com equipamentos de aço inox e bancada de madeira' },
    tecnico: { id: '1632657588888-7bc620e767e3', alt: 'Profissional operando um forno em cozinha de restaurante' },
    aco: { id: '1667892702884-faa077e80d7b', alt: 'Superfície de aço escovado com reflexo de luz diagonal' },
    chapa: { id: '1723744910476-19987e2bc32c', alt: 'Pessoa cozinhando em uma chapa em cozinha profissional' },
    panela: { id: '1627931085762-4017812f1773', alt: 'Panela de aço inox sobre fogão industrial' },
    cozinhaB: { id: '1663790776711-9283bf614ac2', alt: 'Cozinha com equipamentos de aço inox' },
    chef: { id: '1641536618779-b10c232ec2b2', alt: 'Chef preparando comida em uma cozinha' },
};

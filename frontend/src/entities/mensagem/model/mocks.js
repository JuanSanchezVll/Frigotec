const T = (id, de, hora, texto) => ({ id, de, hora, tipo: 'texto', texto });
export const threadsIniciais = {
    PR19283708: [
        T('m1', 'tecnico', '25/09/2026, 10:15', 'Olá, Gustavo. Vi o seu chamado da chapa. Segue a proposta de orçamento para o reparo do termostato.'),
        { id: 'm2', de: 'tecnico', hora: '25/09/2026, 10:16', tipo: 'proposta', titulo: 'Proposta de orçamento', detalhe: '2 itens · validade 7 dias', valor: 'R$ 1.280,00', validade: '7 dias', status: 'Pendente' },
        T('m3', 'cliente', '25/09/2026, 10:40', 'Recebi, obrigado. Vou analisar o orçamento. Pode agendar a visita?'),
        { id: 'm4', de: 'tecnico', hora: '25/09/2026, 11:05', tipo: 'agendamento', titulo: 'Agendamento de visita', data: '30/09/2026', periodo: 'Manhã', endereco: 'Rua das Palmeiras, 100, São Paulo', detalhe: '30/09/2026 · manhã · Rua das Palmeiras, 100, São Paulo', status: 'Pendente' },
    ],
    PR19276620: [
        T('n1', 'cliente', '25/09/2026, 15:10', 'Quando o técnico chega?'),
        { id: 'n2', de: 'tecnico', hora: '25/09/2026, 15:30', tipo: 'agendamento', titulo: 'Agendamento de visita', data: '01/10/2026', periodo: 'Tarde', endereco: 'Av. Brasil, 450, Campinas', detalhe: '01/10/2026 · tarde · Av. Brasil, 450, Campinas', status: 'Aceita' },
        { id: 'n3', de: 'tecnico', hora: '25/09/2026, 15:32', tipo: 'proposta', titulo: 'Proposta de orçamento', detalhe: '1 item · validade 7 dias', valor: 'R$ 2.150,00', validade: '7 dias', status: 'Pendente' },
        T('n4', 'cliente', '25/09/2026, 16:00', 'Combinado, aguardo o orçamento por aqui.'),
    ],
    PR19290415: [
        T('p1', 'cliente', '26/09/2026, 09:00', 'Posso levar o forno até a oficina?'),
        { id: 'p2', de: 'tecnico', hora: '26/09/2026, 09:20', tipo: 'agendamento', titulo: 'Agendamento de visita', data: '02/10/2026', periodo: 'Manhã', endereco: 'Av. das Indústrias, 1200, São Paulo', detalhe: '02/10/2026 · manhã · Av. das Indústrias, 1200, São Paulo', status: 'Pendente' },
        T('p3', 'cliente', '26/09/2026, 10:05', 'Vou confirmar o horário com a equipe e já respondo.'),
    ],
    PR19221305: [
        T('q1', 'tecnico', '23/09/2026, 14:00', 'O motor da coifa será trocado na visita. Confirmo o prazo por aqui.'),
        T('q2', 'cliente', '23/09/2026, 14:20', 'Obrigado, aguardo o prazo.'),
    ],
};

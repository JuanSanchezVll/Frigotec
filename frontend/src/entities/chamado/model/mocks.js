export const ETAPAS = ['Enviado', 'Aprovado pelo técnico', 'Contato do técnico', 'Aprovação do serviço', 'Visita agendada', 'Finalizado'];
export const statusDe = (c) => (c.cancelado ? 'Cancelado' : c.finalizado ? 'Concluído' : c.tecnico ? 'Em andamento' : 'Chamado enviado');
const descr = 'A chapa esquenta de forma desigual: um lado queima os alimentos e o outro fica frio.';
const g = 'Gustavo Mendes';
const D = 'Davi Jeronimo';
const J = 'Juan Sanchez';
const d25 = ['25/09/2026', '25/09/2026', '25/09/2026', '25/09/2026'];
export const chamadosIniciais = [
    { id: 'PR19283708', equipamento: 'Chapa · Frigopro', solicitante: g, data: '25/09/2026', abertura: '25/09/2026 às 20:00', atualizacao: '25/09/2026', categoria: 'Temperatura irregular', descricao: descr, etapa: 4, datas: d25, garantia: 'Garantia em análise', tecnico: D },
    { id: 'PR19271144', equipamento: 'Forno combinado', solicitante: g, data: '15/09/2026', abertura: '15/09/2026 às 09:30', atualizacao: '22/09/2026', categoria: 'Não liga', descricao: 'O forno não liga depois da limpeza.', etapa: 6, datas: ['15/09/2026', '15/09/2026', '16/09/2026', '17/09/2026', '19/09/2026', '22/09/2026'], garantia: 'Sem garantia', tecnico: D, finalizado: true, laudo: { resumo: 'Troca da placa de comando e teste de funcionamento.', pecas: 'Placa de comando', obs: 'Equipamento entregue funcionando.' } },
    { id: 'PR19260932', equipamento: 'Câmara fria', solicitante: g, data: '02/09/2026', abertura: '02/09/2026 às 14:10', atualizacao: '10/09/2026', categoria: 'Ruído', descricao: 'Ruído forte no compressor.', etapa: 6, datas: ['02/09/2026', '02/09/2026', '03/09/2026', '04/09/2026', '08/09/2026', '10/09/2026'], garantia: 'Sem garantia', tecnico: J, finalizado: true, laudo: { resumo: 'Fixação do compressor refeita.', pecas: 'Coxins de borracha', obs: '—' } },
    { id: 'PR19254071', equipamento: 'Fritadeira · Skymsen', solicitante: g, data: '24/09/2026', abertura: '24/09/2026 às 11:45', atualizacao: '24/09/2026', categoria: 'Vazamento', descricao: 'Vazamento de óleo na base.', etapa: 1, datas: ['24/09/2026'], garantia: 'Sem garantia' },
    { id: 'PR19239810', equipamento: 'Máquina de gelo · Frigopro', solicitante: g, data: '20/08/2026', abertura: '20/08/2026 às 16:20', atualizacao: '30/08/2026', categoria: 'Ruído', descricao: 'Ruído ao produzir gelo.', etapa: 6, datas: ['20/08/2026', '21/08/2026', '21/08/2026', '24/08/2026', '27/08/2026', '30/08/2026'], garantia: 'Garantia ativa', validade: '18/11/2027', tecnico: D, finalizado: true, laudo: { resumo: 'Limpeza do evaporador e troca do ventilador.', pecas: 'Ventilador do evaporador', obs: 'Recomendada limpeza a cada 3 meses.' } },
    { id: 'PR19221305', equipamento: 'Coifa', solicitante: g, data: '05/09/2026', abertura: '05/09/2026 às 08:05', atualizacao: '23/09/2026', categoria: 'Ruído', descricao: 'Motor da coifa com vibração.', etapa: 2, datas: ['05/09/2026', '06/09/2026'], garantia: 'Sem garantia', tecnico: J },
    // Chamados de outros clientes (só aparecem para o técnico)
    { id: 'PR19290415', equipamento: 'Forno combinado · Skymsen', solicitante: 'Ricardo Almeida', data: '26/09/2026', abertura: '26/09/2026 às 08:40', atualizacao: '26/09/2026', categoria: 'Não liga', descricao: 'O forno não acende o painel.', etapa: 1, datas: ['26/09/2026'], garantia: 'Sem garantia' },
    { id: 'PR19291877', equipamento: 'Câmara fria · Vulcan', solicitante: 'Marina Costa', data: '26/09/2026', abertura: '26/09/2026 às 09:15', atualizacao: '26/09/2026', categoria: 'Temperatura irregular', descricao: 'A câmara não passa de 8 graus.', etapa: 1, datas: ['26/09/2026'], garantia: 'Sem garantia' },
    { id: 'PR19276620', equipamento: 'Coifa industrial · CAF', solicitante: 'Carolina Teixeira', data: '21/09/2026', abertura: '21/09/2026 às 10:00', atualizacao: '25/09/2026', categoria: 'Ruído', descricao: 'Barulho metálico no exaustor.', etapa: 2, datas: ['21/09/2026', '22/09/2026'], garantia: 'Sem garantia', tecnico: D },
];
export const categorias = ['Temperatura irregular', 'Vazamento', 'Ruído', 'Não liga', 'Outros'];
export const TECNICO = D;

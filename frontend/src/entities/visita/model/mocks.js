export const SEMANA = ['28/09/2026', '29/09/2026', '30/09/2026', '01/10/2026', '02/10/2026', '03/10/2026'];
export const DIAS = ['Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];
export const horario = (p) => (p === 'Manhã' ? '09:00 às 12:00' : '14:00 às 17:00');
export const ehAgendamento = (m) => m.tipo === 'agendamento' && !!m.data;

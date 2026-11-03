// Rótulos fixos da interface (os dados dos chamados vêm do backend).
export const ETAPAS = ['Enviado', 'Aprovado pelo técnico', 'Contato do técnico', 'Aprovação do serviço', 'Visita agendada', 'Finalizado'];
export const statusDe = (c) => (c.cancelado ? 'Cancelado' : c.finalizado ? 'Concluído' : c.tecnico ? 'Em andamento' : 'Chamado enviado');
export const categorias = ['Temperatura irregular', 'Vazamento', 'Ruído', 'Não liga', 'Outros'];
// Nome mostrado na conversa (simulada) quando o chamado ainda não tem técnico.
export const TECNICO = 'Equipe Frigotec';

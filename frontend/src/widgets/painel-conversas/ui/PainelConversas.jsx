import { Link } from 'react-router-dom';
import { useApp } from '../../../entities/estado';
import { Avatar, SidePanel } from '../../../shared/ui';
/** Lista de conversas com mensagem recente, derivada das conversas do estado. */
export function useConversas(perfil) {
    const { chamados, threads } = useApp();
    return chamados
        .filter((c) => (threads[c.id]?.length ?? 0) > 0 && (perfil === 'tecnico' || c.solicitante === 'Gustavo Mendes') && !c.finalizado)
        .map((c) => {
        const t = threads[c.id];
        const u = t[t.length - 1];
        const outro = perfil === 'cliente' ? 'tecnico' : 'cliente';
        return { id: c.id, nome: perfil === 'cliente' ? c.tecnico ?? 'Técnico' : c.solicitante, previa: u.texto ?? u.titulo ?? '', hora: u.hora.split(', ')[1] ?? u.hora, naoLida: u.de === outro };
    });
}
export function PainelConversas({ perfil, aberto, onClose }) {
    const lista = useConversas(perfil);
    const base = perfil === 'cliente' ? '/chamados' : '/tecnico/chamados';
    return (<SidePanel aberto={aberto} onClose={onClose} titulo="Conversas em andamento">
      {lista.length === 0 && <p className="p-8 text-center text-texto-secundario">Nenhuma conversa em andamento.</p>}
      <ul>{lista.map((c, i) => (<li key={c.id}><Link to={`${base}/${c.id}/conversa`} onClick={onClose} className="flex min-h-20 items-center gap-3 border-b border-borda px-5 py-3 transition-colors hover:bg-superficie">
          <Avatar nome={c.nome} tom={i} tamanho={44}/>
          <span className="min-w-0 flex-1"><span className="flex justify-between"><span className="font-semibold">{c.nome}</span><span className="texto-legenda text-texto-secundario">{c.hora}</span></span><span className="num block texto-legenda text-primaria">{c.id}</span><span className="block truncate texto-pequeno text-texto-secundario">{c.previa}</span></span>
          {c.naoLida && <span aria-label="Não lida" className="h-3 w-3 rounded-full bg-primaria"/>}
        </Link></li>))}</ul>
    </SidePanel>);
}

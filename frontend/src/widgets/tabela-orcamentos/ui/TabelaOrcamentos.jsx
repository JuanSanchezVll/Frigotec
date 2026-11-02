import { MessageSquare } from 'lucide-react';
import { Badge, Button } from '../../../shared/ui';
export function TabelaOrcamentos({ lista }) {
    if (!lista.length)
        return <p className="py-16 text-center text-texto-secundario">Nenhuma proposta com este status.</p>;
    const th = 'px-5 py-4 texto-rotulo text-texto-secundario';
    return (<div className="overflow-x-auto rounded-lg border border-borda bg-fundo shadow-1">
      <table className="w-full min-w-[720px] text-left">
        <thead className="bg-superficie"><tr><th className={th}>Chamado</th><th className={th}>Cliente</th><th className={th}>Valor</th><th className={th}>Validade</th><th className={th}>Status</th><th className={`${th} text-right`}>Conversa</th></tr></thead>
        <tbody>{lista.map((o) => (<tr key={o.id} className="h-14 border-t border-borda transition-colors hover:bg-primaria-clara/50">
            <td className="num px-5 font-semibold text-primaria">{o.chamadoId}</td><td className="px-5">{o.cliente}</td><td className="num px-5 font-semibold">{o.valor}</td><td className="px-5 texto-pequeno text-texto-secundario">{o.validade}</td>
            <td className="px-5"><Badge texto={o.status}/></td>
            <td className="px-5 text-right"><Button to={`/tecnico/chamados/${o.chamadoId}/conversa`} variante="contorno" tamanho="sm"><MessageSquare size={16}/>Ver conversa</Button></td>
          </tr>))}</tbody>
      </table>
    </div>);
}

import { MessageSquare, Search } from 'lucide-react';
import { useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { useApp } from '../../entities/estado';
import { statusDe } from '../../entities/chamado';
import { AtribuirButton } from '../../features/atribuir-chamado';
import { Accordion, Avatar, Badge, Button, Segmented } from '../../shared/ui';
import { useLayout } from '../../shared/lib/layout-context';
/** Chamados do técnico: Novo, Meus chamados e Em acompanhamento, com busca e filtro por status. */
export default function TecnicoChamados() {
    const { novos, meus } = useApp();
    const { abrirConversas } = useLayout();
    const [busca, setBusca] = useState('');
    const [status, setStatus] = useState('todos');
    const [sp] = useSearchParams();
    const acomp = sp.has('acompanhamento');
    const filtra = (l) => l.filter((c) => `${c.id} ${c.solicitante} ${c.equipamento}`.toLowerCase().includes(busca.toLowerCase()) && (status === 'todos' || statusDe(c) === status));
    const th = 'px-5 py-4 texto-rotulo text-texto-secundario';
    const tabela = (l, acao) => {
        const f = filtra(l);
        if (!f.length)
            return <p className="p-8 text-center text-texto-secundario">Nenhum chamado aqui por enquanto.</p>;
        return (<div className="overflow-x-auto"><table className="w-full min-w-[760px] text-left">
        <thead className="bg-superficie"><tr><th className={th}>Número</th><th className={th}>Solicitante</th><th className={th}>Produto</th><th className={th}>Data da solicitação</th><th className={`${th} text-right`}>{acao ? 'Ações' : 'Status'}</th></tr></thead>
        <tbody>{f.map((c) => (<tr key={c.id} className="h-14 border-t border-borda transition-colors hover:bg-primaria-clara/50">
            <td className="num px-5"><Link to={`/tecnico/chamados/${c.id}`} className="font-semibold text-primaria hover:underline">{c.id}</Link></td>
            <td className="px-5"><span className="flex items-center gap-2"><Avatar nome={c.solicitante} tamanho={32} tom={1}/>{c.solicitante}</span></td>
            <td className="px-5">{c.equipamento}</td><td className="num px-5 texto-pequeno text-texto-secundario">{c.data}</td>
            <td className="px-5"><div className="flex justify-end gap-2">{acao ? (<><AtribuirButton id={c.id}/><Button to={`/tecnico/chamados/${c.id}`} tamanho="sm" variante="texto">Ver detalhes</Button></>) : <Badge texto={statusDe(c)}/>}</div></td>
          </tr>))}</tbody></table></div>);
    };
    return (<main className="min-h-screen bg-superficie pb-20">
      <div className="mx-auto max-w-[1200px] px-4 pt-10 md:px-6">
        <div className="flex flex-wrap items-end justify-between gap-4">
          <div><h1 className="texto-h1">Chamados</h1><p className="mt-1 text-texto-secundario">Chamados enviados pelos clientes.</p></div>
          <Button variante="secundario" onClick={abrirConversas}><MessageSquare size={18}/>Conversas em andamento</Button>
        </div>
        <div className="mt-8 flex flex-wrap items-center gap-4">
          <div className="relative w-full max-w-md"><Search aria-hidden size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-texto-secundario"/><input aria-label="Buscar chamados" className="campo pl-11" placeholder="Buscar por número, cliente ou produto" value={busca} onChange={(e) => setBusca(e.target.value)}/></div>
          <Segmented label="Filtrar por status" value={status} onChange={setStatus} opcoes={[{ value: 'todos', label: 'Todos' }, { value: 'Chamado enviado', label: 'Enviados' }, { value: 'Em andamento', label: 'Em andamento' }]}/>
        </div>
        <p className="mb-4 mt-8 texto-h3">Chamados enviados</p>
        <div className="space-y-4">
          <Accordion titulo="Novo" contador={novos.length} aberto={!acomp}>{tabela(novos, true)}</Accordion>
          <Accordion titulo="Meus chamados" contador={meus.length}>{tabela(meus, false)}</Accordion>
          <Accordion titulo="Chamados em acompanhamento" contador={0} aberto={acomp} id="acompanhamento">
            <div className="flex flex-col items-center gap-2 p-10 text-center"><span className="flex h-14 w-14 items-center justify-center rounded-full bg-primaria-clara text-primaria"><MessageSquare size={26}/></span><p className="font-semibold">Nada em acompanhamento</p><p className="max-w-sm texto-pequeno text-texto-secundario">Quando um chamado seu estiver aguardando resposta do cliente, ele aparece aqui.</p></div>
          </Accordion>
        </div>
      </div>
    </main>);
}

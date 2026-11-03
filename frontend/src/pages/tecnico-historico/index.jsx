import { FileText } from 'lucide-react';
import { useState } from 'react';
import { useApp } from '../../entities/estado';
import { Avatar, Badge, Button, Popup } from '../../shared/ui';
export default function TecnicoHistorico() {
    const { chamados, usuario } = useApp();
    const [sel, setSel] = useState(null);
    const lista = chamados.filter((c) => c.finalizado && c.tecnicoId === usuario?.id);
    const th = 'px-5 py-4 texto-rotulo text-texto-secundario';
    return (<main className="min-h-screen bg-superficie pb-20">
      <div className="mx-auto max-w-[1200px] px-4 pt-10 md:px-6">
        <h1 className="texto-h1">Histórico</h1>
        <p className="mb-8 mt-1 text-texto-secundario">Atendimentos que você finalizou.</p>
        {lista.length === 0 ? <p className="rounded-lg border border-dashed border-borda-campo bg-fundo p-12 text-center text-texto-secundario">Você ainda não finalizou atendimentos. Quando finalizar, eles aparecem aqui.</p> : (<div className="overflow-x-auto rounded-lg border border-borda bg-fundo shadow-1"><table className="w-full min-w-[720px] text-left">
            <thead className="bg-superficie"><tr><th className={th}>Chamado</th><th className={th}>Cliente</th><th className={th}>Equipamento</th><th className={th}>Finalizado em</th><th className={th}>Status</th><th className={`${th} text-right`}>Resumo</th></tr></thead>
            <tbody>{lista.map((c) => (<tr key={c.id} className="h-14 border-t border-borda hover:bg-primaria-clara/50">
                <td className="num px-5 font-semibold text-primaria">{c.id}</td><td className="px-5"><span className="flex items-center gap-2"><Avatar nome={c.solicitante} tamanho={32} tom={1}/>{c.solicitante}</span></td><td className="px-5">{c.equipamento}</td><td className="num px-5 texto-pequeno text-texto-secundario">{c.atualizacao}</td><td className="px-5"><Badge texto="Concluído"/></td>
                <td className="px-5 text-right"><Button variante="contorno" tamanho="sm" onClick={() => setSel(c)}><FileText size={16}/>Ver resumo do atendimento</Button></td>
              </tr>))}</tbody></table></div>)}
      </div>
      <Popup aberto={!!sel} onClose={() => setSel(null)} id="resumo" titulo="Resumo do atendimento" largura={520}>
        {sel && <div className="mt-5 space-y-4"><p className="num texto-pequeno text-texto-secundario">{sel.id} · {sel.equipamento}</p>
          {[['Resumo do serviço', sel.laudo?.resumo], ['Peças trocadas', sel.laudo?.pecas], ['Observações', sel.laudo?.obs]].map(([a, b]) => <div key={a}><p className="texto-rotulo text-texto-secundario">{a}</p><p>{b}</p></div>)}
          {sel.avaliacao && <div><p className="texto-rotulo text-texto-secundario">Avaliação do cliente</p><p>{sel.avaliacao.nota} de 5 · {sel.avaliacao.comentario || 'Sem comentário'}</p></div>}
          <div className="flex justify-end pt-2"><Button variante="contorno" onClick={() => setSel(null)}>Fechar</Button></div></div>}
      </Popup>
    </main>);
}

import { ArrowLeft, FileText, MessageSquare, Star, XCircle } from 'lucide-react';
import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useApp } from '../../entities/estado';
import { statusDe } from '../../entities/chamado';
import { AceitarOrcamentoPopup } from '../../features/aceitar-orcamento';
import { AprovarServico } from '../../features/aprovar-servico';
import { AvaliarPopup } from '../../features/avaliar-atendimento';
import { CancelarPopup } from '../../features/cancelar-chamado';
import { Alert, Badge, Button, Card, Field, Popup } from '../../shared/ui';
import { AnexosDoChamado } from '../../widgets/anexos-do-chamado';
import { LinhaDoTempo } from '../../widgets/linha-do-tempo';
export default function ChamadoDetalhe() {
    const { id = '' } = useParams();
    const { chamados, threads, visitas } = useApp();
    const c = chamados.find((x) => x.id === id);
    const [pop, setPop] = useState(null);
    const [aba, setAba] = useState('visita');
    const fechar = () => setPop(null);
    if (!c) return <main className="min-h-screen bg-superficie p-10 text-texto-secundario">Carregando chamado…</main>;
    const proposta = [...(threads[c.id] ?? [])].reverse().find((m) => m.tipo === 'proposta');
    const visita = visitas.find((v) => v.chamadoId === c.id);
    const aprovado = c.etapa >= 2;
    return (<>
      <main className="min-h-screen bg-superficie pb-36">
        <div className="mx-auto max-w-[1200px] px-4 pt-8 md:px-6">
          <div className="flex flex-wrap items-start justify-between gap-4">
            <Link to="/chamados" className="inline-flex min-h-11 items-center gap-2 font-semibold text-primaria hover:underline"><ArrowLeft size={18}/>Voltar para o histórico</Link>
            <div className="md:text-right"><h1 className="texto-h2">Número do chamado: <span className="num">{c.id}</span></h1><p className="text-texto-secundario">Aberto em <span className="num">{c.abertura}</span></p></div>
          </div>

          <Card className="mt-6 grid gap-8 lg:grid-cols-[1.2fr_1fr]">
            <div className="space-y-6">
              <div className="flex flex-wrap items-center gap-3"><h2 className="texto-h1">{c.equipamento}</h2><Badge texto={c.garantia}/>{c.validade && <span className="num texto-pequeno text-texto-secundario">até {c.validade}</span>}<Badge texto={statusDe(c)}/></div>
              <Field label="Categoria"><input readOnly className="campo" value={c.categoria}/></Field>
              <Field label="Descrição do problema"><textarea readOnly rows={4} className="campo resize-none" value={c.descricao}/></Field>
            </div>
            <div>
              <p className="mb-2 texto-rotulo">Fotos e vídeos do problema</p>
              <AnexosDoChamado chamadoId={c.chamadoId}/>
            </div>
          </Card>

          <Card className="mt-6">
            <h2 className="texto-h2">Status do chamado</h2>
            <div className="mt-8"><LinhaDoTempo atual={c.etapa} datas={c.datas} etapaClicavel={4} onEtapa={() => setPop('visita')}/></div>
            <div className="mt-8 grid gap-4 md:grid-cols-2">
              {aprovado && <Alert>O técnico aprovou o seu chamado e vai entrar em contato pela conversa do chamado.</Alert>}
              <Alert tipo="alerta">O técnico vai informar o prazo do atendimento. Quando ele informar, o prazo aparece aqui.</Alert>
            </div>
            <div className="mt-8 flex flex-wrap gap-3">
              {c.etapa === 3 && !c.cancelado && <AprovarServico id={c.id}/>}
              {proposta && <Button variante="contorno" onClick={() => setPop('orcamento')}><FileText size={18}/>Aceitar orçamento{proposta.status && proposta.status !== 'Pendente' ? ` (${proposta.status})` : ''}</Button>}
              {c.finalizado && <Button onClick={() => setPop('avaliar')}><Star size={18}/>{c.avaliacao ? 'Ver avaliação' : 'Avaliar atendimento'}</Button>}
              {!c.finalizado && !c.cancelado && <Button variante="contorno" onClick={() => setPop('cancelar')} className="border-borda-campo text-texto"><XCircle size={18}/>Cancelar chamado</Button>}
            </div>
          </Card>
        </div>
      </main>
      <div className="fixed bottom-5 right-5 z-30"><Button to={`/chamados/${c.id}/conversa`} tamanho="lg" className="rounded-full shadow-3"><MessageSquare size={20}/><span className="max-sm:sr-only">Conversar com o técnico</span></Button></div>

      <Popup aberto={pop === 'visita'} onClose={fechar} id="visita" titulo="Visita agendada" largura={560}>
        <div role="tablist" aria-label="Tipo de atendimento" className="mt-5 inline-flex rounded-md border border-borda bg-superficie p-1">
          {[['visita', 'Visita técnica'], ['presencial', 'Atendimento presencial']].map(([k, l]) => (<button key={k} type="button" role="tab" aria-selected={aba === k} onClick={() => setAba(k)} className={`h-10 rounded-sm px-4 texto-pequeno font-semibold ${aba === k ? 'bg-fundo text-primaria shadow-1' : 'text-texto-secundario'}`}>{l}</button>))}
        </div>
        <div key={aba} className="anim-fade mt-6">
          {aba === 'visita' ? (<div className="overflow-hidden rounded-md border border-borda">
              <table className="w-full text-left texto-pequeno"><thead className="bg-superficie texto-rotulo text-texto-secundario"><tr><th className="p-3">Visita</th><th className="p-3">Tipo</th><th className="p-3">Data</th><th className="p-3">Situação</th></tr></thead>
                <tbody><tr className="border-t border-borda"><td className="p-3">1ª</td><td className="p-3">Visita de diagnóstico</td><td className="num p-3">{visita?.data ?? '30/09/2026'}</td><td className="p-3"><Badge texto={visita?.status === 'Aceita' ? 'Confirmado pelo técnico' : 'Pendente'}/></td></tr></tbody></table>
            </div>) : (<div className="space-y-3 texto-corpo">
              <p>Neste atendimento, você leva o equipamento até a oficina da Frigotec.</p>
              <p className="rounded-md bg-superficie p-4"><strong>Endereço:</strong> Av. das Indústrias, 1200, São Paulo<br /><strong>Horário:</strong> segunda a sexta, das 8h às 17h</p>
            </div>)}
        </div>
        <div className="mt-8 flex justify-end"><Button variante="contorno" onClick={fechar}>Fechar</Button></div>
      </Popup>
      <AceitarOrcamentoPopup chamadoId={c.id} proposta={proposta} aberto={pop === 'orcamento'} onClose={fechar}/>
      <AvaliarPopup key={c.id + String(c.avaliacao?.nota)} c={c} aberto={pop === 'avaliar'} onClose={fechar}/>
      <CancelarPopup id={c.id} aberto={pop === 'cancelar'} onClose={fechar}/>
    </>);
}

import { Clock, MapPin } from 'lucide-react';
import { useState } from 'react';
import { DIAS, horario, SEMANA } from '../../../entities/visita';
import { Badge, Button, Popup } from '../../../shared/ui';
const cor = (s) => (s === 'Aceita' ? 'border-sucesso bg-fundo-sucesso text-sucesso' : s === 'Recusada' ? 'border-borda-campo bg-superficie text-texto-secundario' : 'border-brasa bg-fundo-brasa text-texto-brasa');
/** Agenda de visitas: semana de segunda a sábado + lista do dia. */
export function AgendaSemana({ visitas }) {
    const [dia, setDia] = useState(SEMANA[2]);
    const [sel, setSel] = useState(null);
    const doDia = visitas.filter((v) => v.data === dia);
    const fora = visitas.filter((v) => !SEMANA.includes(v.data));
    const bloco = (data, p) => visitas.filter((v) => v.data === data && v.periodo === p);
    return (<div className="grid gap-6 xl:grid-cols-[1fr_340px]">
      <div className="overflow-x-auto rounded-lg border border-borda bg-fundo shadow-1">
        <div className="grid min-w-[720px] grid-cols-[88px_repeat(6,1fr)]">
          <div className="border-b border-borda bg-superficie"/>
          {SEMANA.map((d, i) => (<button key={d} type="button" onClick={() => setDia(d)} aria-pressed={dia === d} className={`border-b border-l border-borda px-2 py-3 text-center transition-colors ${dia === d ? 'bg-primaria text-fundo' : 'bg-superficie hover:bg-primaria-clara'}`}>
              <span className="block texto-rotulo">{DIAS[i]}</span><span className="num block texto-legenda opacity-80">{d.slice(0, 5)}</span>
            </button>))}
          {['Manhã', 'Tarde'].map((p) => (<div key={p} className="contents">
              <div className="flex flex-col justify-center border-b border-borda bg-superficie px-3 py-4"><span className="texto-rotulo">{p}</span><span className="num texto-legenda text-texto-secundario">{horario(p)}</span></div>
              {SEMANA.map((d) => (<div key={d} className={`min-h-28 space-y-2 border-b border-l border-borda p-2 ${dia === d ? 'bg-primaria-clara/40' : ''}`}>
                  {bloco(d, p).map((v) => (<button key={v.id} type="button" onClick={() => setSel(v)} className={`anim-fade w-full rounded-md border-l-4 p-2 text-left texto-legenda font-semibold transition-transform hover:-translate-y-0.5 hover:shadow-2 ${cor(v.status)}`}>
                      <span className="block truncate">{v.cliente}</span><span className="block truncate font-normal">{v.equipamento}</span>
                    </button>))}
                </div>))}
            </div>))}
        </div>
      </div>

      <aside className="rounded-lg border border-borda bg-fundo p-5 shadow-1">
        <h2 className="texto-h3">Visitas do dia <span className="num text-texto-secundario">{dia.slice(0, 5)}</span></h2>
        <ul className="mt-4 space-y-3">
          {doDia.length === 0 && <li className="rounded-md bg-superficie p-4 texto-pequeno text-texto-secundario">Nenhuma visita neste dia. Use "Nova visita" para agendar.</li>}
          {doDia.map((v) => (<li key={v.id} className="rounded-md border border-borda p-3">
              <div className="flex items-start justify-between gap-2"><p className="font-semibold">{v.cliente}</p><Badge texto={v.status}/></div>
              <p className="texto-pequeno text-texto-secundario">{v.equipamento}</p>
              <p className="mt-2 flex items-center gap-1 texto-pequeno"><Clock size={14} aria-hidden/>{horario(v.periodo)}</p>
              <p className="flex items-center gap-1 texto-pequeno"><MapPin size={14} aria-hidden/>{v.endereco}</p>
            </li>))}
        </ul>
        {fora.length > 0 && <p className="mt-6 texto-pequeno text-texto-secundario">Fora desta semana: {fora.map((v) => `${v.cliente} (${v.data.slice(0, 5)})`).join(', ')}.</p>}
      </aside>

      <Popup aberto={!!sel} onClose={() => setSel(null)} id="visita-ag" titulo="Dados da visita" largura={480}>
        {sel && (<div className="mt-5 space-y-3">
            <div className="flex items-center gap-2"><Badge texto={sel.status}/><span className="num texto-pequeno text-texto-secundario">{sel.chamadoId}</span></div>
            <dl className="space-y-3">
              {[['Cliente', sel.cliente], ['Equipamento', sel.equipamento], ['Endereço', sel.endereco], ['Data e horário', `${sel.data} · ${horario(sel.periodo)}`]].map(([a, b]) => <div key={a}><dt className="texto-rotulo text-texto-secundario">{a}</dt><dd className="font-medium">{b}</dd></div>)}
            </dl>
            <div className="flex justify-end gap-3 pt-3"><Button variante="contorno" onClick={() => setSel(null)}>Fechar</Button><Button to={`/tecnico/chamados/${sel.chamadoId}`}>Abrir chamado</Button></div>
          </div>)}
      </Popup>
    </div>);
}

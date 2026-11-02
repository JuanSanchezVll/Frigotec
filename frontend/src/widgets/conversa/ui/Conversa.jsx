import { motion, useReducedMotion } from 'motion/react';
import { CalendarClock, Download, FileText, Paperclip, Send, Timer, X } from 'lucide-react';
import { useEffect, useRef, useState } from 'react';
import { useApp } from '../../../entities/estado';
import { EASE } from '../../../shared/lib/motion';
import { Avatar, Badge, Button } from '../../../shared/ui';
function Cartao({ m, eu, chamadoId }) {
    const { atualizarStatus } = useApp();
    const Icone = m.tipo === 'proposta' ? FileText : m.tipo === 'agendamento' ? CalendarClock : Timer;
    const podeResponder = eu === 'cliente' && m.de === 'tecnico' && m.status === 'Pendente';
    return (<div className="w-full max-w-sm rounded-lg border border-borda bg-fundo p-4 shadow-2">
      <div className="flex items-start gap-3">
        <span className="flex h-11 w-11 shrink-0 items-center justify-center rounded-md bg-primaria-clara text-primaria"><Icone size={22} aria-hidden/></span>
        <div className="min-w-0 flex-1"><p className="font-semibold">{m.titulo}</p><p className="texto-pequeno text-texto-secundario">{m.detalhe}</p></div>
        {m.status && <Badge texto={m.status}/>}
      </div>
      {m.valor && <p className="num mt-3 text-2xl font-extrabold tracking-tight">{m.valor}</p>}
      <div className="mt-4 flex flex-wrap gap-2">
        {m.tipo === 'proposta' && <Button variante="contorno" tamanho="sm"><Download size={16}/>Baixar PDF</Button>}
        {podeResponder && (<><Button tamanho="sm" onClick={() => atualizarStatus(chamadoId, m.id, 'Aceita')}>Aceitar</Button><Button variante="contorno" tamanho="sm" onClick={() => atualizarStatus(chamadoId, m.id, 'Recusada')}>Recusar</Button></>)}
      </div>
    </div>);
}
/** Interface de e-mail corporativo, usada pelo cliente e pelo técnico. Envio simulado, com resposta "digitando…". */
export function Conversa({ eu, chamadoId, nomeCliente, nomeTecnico, altura = 'h-[640px]' }) {
    const { threads, enviar } = useApp();
    const mensagens = threads[chamadoId] ?? [];
    const [texto, setTexto] = useState('');
    const [arqs, setArqs] = useState([]);
    const [digitando, setDigitando] = useState(false);
    const fim = useRef(null);
    const file = useRef(null);
    const inicial = useRef(mensagens.length);
    const r = useReducedMotion();
    const outro = eu === 'cliente' ? 'tecnico' : 'cliente';
    useEffect(() => { fim.current?.scrollIntoView({ block: 'nearest', behavior: r ? 'auto' : 'smooth' }); }, [mensagens.length, digitando, r]);
    const nome = (d) => (d === 'cliente' ? nomeCliente : nomeTecnico);
    const enviarMsg = () => {
        enviar(chamadoId, { de: eu, tipo: 'texto', texto: texto || (arqs.length ? `Segue em anexo: ${arqs.join(', ')}` : 'Mensagem enviada.') });
        setTexto('');
        setArqs([]);
        setDigitando(true);
        setTimeout(() => {
            enviar(chamadoId, { de: outro, tipo: 'texto', texto: eu === 'cliente' ? 'Recebi sua mensagem. Já verifico e retorno por aqui.' : 'Obrigado pelo retorno. Aguardo as próximas informações.' });
            setDigitando(false);
        }, 1200);
    };
    return (<div className={`flex flex-col overflow-hidden rounded-lg border border-borda bg-fundo shadow-2 ${altura}`}>
      <div className="grid gap-1 border-b border-borda bg-superficie px-5 py-3 texto-pequeno sm:grid-cols-[auto_1fr] sm:gap-x-4">
        <span className="font-semibold text-texto-secundario">Para:</span><span className="font-medium">{eu === 'cliente' ? nomeTecnico : nomeCliente}</span>
        <span className="font-semibold text-texto-secundario">Assunto:</span><span className="num font-medium">Chamado {chamadoId}</span>
      </div>
      <div className="flex-1 space-y-5 overflow-y-auto p-4 md:p-6">
        {mensagens.length === 0 && <p className="py-12 text-center text-texto-secundario">Ainda não há mensagens neste chamado. Escreva abaixo para começar.</p>}
        {mensagens.map((m, i) => {
            const meu = m.de === eu;
            const novo = i >= inicial.current && !r;
            return (<motion.div key={m.id} initial={novo ? { opacity: 0, y: meu ? 0 : 12, x: meu ? 40 : 0 } : false} animate={{ opacity: 1, y: 0, x: 0 }} transition={{ duration: 0.25, ease: EASE }} className={`flex gap-3 ${meu ? 'flex-row-reverse' : ''}`}>
              <Avatar nome={nome(m.de)} tom={m.de === 'cliente' ? 0 : 2}/>
              <div className={`flex max-w-[85%] flex-col gap-1 md:max-w-[70%] ${meu ? 'items-end' : 'items-start'}`}>
                <p className="texto-legenda text-texto-secundario"><span className="font-semibold text-texto">{nome(m.de)}</span> · <span className="num">{m.hora}</span></p>
                {m.tipo === 'texto'
                    ? <p className={`rounded-lg px-4 py-3 texto-corpo ${m.de === 'cliente' ? 'rounded-tr-sm bg-primaria-clara' : 'rounded-tl-sm bg-superficie'}`}>{m.texto}</p>
                    : <Cartao m={m} eu={eu} chamadoId={chamadoId}/>}
              </div>
            </motion.div>);
        })}
        {digitando && (<div className="anim-fade flex items-center gap-3" aria-live="polite"><Avatar nome={nome(outro)} tom={outro === 'cliente' ? 0 : 2}/>
            <p className="flex items-center gap-1 rounded-lg bg-superficie px-4 py-3 texto-pequeno text-texto-secundario">digitando<span className="flex gap-0.5">{[0, 1, 2].map((d) => <span key={d} className="h-1.5 w-1.5 rounded-full bg-texto-secundario" style={{ animation: `twinkle 1s ${d * 0.2}s infinite` }}/>)}</span></p></div>)}
        <div ref={fim}/>
      </div>
      <form className="border-t border-borda bg-fundo p-4" onSubmit={(e) => { e.preventDefault(); enviarMsg(); }}>
        {arqs.length > 0 && (<ul className="mb-3 flex flex-wrap gap-2">{arqs.map((a, i) => (<li key={i} className="flex items-center gap-1 rounded-full bg-primaria-clara py-1 pl-3 pr-1 texto-pequeno font-medium text-primaria-escura">{a}
              <button type="button" aria-label={`Remover anexo ${a}`} onClick={() => setArqs(arqs.filter((_, j) => j !== i))} className="flex h-8 w-8 items-center justify-center rounded-full hover:bg-fundo"><X size={14}/></button></li>))}</ul>)}
        <div className="flex flex-col gap-3 sm:flex-row sm:items-end">
          <textarea aria-label="Mensagem" rows={3} className="campo flex-1 resize-none" placeholder="Escreva sua mensagem" value={texto} onChange={(e) => setTexto(e.target.value)}/>
          <div className="flex gap-2">
            <input ref={file} type="file" hidden multiple onChange={(e) => { setArqs([...arqs, ...Array.from(e.target.files ?? []).map((f) => f.name)]); e.target.value = ''; }}/>
            <Button variante="contorno" aria-label="Anexar arquivo" onClick={() => file.current?.click()} className="flex-1 sm:flex-none"><Paperclip size={18}/>Anexo</Button>
            <Button type="submit" className="flex-1 sm:flex-none"><Send size={18}/>Enviar</Button>
          </div>
        </div>
      </form>
    </div>);
}

import { AlertCircle, BadgeCheck, CheckCircle2, Clock, Loader, Send, ShieldCheck, ShieldOff, Ban } from 'lucide-react';
const S = {
    'Chamado enviado': { cls: 'bg-primaria-clara text-primaria-escura', icon: Send },
    'Em andamento': { cls: 'bg-primaria text-fundo', icon: Loader },
    Concluído: { cls: 'bg-sucesso text-fundo', icon: CheckCircle2 },
    Cancelado: { cls: 'bg-superficie text-texto-secundario ring-1 ring-borda-campo/50', icon: Ban },
    'Garantia ativa': { cls: 'bg-fundo-sucesso text-sucesso ring-1 ring-sucesso/30', icon: ShieldCheck },
    'Garantia em análise': { cls: 'bg-fundo-brasa text-texto-brasa ring-1 ring-brasa/50', icon: Clock, extra: 'pulse-brasa' },
    'Garantia vencida': { cls: 'bg-superficie text-texto-secundario ring-1 ring-borda-campo/50', icon: ShieldOff },
    'Garantia recusada': { cls: 'bg-superficie text-texto-secundario ring-1 ring-borda-campo/50', icon: ShieldOff },
    'Sem garantia': { cls: 'bg-superficie text-texto-secundario ring-1 ring-borda-campo/50', icon: ShieldOff },
    'Confirmado pelo técnico': { cls: 'bg-fundo-sucesso text-sucesso', icon: BadgeCheck },
    Pendente: { cls: 'bg-fundo-brasa text-texto-brasa ring-1 ring-brasa/50', icon: Clock, extra: 'pulse-brasa' },
    Aceita: { cls: 'bg-fundo-sucesso text-sucesso', icon: CheckCircle2 },
    Recusada: { cls: 'bg-superficie text-texto-secundario ring-1 ring-borda-campo/50', icon: AlertCircle },
};
/** Selo de status. Pendente e análise pulsam em âmbar; aceita e concluído desenham o check. */
export function Badge({ texto }) {
    const s = S[texto] ?? S.Pendente;
    const I = s.icon;
    const check = texto === 'Aceita' || texto === 'Concluído' || texto === 'Garantia ativa' || texto === 'Confirmado pelo técnico';
    return (<span className={`inline-flex h-7 items-center gap-1.5 whitespace-nowrap rounded-full px-3 texto-legenda font-semibold ${s.cls} ${s.extra ?? ''}`}>
      {check ? (<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round" aria-hidden className="draw-in"><path className="d" d="M5 12.5l4.5 4.5L19 7.5"/></svg>) : <I size={14} aria-hidden/>}
      {texto}
    </span>);
}

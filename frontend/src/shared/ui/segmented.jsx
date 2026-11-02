import { Check } from 'lucide-react';
/** Seletor segmentado compacto (filtros). */
export function Segmented({ label, opcoes, value, onChange }) {
    return (<div role="radiogroup" aria-label={label} className="inline-flex rounded-md bg-superficie p-1 border border-borda">
      {opcoes.map((o) => (<button key={o.value} type="button" role="radio" aria-checked={value === o.value} onClick={() => onChange(o.value)} className={`h-10 rounded-sm px-4 texto-pequeno font-semibold transition-all ${value === o.value ? 'bg-fundo text-primaria shadow-1' : 'text-texto-secundario hover:text-texto'}`}>
          {o.label}
        </button>))}
    </div>);
}
/** Seletor em cartões grandes (Física / Jurídica). */
export function SegmentedCards({ label, opcoes, value, onChange }) {
    return (<div role="radiogroup" aria-label={label} className="grid gap-4 sm:grid-cols-2">
      {opcoes.map((o) => {
            const ativo = value === o.value;
            const Icon = o.icon;
            return (<button key={o.value} type="button" role="radio" aria-checked={ativo} onClick={() => onChange(o.value)} className={`relative rounded-lg border-2 p-5 text-left transition-all ${ativo ? 'border-primaria bg-primaria-clara shadow-1' : 'border-borda bg-fundo hover:border-primaria'}`}>
            {Icon && <span key={String(ativo)} className={`mb-4 flex h-11 w-11 items-center justify-center rounded-md ${ativo ? 'anim-pop bg-primaria text-fundo' : 'bg-superficie text-texto'}`}><Icon size={22} className={ativo ? 'animate-[wobble_0.5s_ease-in-out]' : ''}/></span>}
            <span className="block texto-h3">{o.label}</span>
            {o.desc && <span className="mt-1 block texto-pequeno text-texto-secundario">{o.desc}</span>}
            {ativo && <span className="absolute right-4 top-4 flex h-6 w-6 items-center justify-center rounded-full bg-primaria text-fundo"><Check size={14}/></span>}
          </button>);
        })}
    </div>);
}

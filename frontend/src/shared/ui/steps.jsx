import { Check } from 'lucide-react';
/** Indicador de etapas. */
export function Steps({ etapas, atual }) {
    return (<ol className="flex items-start">
      {etapas.map((e, i) => (<li key={e} aria-current={i === atual ? 'step' : undefined} className="relative flex flex-1 flex-col items-center text-center">
          {i > 0 && (<><span aria-hidden className="absolute right-1/2 top-[15px] h-0.5 w-full bg-borda"/><span aria-hidden className={`absolute right-1/2 top-[15px] h-0.5 w-full origin-left bg-primaria transition-transform duration-500 ${i <= atual ? 'scale-x-100' : 'scale-x-0'}`}/></>)}
          <span className={`relative z-10 flex h-8 w-8 items-center justify-center rounded-full text-sm font-bold transition-all duration-300 ${i < atual ? 'bg-sucesso text-fundo' : i === atual ? 'bg-primaria text-fundo ring-4 ring-primaria-clara' : 'border-2 border-borda bg-fundo text-texto-secundario'}`}>
            {i < atual ? <Check size={16}/> : i + 1}
          </span>
          <span className={`mt-2 px-1 texto-legenda ${i === atual ? 'font-semibold text-texto' : 'text-texto-secundario'}`}>{e}</span>
        </li>))}
    </ol>);
}

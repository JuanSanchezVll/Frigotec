import { ChevronDown } from 'lucide-react';
import { useState } from 'react';
export function Accordion({ titulo, contador, aberto: ab = false, children, id }) {
    const [aberto, setAberto] = useState(ab);
    return (<section id={id} className="overflow-hidden rounded-lg border border-borda bg-fundo shadow-1">
      <h3>
        <button type="button" aria-expanded={aberto} onClick={() => setAberto(!aberto)} className="flex min-h-16 w-full items-center gap-3 px-5 text-left hover:bg-superficie">
          <span className="texto-h3">{titulo}</span>
          <span className="rounded-full bg-primaria-clara px-2.5 py-0.5 texto-pequeno font-semibold text-primaria-escura">{contador}</span>
          <ChevronDown size={20} className={`ml-auto text-texto-secundario transition-transform duration-200 ${aberto ? 'rotate-180' : ''}`} aria-hidden/>
        </button>
      </h3>
      <div className={`grid transition-all duration-300 ${aberto ? 'grid-rows-[1fr]' : 'grid-rows-[0fr]'}`}>
        <div className="overflow-hidden"><div className="border-t border-borda">{children}</div></div>
      </div>
    </section>);
}

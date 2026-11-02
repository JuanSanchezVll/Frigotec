import { ChevronLeft, ChevronRight } from 'lucide-react';
import { useEffect, useRef } from 'react';
import { marcas } from '../../../entities/usuario';
/** Carrossel infinito em movimento lento (nomes das marcas em tipografia, sem logos reais). */
export function BrandCarousel({ escuro = false }) {
    const ref = useRef(null);
    const pausa = useRef(false);
    useEffect(() => {
        const el = ref.current;
        if (!el || window.matchMedia('(prefers-reduced-motion: reduce)').matches)
            return;
        let raf = 0;
        const tick = () => {
            if (!pausa.current) {
                el.scrollLeft += 0.5;
                if (el.scrollLeft >= el.scrollWidth / 2)
                    el.scrollLeft -= el.scrollWidth / 2;
            }
            raf = requestAnimationFrame(tick);
        };
        raf = requestAnimationFrame(tick);
        return () => cancelAnimationFrame(raf);
    }, []);
    const seta = (dir) => ref.current?.scrollBy({ left: dir * 280, behavior: 'smooth' });
    const btn = `flex h-11 w-11 shrink-0 items-center justify-center rounded-full border shadow-1 transition-colors ${escuro ? 'on-dark border-fundo/20 bg-fundo/10 text-fundo hover:bg-fundo/20' : 'border-borda bg-fundo text-texto-secundario hover:text-primaria'}`;
    return (<div className="flex items-center gap-3" role="group" aria-label="Marcas atendidas" onMouseEnter={() => (pausa.current = true)} onMouseLeave={() => (pausa.current = false)}>
      <button type="button" aria-label="Marca anterior" onClick={() => seta(-1)} className={btn}><ChevronLeft size={20}/></button>
      <div ref={ref} className="flex-1 overflow-hidden [mask-image:linear-gradient(90deg,transparent,#000_8%,#000_92%,transparent)]">
        <ul className="flex w-max items-center gap-14 py-4">
          {[...marcas, ...marcas].map((m, i) => (<li key={i} aria-hidden={i >= marcas.length} className={`whitespace-nowrap text-4xl font-extrabold tracking-tight transition-colors ${escuro ? 'text-fundo/80 hover:text-fundo' : 'text-texto-secundario/60 hover:text-primaria'}`}>{m}</li>))}
        </ul>
      </div>
      <button type="button" aria-label="Próxima marca" onClick={() => seta(1)} className={btn}><ChevronRight size={20}/></button>
    </div>);
}

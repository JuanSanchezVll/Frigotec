import { useInView, useReducedMotion } from 'motion/react';
import { Check } from 'lucide-react';
import { useRef } from 'react';
import { ETAPAS } from '../../../entities/chamado';
/** Linha do tempo de 6 etapas: a linha se preenche e os pontos "ligam" em sequência (1,2 s no total). */
export function LinhaDoTempo({ atual, datas, onEtapa, etapaClicavel }) {
    const ref = useRef(null);
    const vis = useInView(ref, { once: true, amount: 0.3 });
    const r = useReducedMotion();
    const go = vis || r;
    return (<ol ref={ref} className="grid md:grid-cols-6">
      {ETAPAS.map((nome, i) => {
            const feita = i < atual;
            const ehAtual = i === atual;
            const clic = onEtapa && i === etapaClicavel;
            const atraso = { transitionDelay: r ? '0ms' : `${i * 200}ms` };
            const rotulo = <span className={`block text-base ${ehAtual ? 'font-bold' : 'font-semibold'} text-texto ${clic ? 'underline decoration-primaria decoration-2 underline-offset-4' : ''}`}>{nome}</span>;
            return (<li key={nome} aria-current={ehAtual ? 'step' : undefined} className="relative flex gap-4 pb-8 last:pb-0 md:flex-col md:items-center md:gap-3 md:pb-0 md:text-center">
            {i < 5 && (<>
                <span aria-hidden className="absolute left-[15px] top-8 h-full w-0.5 bg-borda md:left-1/2 md:top-[15px] md:h-0.5 md:w-full"/>
                <span aria-hidden style={atraso} className={`absolute left-[15px] top-8 h-full w-0.5 origin-top bg-sucesso transition-transform duration-300 md:left-1/2 md:top-[15px] md:h-0.5 md:w-full md:origin-left ${feita && go ? 'scale-100' : 'scale-0'}`}/>
              </>)}
            <span aria-hidden style={atraso} className={`relative z-10 flex h-8 w-8 shrink-0 items-center justify-center rounded-full transition-all duration-300 ${go ? 'scale-100 opacity-100' : 'scale-75 opacity-40'} ${feita ? 'bg-sucesso text-fundo' : ehAtual ? 'pulse-halo bg-primaria text-fundo ring-4 ring-primaria-clara' : 'border-2 border-borda-campo bg-fundo'}`}>
              {feita ? <Check size={16}/> : ehAtual ? <span className="h-2.5 w-2.5 rounded-full bg-fundo"/> : null}
            </span>
            <span className="md:px-1">
              {clic ? <button type="button" onClick={onEtapa} className="min-h-11 text-left transition-colors hover:text-primaria md:text-center">{rotulo}</button> : rotulo}
              {feita && <span className="num block texto-pequeno text-texto-secundario">{datas[i]}</span>}
              {ehAtual && <span className="block texto-pequeno font-bold text-primaria">Etapa atual</span>}
            </span>
          </li>);
        })}
    </ol>);
}

import { Link } from 'react-router-dom';
import { Counter } from '../../../shared/ui';
/** Cartões de resumo com contador animado (valores vêm dos mocks). */
export function KpiCards({ itens }) {
    return (<ul className="grid gap-5 sm:grid-cols-2 lg:grid-cols-4">
      {itens.map((k) => (<li key={k.label}>
          <Link to={k.to} className="card-hover rivet on-dark relative flex h-full flex-col justify-between gap-6 overflow-hidden rounded-xl border border-gelo/20 bg-profundo p-6 pt-8 text-fundo shadow-2">
            <div className="vent pointer-events-none absolute inset-0 opacity-30" aria-hidden/>
            <span className={`relative flex h-12 w-12 items-center justify-center rounded-lg ${k.tom === 'quente' ? 'bg-brasa/20 text-brasa' : 'bg-destaque/20 text-gelo'}`}><k.icon size={24} aria-hidden/></span>
            <span className="relative"><span className="block text-5xl font-extrabold tracking-tight"><Counter valor={k.valor}/></span><span className="mt-1 block font-semibold text-fundo/85">{k.label}</span></span>
          </Link>
        </li>))}
    </ul>);
}

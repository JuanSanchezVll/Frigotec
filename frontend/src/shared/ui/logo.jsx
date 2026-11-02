import { useId } from 'react';
import { Link } from 'react-router-dom';
/** Dentes da engrenagem, gerados em código. */
function engrenagem(cx, cy, rExt, rInt, dentes) {
    const pts = [];
    for (let i = 0; i < dentes; i++) {
        const a = (i / dentes) * Math.PI * 2;
        const w = Math.PI / dentes;
        const p = (r, ang) => `${(cx + r * Math.cos(ang)).toFixed(2)} ${(cy + r * Math.sin(ang)).toFixed(2)}`;
        pts.push(`${i ? 'L' : 'M'}${p(rInt, a - w * 0.9)}L${p(rExt, a - w * 0.45)}L${p(rExt, a + w * 0.45)}L${p(rInt, a + w * 0.9)}`);
    }
    return `${pts.join('')}Z`;
}
const GEAR = engrenagem(34, 34, 10.5, 7.8, 8);
/** Marca: chave inglesa + engrenagem + aletas de refrigeração, em selo arredondado. */
export function LogoMark({ tamanho = 48 }) {
    const id = useId().replace(/:/g, '');
    return (<svg width={tamanho} height={tamanho} viewBox="0 0 56 56" role="img" aria-label="Frigotec" className="shrink-0">
      <defs>
        <linearGradient id={`${id}f`} x1="0" y1="0" x2="1" y2="1"><stop offset="0" stopColor="#2a4ea2"/><stop offset="1" stopColor="#0b1226"/></linearGradient>
        <linearGradient id={`${id}s`} x1="0" y1="0" x2="1" y2="1"><stop offset="0" stopColor="#ffffff"/><stop offset="1" stopColor="#9db8ff"/></linearGradient>
        <radialGradient id={`${id}g`} cx="0.8" cy="0.1" r="0.9"><stop offset="0" stopColor="#4f7bff" stopOpacity="0.55"/><stop offset="1" stopColor="#4f7bff" stopOpacity="0"/></radialGradient>
      </defs>
      <rect x="1" y="1" width="54" height="54" rx="15" fill={`url(#${id}f)`}/>
      <rect x="1" y="1" width="54" height="54" rx="15" fill={`url(#${id}g)`}/>
      <rect x="1.5" y="1.5" width="53" height="53" rx="14.5" fill="none" stroke="#ffffff" strokeOpacity="0.28"/>
      {/* aletas de refrigeração */}
      {[0, 1, 2, 3].map((i) => <rect key={i} x={9 + i * 4.2} y={37 + (i % 2) * 1.5} width="2" height={11 - (i % 2) * 1.5} rx="1" fill="#ffffff" fillOpacity={0.55 + i * 0.1}/>)}
      {/* chave inglesa */}
      <g transform="rotate(-42 28 28)"><g className="logo-wrench" stroke={`url(#${id}s)`} fill="none" strokeLinecap="round" strokeLinejoin="round">
        <path d="M9 28h25" strokeWidth="5"/>
        <path d="M34 21.5a8 8 0 1 1 0 13" strokeWidth="4.2"/>
        <circle cx="12.5" cy="28" r="1.2" fill="#0b1226" stroke="none"/>
      </g></g>
      {/* engrenagem */}
      <g className="logo-gear">
        <path d={GEAR} fill={`url(#${id}s)`} stroke="#0b1226" strokeWidth="1.6" strokeLinejoin="round"/>
        <circle cx="34" cy="34" r="3.6" fill="#0b1226"/>
        <circle cx="34" cy="34" r="1.5" fill="#4f7bff"/>
      </g>
      <rect className="logo-shine" x="-20" y="0" width="14" height="56" fill="#ffffff" opacity="0.35" transform="skewX(-20)"/>
    </svg>);
}
/** Logo Frigotec redesenhada (vetorial). `tema="claro"` para fundos brancos. */
export function Logo({ to = '/', variante = 'horizontal', tamanho, tema = 'escuro', tagline }) {
    const vertical = variante === 'vertical';
    const t = tamanho ?? (vertical ? 80 : 44);
    const cor = tema === 'escuro' ? 'text-fundo' : 'text-texto';
    const mostraTag = tagline ?? vertical;
    return (<Link to={to} aria-label="Frigotec, serviços e peças para equipamentos de cozinhas profissionais" className={`on-dark group inline-flex min-h-11 items-center ${vertical ? 'flex-col gap-3 text-center' : 'gap-3'}`}>
      <span className="overflow-hidden rounded-[15px] transition-transform duration-300 group-hover:scale-105"><LogoMark tamanho={t}/></span>
      <span className="flex flex-col leading-none">
        <span aria-hidden className={`font-extrabold uppercase tracking-[0.04em] ${cor}`} style={{ fontSize: t * (vertical ? 0.52 : 0.5) }}>
          Frigo<span className="bg-gradient-to-r from-destaque to-[#9db8ff] bg-clip-text text-transparent">tec</span>
        </span>
        {mostraTag && <span aria-hidden className={`mt-3 max-w-[260px] text-sm font-semibold uppercase leading-snug tracking-[0.08em] ${tema === 'escuro' ? 'text-fundo/85' : 'text-texto-secundario'}`}>Serviços e peças para cozinhas profissionais</span>}
      </span>
    </Link>);
}

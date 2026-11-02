import { motion, useScroll, useTransform, useReducedMotion } from 'motion/react';
import { Flame, Snowflake } from 'lucide-react';
import { useRef } from 'react';
/** Seção de rolagem "Frio e Calor": o fundo vai de brasa a gelo e a agulha do termostato gira. Valores do mostrador são ilustrativos. */
export function FrioCalor() {
    const ref = useRef(null);
    const r = useReducedMotion();
    const { scrollYProgress: p } = useScroll({ target: ref, offset: ['start end', 'end start'] });
    // Cores derivadas: brasa escura -> gelo claro (primaria-clara)
    const fundo = useTransform(p, [0.15, 0.7], ['#2b1307', '#e8eef9']);
    const texto = useTransform(p, [0.3, 0.55], ['#ffffff', '#0b1226']);
    const agulha = useTransform(p, [0.15, 0.7], [-110, 110]);
    const chama = useTransform(p, [0.15, 0.55], [1, 0.1]);
    const porta = useTransform(p, [0.35, 0.7], [1, 0.3]);
    const nevoa = useTransform(p, [0.4, 0.7], [0, 1]);
    return (<motion.section ref={ref} id="frio-calor" style={r ? { background: '#e8eef9', color: '#0b1226' } : { background: fundo, color: texto }} className="relative overflow-hidden py-24 md:py-32">
      <div className="mx-auto max-w-[1200px] px-4 md:px-6">
        <h2 className="mx-auto max-w-2xl text-center texto-h1 md:text-5xl">Cuidamos do quente e do frio da sua cozinha.</h2>
        <div className="mt-16 grid items-center gap-10 md:grid-cols-[1fr_auto_1fr]">
          {/* Forno com chama */}
          <div className="text-center">
            <svg viewBox="0 0 220 200" className="mx-auto w-full max-w-[260px]" role="img" aria-label="Ilustração de um forno com chama acesa">
              <rect x="20" y="30" width="180" height="150" rx="14" fill="#151515" stroke="#ff7a1a" strokeOpacity="0.6" strokeWidth="2"/>
              <rect x="36" y="64" width="148" height="92" rx="8" fill="#0b1226" stroke="#ffffff" strokeOpacity="0.25"/>
              {[0, 1, 2].map((i) => <circle key={i} cx={60 + i * 50} cy="46" r="7" fill="#ffffff" fillOpacity="0.2"/>)}
              <motion.g style={{ opacity: r ? 1 : chama, transformOrigin: '110px 150px' }}>
                {[70, 110, 150].map((x, i) => (<path key={x} d={`M${x} 150c-16-14-12-30 0-44 4 12 22 18 14 44z`} fill={i === 1 ? '#ffb15c' : '#ff7a1a'} style={{ transformOrigin: `${x}px 150px`, animation: `flame ${1.1 + i * 0.25}s ease-in-out infinite` }}/>))}
              </motion.g>
            </svg>
            <p className="mt-4 flex items-center justify-center gap-2 font-semibold"><Flame size={20} aria-hidden/>Forno e cozinha quente</p>
          </div>
          {/* Mostrador de termostato */}
          <div className="mx-auto text-center">
            <svg viewBox="0 0 200 200" className="w-52 md:w-64" role="img" aria-label="Mostrador de termostato ilustrativo">
              <circle cx="100" cy="100" r="92" fill="#0b1226" stroke="#bfd7ff" strokeOpacity="0.5" strokeWidth="3"/>
              <path d="M30 140a80 80 0 0 1 140 0" fill="none" stroke="url(#term)" strokeWidth="10" strokeLinecap="round"/>
              <defs><linearGradient id="term" x1="0" x2="1"><stop offset="0" stopColor="#4f7bff"/><stop offset="0.5" stopColor="#bfd7ff"/><stop offset="1" stopColor="#ff7a1a"/></linearGradient></defs>
              {Array.from({ length: 13 }).map((_, i) => { const a = (-120 + i * 20) * (Math.PI / 180); return <line key={i} x1={100 + 66 * Math.sin(a)} y1={100 - 66 * Math.cos(a)} x2={100 + 74 * Math.sin(a)} y2={100 - 74 * Math.cos(a)} stroke="#fff" strokeOpacity="0.6"/>; })}
              <motion.g style={{ rotate: r ? 0 : agulha, transformOrigin: '100px 100px' }}><path d="M97 100L100 36l3 64z" fill="#ffffff"/></motion.g>
              <circle cx="100" cy="100" r="9" fill="#2a4ea2" stroke="#fff" strokeWidth="2"/>
              <text x="100" y="160" textAnchor="middle" fill="#bfd7ff" fontSize="11" fontWeight="600">ILUSTRATIVO</text>
            </svg>
          </div>
          {/* Câmara fria com porta e névoa */}
          <div className="text-center">
            <svg viewBox="0 0 220 200" className="mx-auto w-full max-w-[260px]" role="img" aria-label="Ilustração de uma câmara fria com a porta abrindo">
              <rect x="30" y="20" width="160" height="165" rx="12" fill="#bfd7ff" stroke="#2a4ea2" strokeWidth="2"/>
              <rect x="50" y="40" width="120" height="145" fill="#0b1226"/>
              <motion.g style={{ scaleX: r ? 1 : porta, transformOrigin: '50px 110px' }}><rect x="50" y="40" width="120" height="145" fill="#e8eef9" stroke="#2a4ea2" strokeWidth="2"/><rect x="150" y="105" width="8" height="26" rx="3" fill="#5a6272"/></motion.g>
              <motion.g style={{ opacity: r ? 1 : nevoa }}>
                {[0, 1, 2, 3].map((i) => <circle key={i} cx={120 + i * 8} cy={150 - i * 14} r="14" fill="#fff" fillOpacity="0.8" style={{ animation: `mist ${3 + i * 0.5}s ${i * 0.6}s ease-out infinite` }}/>)}
              </motion.g>
            </svg>
            <p className="mt-4 flex items-center justify-center gap-2 font-semibold"><Snowflake size={20} aria-hidden/>Câmara fria e refrigeração</p>
          </div>
        </div>
      </div>
    </motion.section>);
}

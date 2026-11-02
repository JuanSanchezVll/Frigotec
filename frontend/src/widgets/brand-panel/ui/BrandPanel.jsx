import { useReducedMotion } from 'motion/react';
import { FOTOS, foto } from '../../../shared/lib/img';
import { Logo } from '../../../shared/ui';
import { VIDEOS } from '../../hero-video';
/** Painel de marca (cadastro e login): vídeo curto em laço escurecido, degradê azul-preto e logo grande. */
export function BrandPanel({ titulo, children, className = '' }) {
    const r = useReducedMotion();
    const poster = foto(FOTOS.aco.id, 1200);
    return (<aside className={`on-dark relative isolate flex flex-col justify-between overflow-hidden bg-profundo p-10 text-fundo xl:p-14 ${className}`}>
      {r ? <img src={poster} alt="" className="absolute inset-0 -z-20 h-full w-full object-cover opacity-60"/>
            : <video className="absolute inset-0 -z-20 h-full w-full object-cover opacity-60" autoPlay muted loop playsInline poster={poster} aria-hidden><source src={VIDEOS.fogao} type="video/mp4"/></video>}
      <div className="absolute inset-0 -z-10 bg-gradient-to-br from-secundaria/90 via-profundo/85 to-primaria/70"/>
      <div className="vent absolute inset-0 -z-10 opacity-40" aria-hidden/>
      <Logo variante="vertical" tamanho={88}/>
      <div>
        <h1 className="max-w-md texto-h1 xl:text-5xl">{titulo}</h1>
        <div className="mt-8 space-y-4 text-base">{children}</div>
      </div>
      <p className="texto-pequeno text-fundo/80">Serviços e peças para equipamentos de cozinhas profissionais.</p>
    </aside>);
}

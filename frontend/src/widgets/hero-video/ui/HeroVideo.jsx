import { useReducedMotion } from 'motion/react';
export const VIDEOS = {
    restaurante: 'https://cdn.coverr.co/videos/coverr-chef-cooking-in-a-restaurant-3327/1080p.mp4',
    prato: 'https://cdn.coverr.co/videos/coverr-chef-preparing-a-dish-at-a-restaurant-6248/720p.mp4',
    fogao: 'https://cdn.coverr.co/videos/coverr-close-up-of-lit-stove-7688/720p.mp4',
};
/** Vapor subindo e cristais de gelo cintilando (decorativos). */
export function Particulas() {
    return (<div aria-hidden className="pointer-events-none absolute inset-0 overflow-hidden">
      {Array.from({ length: 9 }).map((_, i) => (<span key={i} className="absolute bottom-0 block h-10 w-10 rounded-full bg-fundo/15 blur-xl" style={{ left: `${8 + i * 10}%`, animation: `float-up ${7 + (i % 4)}s ${i * 0.9}s ease-out infinite` }}/>))}
      {Array.from({ length: 14 }).map((_, i) => (<span key={i} className="absolute block h-1.5 w-1.5 rotate-45 bg-gelo" style={{ right: `${3 + (i * 7) % 24}%`, top: `${8 + (i * 13) % 50}%`, animation: `twinkle ${2.2 + (i % 3)}s ${i * 0.3}s ease-in-out infinite` }}/>))}
    </div>);
}
/** Vídeo de fundo em laço: mudo, sem controles, com imagem de reserva e zoom lento. */
export function HeroVideo({ src, poster, alt, children, className = '', particulas = false, gradiente = 'from-secundaria via-secundaria/80 to-secundaria/10' }) {
    const reduzido = useReducedMotion();
    return (<section className={`on-dark relative isolate overflow-hidden bg-profundo text-fundo ${className}`}>
      <div className="absolute inset-0 -z-20 overflow-hidden">
        {reduzido ? (<img src={poster} alt={alt} className="h-full w-full object-cover"/>) : (<video className="zoom-slow h-full w-full object-cover" autoPlay muted loop playsInline poster={poster} aria-label={alt} preload="auto">
            <source src={src} type="video/mp4"/>
          </video>)}
      </div>
      <div className={`absolute inset-0 -z-10 bg-gradient-to-r ${gradiente}`}/>
      <div className="absolute inset-0 -z-10 bg-gradient-to-t from-profundo/80 via-transparent to-profundo/40"/>
      <div className="vent pointer-events-none absolute inset-0 -z-10 opacity-40" aria-hidden/>
      {particulas && <Particulas />}
      {children}
    </section>);
}

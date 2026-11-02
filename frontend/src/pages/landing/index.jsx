import { motion, useReducedMotion, useScroll, useTransform } from 'motion/react';
import { ArrowRight, ClipboardCheck, Clock, History, MessageSquare, PackagePlus, Quote, ShieldCheck, Star } from 'lucide-react';
import { useRef } from 'react';
import { depoimentos } from '../../entities/usuario';
import { DUR, EASE } from '../../shared/lib/motion';
import { FOTOS, foto } from '../../shared/lib/img';
import { Reveal } from '../../shared/lib/reveal';
import { Avatar, Button } from '../../shared/ui';
import { BrandCarousel } from '../../widgets/brand-carousel';
import { Footer } from '../../widgets/footer';
import { HeaderPublico } from '../../widgets/header-publico';
import { HeroVideo, VIDEOS } from '../../widgets/hero-video';
import { FrioCalor } from './ui/FrioCalor';
const PALAVRAS = ['Assistência', 'técnica', 'para', 'a', 'sua'];
const beneficios = [
    { icon: History, t: 'Histórico por equipamento', d: 'Tudo o que cada máquina já teve de conserto.' },
    { icon: ShieldCheck, t: 'Garantia acompanhada', d: 'Pedir a garantia e saber em que etapa ela está.' },
    { icon: MessageSquare, t: 'Conversa direta com o técnico', d: 'Sem telefone, sem recado perdido.' },
];
function IconePasso({ i }) {
    const base = 'relative flex h-16 w-16 items-center justify-center rounded-xl bg-gradient-to-br from-primaria to-destaque text-fundo shadow-[0_10px_24px_-10px_var(--cor-primaria)] transition-transform duration-300';
    if (i === 0)
        return <span className={`${base} group-hover:-translate-y-1 group-hover:-rotate-6`}><PackagePlus size={28} aria-hidden className="transition-transform duration-300 group-hover:scale-110"/></span>;
    if (i === 1)
        return <span className={base}><ClipboardCheck size={28} aria-hidden/><svg viewBox="0 0 24 24" width="18" height="18" className="absolute bottom-1.5 right-1.5 scale-0 text-fundo transition-transform duration-300 group-hover:scale-100" fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round"><path d="M5 12.5l4.5 4.5L19 7.5"/></svg></span>;
    if (i === 2)
        return <span className={base}><MessageSquare size={28} aria-hidden/><span className="absolute inset-0 flex items-center justify-center gap-0.5 pt-0.5 opacity-0 transition-opacity group-hover:opacity-100">{[0, 1, 2].map((d) => <span key={d} className="h-1 w-1 rounded-full bg-fundo" style={{ animation: `twinkle 0.9s ${d * 0.15}s infinite` }}/>)}</span></span>;
    return <span className={`${base} group-hover:shadow-[0_0_28px_var(--cor-gelo)]`}><ShieldCheck size={28} aria-hidden/></span>;
}
const passos = [
    { t: 'Cadastre o equipamento', d: 'Informe marca, modelo e número de série uma única vez.' },
    { t: 'Abra o chamado', d: 'Escolha o equipamento, descreva o problema e envie fotos.' },
    { t: 'Converse com o técnico', d: 'Receba orçamento, agendamento e prazo pela conversa do chamado.' },
    { t: 'Acompanhe até a conclusão', d: 'Veja cada etapa do atendimento e da garantia em um só lugar.' },
];
/** Landing: tela pública de entrada ("a porta da câmara fria"). */
export default function Landing() {
    const r = useReducedMotion();
    const marcas = useRef(null);
    const tec = useRef(null);
    const { scrollYProgress } = useScroll({ target: tec, offset: ['start end', 'end start'] });
    const py = useTransform(scrollYProgress, [0, 1], [-20, 20]);
    const luz = (e) => {
        const b = marcas.current.getBoundingClientRect();
        marcas.current.style.setProperty('--mx', `${e.clientX - b.left}px`);
        marcas.current.style.setProperty('--my', `${e.clientY - b.top}px`);
    };
    return (<>
      <HeaderPublico />
      <main>
        <HeroVideo src={VIDEOS.restaurante} poster={foto(FOTOS.hero.id, 1920)} alt="Cozinha profissional com chamas na chapa e aço inox" particulas className="flex min-h-screen flex-col justify-end">
          <div className="mx-auto w-full max-w-[1200px] px-4 pb-10 pt-32 md:px-6">
            <p className="anim-slide inline-flex items-center gap-2 rounded-full glass px-4 py-1.5 texto-pequeno font-semibold"><span className="h-2 w-2 rounded-full bg-gelo pulse-halo" aria-hidden/>Pós-venda para cozinhas profissionais</p>
            <h1 className="mt-6 max-w-3xl texto-hero" aria-label="Assistência técnica para a sua cozinha profissional">
              <span aria-hidden>
                {[...PALAVRAS, 'cozinha', 'profissional'].map((w, i) => (<motion.span key={i} className={`mr-[0.25em] inline-block ${i >= PALAVRAS.length ? 'brilho-texto' : ''}`} initial={r ? false : { opacity: 0, y: 28 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: DUR.secao, ease: EASE, delay: 0.15 + i * 0.09 }}>{w}</motion.span>))}
              </span>
            </h1>
            <p className="anim-slide mt-6 max-w-xl texto-corpo text-fundo/85 md:text-lg">Cadastre seus equipamentos, abra chamados e acompanhe cada atendimento e garantia em um só lugar.</p>
            <div className="anim-slide mt-10 flex flex-col gap-3 sm:flex-row">
              <Button to="/login" tamanho="lg">Abrir seu chamado <ArrowRight size={20}/></Button>
              <Button variante="contorno-branco" tamanho="lg" onClick={() => document.getElementById('servicos')?.scrollIntoView({ behavior: 'smooth' })}>Conhecer os serviços</Button>
            </div>
            <ul className="glass mt-14 grid gap-1 rounded-xl p-2 md:grid-cols-3">
              <li className="flex items-center gap-3 rounded-lg px-4 py-3"><span className="flex h-10 w-10 items-center justify-center rounded-md bg-destaque/25"><motion.span initial={r ? false : { rotate: 0 }} animate={{ rotate: 360 }} transition={{ duration: 1.2, delay: 1.2, ease: EASE }}><Clock size={20} aria-hidden/></motion.span></span><span className="font-semibold">Chamado em minutos</span></li>
              <li className="flex items-center gap-3 rounded-lg px-4 py-3"><span className="flex h-10 w-10 items-center justify-center rounded-md bg-destaque/25"><span className="h-3.5 w-3.5 rounded-full bg-gelo pulse-halo" aria-hidden/></span><span className="font-semibold">Acompanhamento em tempo real</span></li>
              <li className="flex items-center gap-3 rounded-lg px-4 py-3"><span className="flex h-10 w-10 items-center justify-center rounded-md bg-destaque/25"><svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="draw-in" aria-hidden><path className="d" d="M12 3l8 3v6c0 4.5-3.3 8-8 9-4.7-1-8-4.5-8-9V6l8-3z"/></svg></span><span className="font-semibold">Garantia sob análise de especialistas</span></li>
            </ul>
          </div>
        </HeroVideo>

        <FrioCalor />

        <section id="marcas" ref={marcas} onMouseMove={luz} className="on-dark relative isolate scroll-mt-20 overflow-hidden bg-profundo py-20 text-fundo md:py-28">
          <div aria-hidden className="pointer-events-none absolute inset-0 -z-10" style={{ background: 'radial-gradient(320px circle at var(--mx, 50%) var(--my, 50%), rgba(79,123,255,0.3), transparent 70%)' }}/>
          <div className="vent absolute inset-0 -z-10 opacity-40" aria-hidden/>
          <div className="mx-auto max-w-[1200px] px-4 md:px-6">
            <Reveal><h2 className="max-w-xl texto-h1">Atendemos as marcas que sua cozinha usa</h2></Reveal>
            <Reveal className="mt-10" delay={100}><BrandCarousel escuro/></Reveal>
          </div>
        </section>

        <section id="servicos" className="scroll-mt-20 bg-superficie py-20 md:py-28">
          <div className="mx-auto max-w-[1200px] px-4 md:px-6">
            <Reveal><p className="texto-rotulo uppercase tracking-widest text-primaria">Como funciona</p><h2 className="mt-3 max-w-xl texto-h1">Da quebra à solução em quatro passos</h2></Reveal>
            <div className="relative mt-14">
              <motion.span aria-hidden className="absolute left-[12%] right-[12%] top-[56px] hidden h-0.5 origin-left bg-gradient-to-r from-primaria/20 via-primaria to-primaria/20 md:block" initial={r ? false : { scaleX: 0 }} whileInView={{ scaleX: 1 }} viewport={{ once: true, amount: 0.5 }} transition={{ duration: 1.2, ease: EASE }}/>
              <ol className="grid gap-6 md:grid-cols-4">
                {passos.map((p, i) => (<Reveal key={p.t} ordem={i}>
                    <li className="card-hover group relative h-full list-none rounded-xl border border-borda bg-fundo p-6">
                      <IconePasso i={i}/>
                      <span className="num mt-5 block texto-legenda font-bold text-primaria">PASSO {i + 1}</span>
                      <h3 className="mt-1 texto-h3">{p.t}</h3>
                      <p className="mt-2 texto-pequeno text-texto-secundario">{p.d}</p>
                    </li>
                  </Reveal>))}
              </ol>
            </div>
          </div>
        </section>

        <section ref={tec} className="on-dark relative isolate overflow-hidden bg-hero py-20 text-fundo md:py-28">
          <div className="glow absolute inset-0 -z-10" aria-hidden/><div className="diag absolute inset-0 -z-10" aria-hidden/>
          <div className="mx-auto grid max-w-[1200px] items-center gap-12 px-4 md:px-6 lg:grid-cols-2">
            <Reveal>
              <div className="relative aspect-[4/3] overflow-hidden rounded-xl shadow-3">
                <motion.img src={foto(FOTOS.tecnico.id, 1600)} alt={FOTOS.tecnico.alt} loading="lazy" style={r ? undefined : { y: py, scale: 1.12 }} className="h-full w-full object-cover"/>
                <div className="absolute inset-0 bg-gradient-to-t from-secundaria/60 to-transparent"/>
              </div>
            </Reveal>
            <div>
              <Reveal><h2 className="texto-h1">Manutenção organizada para a cozinha não parar</h2></Reveal>
              <ul className="mt-8 space-y-6">
                {beneficios.map((b, i) => (<Reveal key={b.t} ordem={i + 1}><li className="flex gap-4"><span className="flex h-12 w-12 shrink-0 items-center justify-center rounded-lg glass"><b.icon size={22} aria-hidden/></span><div><h3 className="texto-h3">{b.t}</h3><p className="text-fundo/80">{b.d}</p></div></li></Reveal>))}
              </ul>
            </div>
          </div>
        </section>

        <section className="bg-fundo py-20 md:py-28">
          <div className="mx-auto max-w-[1200px] px-4 md:px-6">
            <Reveal><h2 className="texto-h1">Quem usa, recomenda</h2></Reveal>
            {/* Depoimentos de exemplo (fictícios) */}
            <ul className="mt-12 grid gap-6 md:grid-cols-3">
              {depoimentos.map((d, i) => (<Reveal key={d.nome} ordem={i}>
                  <li className="card-hover relative h-full list-none rounded-xl border border-borda bg-superficie p-7">
                    <Quote size={44} className="absolute right-6 top-6 text-primaria/15" aria-hidden/>
                    <span role="img" aria-label="Nota 5 de 5" className="flex text-brasa">{[0, 1, 2, 3, 4].map((s) => (<motion.span key={s} initial={r ? false : { opacity: 0.15, scale: 0.6 }} whileInView={{ opacity: 1, scale: 1 }} viewport={{ once: true }} transition={{ delay: 0.3 + i * 0.08 + s * 0.12, duration: 0.3 }}><Star size={18} fill="currentColor"/></motion.span>))}</span>
                    <p className="mt-4 texto-corpo">{d.texto}</p>
                    <div className="mt-6 flex items-center gap-3"><Avatar nome={d.nome} tom={i} tamanho={44}/><div><p className="texto-pequeno font-semibold">{d.nome}</p><p className="texto-legenda text-texto-secundario">{d.cargo} · {d.empresa}</p></div></div>
                  </li>
                </Reveal>))}
            </ul>
          </div>
        </section>

        <section className="px-4 pb-20 md:px-6 md:pb-28">
          <Reveal>
            <div className="on-dark relative isolate mx-auto max-w-[1200px] overflow-hidden rounded-xl bg-gradient-to-br from-primaria via-primaria-escura to-profundo p-10 text-fundo md:p-16">
              <div className="vent-run absolute inset-0 -z-10 opacity-70" aria-hidden/><div className="absolute -right-20 -top-20 -z-10 h-72 w-72 rounded-full bg-destaque/40 blur-3xl" aria-hidden/>
              <h2 className="max-w-xl texto-h1">Precisa de assistência técnica agora?</h2>
              <p className="mt-3 max-w-lg text-fundo/85">Abra o chamado em poucos minutos e acompanhe tudo pelo portal.</p>
              <Button to="/login" variante="branco" tamanho="lg" className="btn-shine mt-8">Abrir seu chamado <ArrowRight size={20}/></Button>
            </div>
          </Reveal>
        </section>
      </main>
      <Footer />
    </>);
}

import { ArrowRight, History, MessageSquare, Refrigerator, Star, Wrench } from 'lucide-react';
import { Link } from 'react-router-dom';
import { useLayout } from '../../shared/lib/layout-context';
import { useApp } from '../../entities/estado';
import { statusDe } from '../../entities/chamado';
import { depoimentos } from '../../entities/usuario';
import { FOTOS, foto } from '../../shared/lib/img';
import { Reveal } from '../../shared/lib/reveal';
import { Avatar, Badge, Button } from '../../shared/ui';
import { BrandCarousel } from '../../widgets/brand-carousel';
import { Footer } from '../../widgets/footer';
import { HeroVideo, VIDEOS } from '../../widgets/hero-video';
import { LinhaDoTempo } from '../../widgets/linha-do-tempo';
import { useConversas } from '../../widgets/painel-conversas';
export default function Home() {
    const { meusChamados, equipamentos, usuario } = useApp();
    const primeiroNome = usuario?.nomeCompleto?.split(' ')[0] ?? 'Gustavo';
    const { abrirConversas } = useLayout();
    const novas = useConversas('cliente').filter((c) => c.naoLida).length;
    const c = meusChamados.find((x) => !x.finalizado && !x.cancelado && x.id === 'PR19283708') ?? meusChamados.find((x) => !x.finalizado && !x.cancelado);
    return (<>
      <main>
        <HeroVideo src={VIDEOS.prato} poster={foto(FOTOS.bancada.id, 1920)} alt="Cozinha profissional escurecida com chef preparando um prato" className="pb-28 pt-20 md:pt-28" gradiente="from-secundaria via-secundaria/85 to-profundo/50">
          <div className="mx-auto max-w-[1200px] px-4 md:px-6">
            <h1 className="anim-slide max-w-2xl texto-hero">Olá, {primeiroNome}. Como podemos ajudar hoje?</h1>
            <p className="mt-5 max-w-lg text-lg text-fundo/85">Abra um chamado, cadastre um equipamento ou veja como está o atendimento.</p>
            <Button to="/chamados/novo" tamanho="lg" className="mt-8">Abrir seu chamado <ArrowRight size={20}/></Button>
          </div>
        </HeroVideo>

        <div className="mx-auto max-w-[1200px] px-4 md:px-6">
          <ul className="relative z-10 -mt-16 grid gap-5 md:grid-cols-3 md:grid-rows-2">
            <li className="md:row-span-2">
              <Link to="/equipamentos/novo" className="card-hover on-dark rivet group relative flex h-full min-h-72 flex-col justify-between gap-8 overflow-hidden rounded-xl border border-gelo/30 bg-gradient-to-br from-primaria to-profundo p-7 pt-9 text-fundo shadow-2">
                <span aria-hidden className="vent pointer-events-none absolute inset-0 opacity-40"/>
                <span className="relative flex h-16 w-16 items-center justify-center rounded-lg bg-fundo/15"><Wrench size={30} aria-hidden className="transition-transform duration-300 group-hover:rotate-12"/></span>
                <span className="relative block">
                  <span className="block texto-h2 text-fundo">Cadastre o seu equipamento para a manutenção</span>
                  <span className="mt-3 block text-fundo/85">Informe marca, modelo e número de série uma única vez.</span>
                  <span className="mt-5 inline-flex items-center gap-2 rounded-md bg-fundo px-4 py-2.5 font-semibold text-secundaria">Cadastrar equipamento <ArrowRight size={18} className="transition-transform group-hover:translate-x-1" aria-hidden/></span>
                </span>
              </Link>
            </li>
            <li>
              <Link to="/equipamentos" className="card-hover group flex h-full min-h-36 items-start gap-4 rounded-xl border border-borda bg-fundo p-6 shadow-1">
                <span className="flex h-12 w-12 shrink-0 items-center justify-center rounded-md bg-primaria-clara text-primaria"><Refrigerator size={24} aria-hidden className="transition-transform duration-300 group-hover:-rotate-6 group-hover:scale-110"/></span>
                <span><span className="block texto-h3">Meus equipamentos</span><span className="num mt-1 inline-block rounded-full bg-primaria-clara px-2.5 py-0.5 texto-legenda font-semibold text-primaria-escura">{equipamentos.length} cadastrados</span></span>
              </Link>
            </li>
            <li>
              <button type="button" onClick={abrirConversas} className="card-hover group flex h-full min-h-36 w-full items-start gap-4 rounded-xl border border-borda bg-fundo p-6 text-left shadow-1">
                <span className="relative flex h-12 w-12 shrink-0 items-center justify-center rounded-md bg-primaria-clara text-primaria"><MessageSquare size={24} aria-hidden className="transition-transform duration-300 group-hover:scale-110"/>{novas > 0 && <span className="absolute -right-1 -top-1 h-3 w-3 rounded-full bg-primaria ring-2 ring-fundo"/>}</span>
                <span><span className="block texto-h3">Conversas em andamento</span>{novas > 0 && <span className="mt-1 inline-block rounded-full bg-primaria px-2.5 py-0.5 texto-legenda font-semibold text-fundo">{novas} {novas === 1 ? 'nova' : 'novas'}</span>}</span>
              </button>
            </li>
            <li className="md:col-span-2">
              <div className="card-hover flex h-full flex-col rounded-xl border border-borda bg-fundo p-6 shadow-1">
                {c ? (<>
                    <div className="flex flex-wrap items-center justify-between gap-3"><div><p className="texto-legenda font-bold uppercase tracking-widest text-primaria">Chamado em andamento</p><h2 className="mt-1 texto-h3">{c.equipamento} · <span className="num">{c.id}</span></h2></div><Badge texto={statusDe(c)}/></div>
                    <div className="mt-6"><LinhaDoTempo atual={c.etapa} datas={c.datas}/></div>
                    <div className="mt-6"><Button to={`/chamados/${c.id}`} variante="contorno" tamanho="sm">Ver detalhes</Button></div>
                  </>) : <p className="m-auto text-texto-secundario">Você não tem chamados em andamento.</p>}
              </div>
            </li>
            <li className="md:col-span-1">
              <Link to="/chamados" className="card-hover group flex h-full min-h-36 items-start gap-4 rounded-xl border border-borda bg-fundo p-6 shadow-1">
                <span className="flex h-12 w-12 shrink-0 items-center justify-center rounded-md bg-primaria-clara text-primaria"><History size={24} aria-hidden className="transition-transform duration-300 group-hover:-rotate-12"/></span>
                <span className="texto-h3">Histórico de chamados</span>
              </Link>
            </li>
          </ul>

          <Reveal><section className="mt-24"><h2 className="texto-h2">Marcas que atendemos</h2><div className="mt-6"><BrandCarousel /></div></section></Reveal>
          <section className="mb-24 mt-20">
            <Reveal><h2 className="texto-h2">Avaliações de clientes</h2></Reveal>
            <ul className="mt-8 grid gap-6 md:grid-cols-3">
              {depoimentos.map((d, i) => (<Reveal key={d.nome} ordem={i}><li className="h-full list-none rounded-xl border border-borda bg-superficie p-6">
                  <span role="img" aria-label="Nota 5 de 5" className="flex text-brasa">{[0, 1, 2, 3, 4].map((s) => <Star key={s} size={16} fill="currentColor"/>)}</span>
                  <p className="mt-3">{d.texto}</p>
                  <div className="mt-5 flex items-center gap-3"><Avatar nome={d.nome} tom={i}/><p className="texto-pequeno font-semibold">{d.nome}<span className="block font-normal text-texto-secundario">{d.cargo}</span></p></div>
                </li></Reveal>))}
            </ul>
          </section>
        </div>
      </main>
      <Footer />
    </>);
}

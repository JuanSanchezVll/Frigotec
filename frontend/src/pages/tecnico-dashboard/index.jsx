import { ArrowRight, CalendarDays, CircleDollarSign, ClipboardList, Inbox } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useApp } from '../../entities/estado';
import { buscarDashboardTecnico } from '../../entities/dashboard';
import { dataBr, horaBr } from '../../entities/equipamento';
import { AtribuirButton } from '../../features/atribuir-chamado';
import { FOTOS, foto } from '../../shared/lib/img';
import { Reveal } from '../../shared/lib/reveal';
import { Avatar, Badge, Button, Card } from '../../shared/ui';
import { HeroVideo, VIDEOS } from '../../widgets/hero-video';
import { KpiCards } from '../../widgets/kpi-cards';
import { useConversas } from '../../widgets/painel-conversas';
/** Dashboard do técnico: mesma estrutura e linguagem da Home do cliente. */
export default function TecnicoDashboard() {
    const { novos, meus } = useApp();
    // KPIs e próximas visitas vêm do backend (GET /dashboard/tecnico); recarrega quando a fila ou a carteira mudam.
    const [painel, setPainel] = useState({ chamadosNovos: 0, meusChamados: 0, visitasDestaSemana: 0, orcamentosAguardandoResposta: 0, proximasVisitas: [] });
    useEffect(() => { buscarDashboardTecnico().then(setPainel).catch(() => {}); }, [novos.length, meus.length]);
    const proximas = painel.proximasVisitas;
    const conversas = useConversas('tecnico').filter((c) => c.naoLida).slice(0, 3);
    return (<main className="min-h-screen bg-superficie pb-20">
      <HeroVideo src={VIDEOS.fogao} poster={foto(FOTOS.bancada.id, 1920)} alt="Fogão profissional com chama acesa em cozinha escurecida" className="pb-28 pt-20 md:pt-28" gradiente="from-secundaria via-secundaria/85 to-profundo/50">
        <div className="mx-auto max-w-[1200px] px-4 md:px-6">
          <h1 className="anim-slide max-w-2xl texto-hero">Olá, Davi. Estes são os chamados da sua fila.</h1>
          <p className="mt-5 max-w-lg text-lg text-fundo/85">Veja o que chegou, o que está com você e as próximas visitas.</p>
          <Button to="/tecnico/chamados" tamanho="lg" className="mt-8">Ver chamados novos <ArrowRight size={20}/></Button>
        </div>
      </HeroVideo>
      <div className="mx-auto max-w-[1200px] px-4 md:px-6">
        <div className="relative z-10 -mt-16">
          <KpiCards itens={[
            { label: 'Chamados novos', valor: painel.chamadosNovos, icon: Inbox, to: '/tecnico/chamados', tom: 'quente' },
            { label: 'Meus chamados', valor: painel.meusChamados, icon: ClipboardList, to: '/tecnico/chamados' },
            { label: 'Visitas desta semana', valor: painel.visitasDestaSemana, icon: CalendarDays, to: '/tecnico/agenda' },
            { label: 'Orçamentos aguardando resposta', valor: painel.orcamentosAguardandoResposta, icon: CircleDollarSign, to: '/tecnico/orcamentos', tom: 'quente' },
        ]}/>
        </div>

        <div className="mt-12 grid gap-6 lg:grid-cols-2">
          <Reveal><Card>
            <div className="flex items-center justify-between"><h2 className="texto-h2">Próximas visitas</h2><Link to="/tecnico/agenda" className="texto-pequeno font-semibold text-primaria hover:underline">Ver agenda</Link></div>
            <ul className="mt-5 space-y-3">{proximas.map((v) => (<li key={v.protocolo} className="flex items-center gap-4 rounded-md border border-borda p-3">
                <span className="flex h-14 w-14 shrink-0 flex-col items-center justify-center rounded-md bg-primaria-clara text-primaria-escura"><span className="num text-lg font-extrabold leading-none">{dataBr(v.quando).slice(0, 2)}</span><span className="num texto-legenda">/{dataBr(v.quando).slice(3, 5)}</span></span>
                <span className="min-w-0 flex-1"><span className="block font-semibold">{v.cliente}</span><span className="block truncate texto-pequeno text-texto-secundario">{v.equipamento} · {horaBr(v.quando)}</span><Link to={`/tecnico/chamados/${v.protocolo}`} className="num texto-pequeno font-semibold text-primaria hover:underline">{v.protocolo}</Link></span>
                <Badge texto="Confirmado pelo técnico"/>
              </li>))}
              {proximas.length === 0 && <li className="p-6 text-center text-texto-secundario">Nenhuma visita agendada.</li>}</ul>
          </Card></Reveal>

          <Reveal delay={100}><Card>
            <div className="flex items-center justify-between"><h2 className="texto-h2">Conversas com mensagem nova</h2></div>
            <ul className="mt-5 space-y-1">{conversas.map((c, i) => (<li key={c.id}><Link to={`/tecnico/chamados/${c.id}/conversa`} className="flex min-h-16 items-center gap-3 rounded-md px-2 transition-colors hover:bg-superficie"><Avatar nome={c.nome} tom={i} tamanho={44}/><span className="min-w-0 flex-1"><span className="flex justify-between"><span className="font-semibold">{c.nome}</span><span className="texto-legenda text-texto-secundario">{c.hora}</span></span><span className="block truncate texto-pequeno text-texto-secundario">{c.previa}</span></span><span aria-label="Não lida" className="h-3 w-3 rounded-full bg-primaria"/></Link></li>))}
              {conversas.length === 0 && <li className="p-6 text-center text-texto-secundario">Nenhuma mensagem nova.</li>}</ul>
          </Card></Reveal>
        </div>

        <Reveal className="mt-6"><Card>
          <div className="flex items-center justify-between"><h2 className="texto-h2">Chamados novos</h2><Link to="/tecnico/chamados" className="texto-pequeno font-semibold text-primaria hover:underline">Ver todos</Link></div>
          <ul className="mt-5 divide-y divide-borda">{novos.slice(0, 3).map((c) => (<li key={c.id} className="flex flex-wrap items-center gap-4 py-3">
              <Avatar nome={c.solicitante} tom={1} tamanho={40}/>
              <span className="min-w-0 flex-1"><Link to={`/tecnico/chamados/${c.id}`} className="num font-semibold text-primaria hover:underline">{c.id}</Link><span className="block texto-pequeno text-texto-secundario">{c.solicitante} · {c.equipamento} · <span className="num">{c.data}</span></span></span>
              <div className="flex gap-2"><AtribuirButton id={c.id}/><Button to={`/tecnico/chamados/${c.id}`} tamanho="sm" variante="texto">Ver detalhes</Button></div>
            </li>))}
            {novos.length === 0 && <li className="py-8 text-center text-texto-secundario">Nenhum chamado novo na fila.</li>}</ul>
        </Card></Reveal>
      </div>
    </main>);
}

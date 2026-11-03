import { lazy, Suspense, useEffect } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';
import { AdminLayout, ClienteLayout, PublicoLayout, TecnicoLayout } from '../layouts';
const Cadastro = lazy(() => import('../../pages/cadastro'));
const AdminColaboradores = lazy(() => import('../../pages/admin-colaboradores'));
import { PERFIL, PERFIS_DA_EQUIPE } from '../../entities/usuario';
import { RotaProtegida } from './RotaProtegida';
const ChamadoDetalhe = lazy(() => import('../../pages/chamado-detalhe'));
const ChamadoNovo = lazy(() => import('../../pages/chamado-novo'));
const Chamados = lazy(() => import('../../pages/chamados'));
const ConversaCliente = lazy(() => import('../../pages/conversa-cliente'));
const EquipamentoNovo = lazy(() => import('../../pages/equipamento-novo'));
const Equipamentos = lazy(() => import('../../pages/meus-equipamentos'));
const Home = lazy(() => import('../../pages/home-cliente'));
const Landing = lazy(() => import('../../pages/landing'));
const Login = lazy(() => import('../../pages/login'));
const TecnicoAgenda = lazy(() => import('../../pages/tecnico-agenda'));
const TecnicoChamado = lazy(() => import('../../pages/tecnico-chamado-detalhe'));
const TecnicoChamados = lazy(() => import('../../pages/tecnico-chamados'));
const TecnicoConversa = lazy(() => import('../../pages/tecnico-conversa'));
const TecnicoDashboard = lazy(() => import('../../pages/tecnico-dashboard'));
const TecnicoHistorico = lazy(() => import('../../pages/tecnico-historico'));
const TecnicoOrcamentos = lazy(() => import('../../pages/tecnico-orcamentos'));
function ScrollTop() {
    const { pathname, hash } = useLocation();
    useEffect(() => {
        if (hash)
            setTimeout(() => document.querySelector(hash)?.scrollIntoView(), 50);
        else
            window.scrollTo(0, 0);
    }, [pathname, hash]);
    return null;
}
/** Telas do portal: públicas (landing, cadastro, login) e protegidas por perfil: cliente, técnico/administrador e só administrador. */
export function AppRoutes() {
    return (<>
      <ScrollTop />
      <Suspense fallback={<p className="p-10 text-texto-secundario">Carregando…</p>}>
      <Routes>
        <Route element={<PublicoLayout />}>
          <Route path="/" element={<Landing />}/>
          <Route path="/cadastro" element={<Cadastro />}/>
          <Route path="/login" element={<Login />}/>
        </Route>
        <Route element={<RotaProtegida perfis={[PERFIL.CLIENTE]}/>}>
        <Route element={<ClienteLayout />}>
          <Route path="/home" element={<Home />}/>
          <Route path="/equipamentos" element={<Equipamentos />}/>
          <Route path="/equipamentos/novo" element={<EquipamentoNovo />}/>
          <Route path="/chamados/novo" element={<ChamadoNovo />}/>
          <Route path="/chamados" element={<Chamados />}/>
          <Route path="/chamados/:id" element={<ChamadoDetalhe />}/>
          <Route path="/chamados/:id/conversa" element={<ConversaCliente />}/>
        </Route>
        </Route>
        <Route element={<RotaProtegida perfis={PERFIS_DA_EQUIPE}/>}>
        <Route element={<TecnicoLayout />}>
          <Route path="/tecnico" element={<TecnicoDashboard />}/>
          <Route path="/tecnico/chamados" element={<TecnicoChamados />}/>
          <Route path="/tecnico/chamados/:id" element={<TecnicoChamado />}/>
          <Route path="/tecnico/chamados/:id/conversa" element={<TecnicoConversa />}/>
          <Route path="/tecnico/agenda" element={<TecnicoAgenda />}/>
          <Route path="/tecnico/orcamentos" element={<TecnicoOrcamentos />}/>
          <Route path="/tecnico/historico" element={<TecnicoHistorico />}/>
        </Route>
        </Route>
        <Route element={<RotaProtegida perfis={[PERFIL.ADMIN]}/>}>
        <Route element={<AdminLayout />}>
          <Route path="/admin" element={<AdminColaboradores />}/>
        </Route>
        </Route>
        <Route path="*" element={<Landing />}/>
      </Routes>
      </Suspense>
    </>);
}

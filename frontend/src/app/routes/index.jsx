import { useEffect } from 'react';
import { Route, Routes, useLocation } from 'react-router-dom';
import { ClienteLayout, PublicoLayout, TecnicoLayout } from '../layouts';
import Cadastro from '../../pages/cadastro';
import ChamadoDetalhe from '../../pages/chamado-detalhe';
import ChamadoNovo from '../../pages/chamado-novo';
import Chamados from '../../pages/chamados';
import ConversaCliente from '../../pages/conversa-cliente';
import EquipamentoNovo from '../../pages/equipamento-novo';
import Equipamentos from '../../pages/meus-equipamentos';
import Home from '../../pages/home-cliente';
import Landing from '../../pages/landing';
import Login from '../../pages/login';
import TecnicoAgenda from '../../pages/tecnico-agenda';
import TecnicoChamado from '../../pages/tecnico-chamado-detalhe';
import TecnicoChamados from '../../pages/tecnico-chamados';
import TecnicoConversa from '../../pages/tecnico-conversa';
import TecnicoDashboard from '../../pages/tecnico-dashboard';
import TecnicoHistorico from '../../pages/tecnico-historico';
import TecnicoOrcamentos from '../../pages/tecnico-orcamentos';
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
/** As 17 telas do portal: 10 do cliente (incluindo as públicas) e 7 do técnico. */
export function AppRoutes() {
    return (<>
      <ScrollTop />
      <Routes>
        <Route element={<PublicoLayout />}>
          <Route path="/" element={<Landing />}/>
          <Route path="/cadastro" element={<Cadastro />}/>
          <Route path="/login" element={<Login />}/>
        </Route>
        <Route element={<ClienteLayout />}>
          <Route path="/home" element={<Home />}/>
          <Route path="/equipamentos" element={<Equipamentos />}/>
          <Route path="/equipamentos/novo" element={<EquipamentoNovo />}/>
          <Route path="/chamados/novo" element={<ChamadoNovo />}/>
          <Route path="/chamados" element={<Chamados />}/>
          <Route path="/chamados/:id" element={<ChamadoDetalhe />}/>
          <Route path="/chamados/:id/conversa" element={<ConversaCliente />}/>
        </Route>
        <Route element={<TecnicoLayout />}>
          <Route path="/tecnico" element={<TecnicoDashboard />}/>
          <Route path="/tecnico/chamados" element={<TecnicoChamados />}/>
          <Route path="/tecnico/chamados/:id" element={<TecnicoChamado />}/>
          <Route path="/tecnico/chamados/:id/conversa" element={<TecnicoConversa />}/>
          <Route path="/tecnico/agenda" element={<TecnicoAgenda />}/>
          <Route path="/tecnico/orcamentos" element={<TecnicoOrcamentos />}/>
          <Route path="/tecnico/historico" element={<TecnicoHistorico />}/>
        </Route>
        <Route path="*" element={<Landing />}/>
      </Routes>
    </>);
}

import { motion, useReducedMotion } from 'motion/react';
import { useState } from 'react';
import { Outlet, useLocation } from 'react-router-dom';
import { LayoutCtx } from '../../shared/lib/layout-context';
import { rota } from '../../shared/lib/motion';
import { HeaderCliente, HeaderTecnico } from '../../widgets/header-app';
import { PainelConversas } from '../../widgets/painel-conversas';
import { SidebarApp } from '../../widgets/sidebar-app';
/** Troca de rota: fade de 200 ms com leve deslize. */
export function Transicao({ children }) {
    const { pathname } = useLocation();
    const r = useReducedMotion();
    return <motion.div key={pathname} {...(r ? { initial: false } : rota)}>{children}</motion.div>;
}
function LayoutLogado({ perfil }) {
    const [conv, setConv] = useState(false);
    const [mobile, setMobile] = useState(false);
    return (<LayoutCtx.Provider value={{ abrirConversas: () => setConv(true) }}>
      {perfil === 'cliente' ? <HeaderCliente onMenu={() => setMobile(true)}/> : <HeaderTecnico onMenu={() => setMobile(true)}/>}
      <SidebarApp perfil={perfil} onConversas={() => setConv(true)} mobileAberto={mobile} onFecharMobile={() => setMobile(false)}/>
      <Transicao><Outlet /></Transicao>
      <PainelConversas perfil={perfil} aberto={conv} onClose={() => setConv(false)}/>
    </LayoutCtx.Provider>);
}
export const ClienteLayout = () => <LayoutLogado perfil="cliente"/>;
export const TecnicoLayout = () => <LayoutLogado perfil="tecnico"/>;
export const PublicoLayout = () => <Transicao><Outlet /></Transicao>;

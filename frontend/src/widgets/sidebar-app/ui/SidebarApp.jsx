import { CalendarDays, CircleDollarSign, ClipboardList, History, House, LayoutDashboard, LogOut, MessageSquare, Menu, PlusCircle, Refrigerator, Users } from 'lucide-react';
import { useEffect, useRef, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { cliente, tecnico } from '../../../entities/usuario';
import { useApp } from '../../../entities/estado';
import { Avatar } from '../../../shared/ui';
const ITENS = {
    cliente: [
        { label: 'Home', icon: House, to: '/home', ativo: (p) => p === '/home' },
        { label: 'Abrir chamado', icon: PlusCircle, to: '/chamados/novo', ativo: (p) => p === '/chamados/novo' },
        { label: 'Meus chamados', icon: ClipboardList, to: '/chamados', ativo: (p) => p.startsWith('/chamados') && p !== '/chamados/novo' },
        { label: 'Meus equipamentos', icon: Refrigerator, to: '/equipamentos', ativo: (p) => p.startsWith('/equipamentos') },
        { label: 'Conversas', icon: MessageSquare, acao: 'conversas' },
    ],
    tecnico: [
        { label: 'Dashboard', icon: LayoutDashboard, to: '/tecnico', ativo: (p) => p === '/tecnico' },
        { label: 'Chamados', icon: ClipboardList, to: '/tecnico/chamados', ativo: (p) => p.startsWith('/tecnico/chamados') },
        { label: 'Agenda de visitas', icon: CalendarDays, to: '/tecnico/agenda', ativo: (p) => p === '/tecnico/agenda' },
        { label: 'Orçamentos', icon: CircleDollarSign, to: '/tecnico/orcamentos', ativo: (p) => p === '/tecnico/orcamentos' },
        { label: 'Histórico', icon: History, to: '/tecnico/historico', ativo: (p) => p === '/tecnico/historico' },
        { label: 'Conversas', icon: MessageSquare, acao: 'conversas' },
    ],
};
// O administrador faz tudo do técnico (SDD seção 3) e ainda gerencia os colaboradores.
ITENS.admin = [{ label: 'Colaboradores', icon: Users, to: '/admin', ativo: (p) => p === '/admin' }, ...ITENS.tecnico];
function Conteudo({ perfil, onConversas, onNavegar }) {
    const { pathname } = useLocation();
    const { usuario, sair } = useApp();
    const u = usuario?.nomeCompleto ?? (perfil === 'cliente' ? cliente.nome : tecnico.nome);
    const base = 'on-dark group relative flex min-h-12 w-full items-center gap-3 rounded-md px-3 text-left font-semibold transition-colors';
    const inner = (I) => <I size={20} aria-hidden className="transition-transform duration-200 group-hover:scale-110 group-hover:-rotate-6"/>;
    return (<div className="inox flex h-full flex-col bg-profundo text-fundo">
      <nav aria-label="Menu lateral" className="flex-1 space-y-1 p-3 pt-5">
        {ITENS[perfil].map((i) => {
            const ativo = i.ativo?.(pathname);
            const cls = `${base} ${ativo ? 'bg-fundo/10 text-fundo' : 'text-fundo/75 hover:bg-fundo/5 hover:text-fundo'}`;
            const barra = ativo && <span aria-hidden className="absolute inset-y-2 left-0 w-1 rounded-r bg-destaque"/>;
            return i.to ? (<Link key={i.label} to={i.to} onClick={onNavegar} aria-current={ativo ? 'page' : undefined} className={cls}>{barra}{inner(i.icon)}{i.label}</Link>) : (<button key={i.label} type="button" onClick={() => { onNavegar(); onConversas(); }} className={cls}>{inner(i.icon)}{i.label}</button>);
        })}
        <Link to="/" onClick={() => { sair(); onNavegar?.(); }} className={`${base} mt-4 text-fundo/75 hover:bg-fundo/5 hover:text-fundo`}>{inner(LogOut)}Sair</Link>
      </nav>
      <div className="flex items-center gap-3 border-t border-fundo/10 p-4">
        <Avatar nome={u} tom={perfil === 'cliente' ? 0 : 1} tamanho={40}/>
        <div className="min-w-0"><p className="truncate texto-pequeno font-semibold">{u}</p>{perfil !== 'cliente' && <span className="rounded-full bg-destaque/20 px-2 py-0.5 texto-legenda font-semibold text-gelo">{perfil === 'admin' ? 'Administrador' : 'Técnico'}</span>}</div>
      </div>
    </div>);
}
/** Menu lateral escondido: aparece ao passar o mouse na borda esquerda; vira gaveta no celular. */
export function SidebarApp({ perfil, onConversas, mobileAberto, onFecharMobile }) {
    const [aberto, setAberto] = useState(false);
    const t = useRef(undefined);
    const abrir = () => { clearTimeout(t.current); setAberto(true); };
    const fechar = () => { clearTimeout(t.current); t.current = setTimeout(() => setAberto(false), 400); };
    useEffect(() => () => clearTimeout(t.current), []);
    const { pathname } = useLocation();
    useEffect(() => setAberto(false), [pathname]);
    return (<>
      <div onMouseEnter={abrir} onMouseLeave={fechar} onKeyDown={(e) => e.key === 'Escape' && setAberto(false)} onBlur={(e) => { if (!e.currentTarget.contains(e.relatedTarget))
        fechar(); }} className={`fixed bottom-0 left-0 top-[72px] z-40 hidden md:block ${aberto ? 'w-[264px]' : 'w-4'}`}>
        <button type="button" aria-label="Abrir menu lateral" aria-expanded={aberto} onFocus={abrir} onClick={abrir} className="on-dark absolute inset-y-0 left-0 w-4 bg-gradient-to-r from-profundo/15 to-transparent">
          <span className={`absolute left-0 top-1/2 flex h-14 w-4 -translate-y-1/2 items-center justify-center rounded-r-md bg-primaria text-fundo shadow-2 transition-opacity ${aberto ? 'opacity-0' : 'opacity-100'}`}><Menu size={12} aria-hidden/></span>
        </button>
        <aside inert={!aberto} aria-label="Menu lateral" className={`absolute left-0 top-0 h-full w-[264px] shadow-3 transition-transform duration-[220ms] ease-out ${aberto ? 'translate-x-0' : '-translate-x-full'}`}>
          <Conteudo perfil={perfil} onConversas={onConversas} onNavegar={() => setAberto(false)}/>
        </aside>
      </div>
      {mobileAberto && (<div className="anim-fade fixed inset-0 z-[60] bg-profundo/70 backdrop-blur-sm md:hidden" onMouseDown={(e) => e.target === e.currentTarget && onFecharMobile()}>
          <aside aria-label="Menu" className="anim-panel-left h-full w-[264px] shadow-3"><Conteudo perfil={perfil} onConversas={onConversas} onNavegar={onFecharMobile}/></aside>
        </div>)}
    </>);
}

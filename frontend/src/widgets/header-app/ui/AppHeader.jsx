import { ChevronDown, LogOut, Menu } from 'lucide-react';
import { useEffect, useRef, useState } from 'react';
import { Link } from 'react-router-dom';
import { Avatar, Logo } from '../../../shared/ui';
/** Cabeçalho logado enxuto: logo e usuário. A navegação fica no menu lateral. */
export function AppHeader({ logoTo, nome, papel, onMenu }) {
    const [user, setUser] = useState(false);
    const ref = useRef(null);
    useEffect(() => {
        const f = (e) => { if (!ref.current?.contains(e.target))
            setUser(false); };
        document.addEventListener('mousedown', f);
        return () => document.removeEventListener('mousedown', f);
    }, []);
    return (<header className="on-dark sticky top-0 z-50 border-b border-fundo/10 bg-profundo/95 text-fundo shadow-2 backdrop-blur">
      <div className="flex h-16 items-center justify-between gap-4 px-4 md:h-[72px] md:px-8">
        <div className="flex items-center gap-2">
          <button type="button" aria-label="Abrir menu" onClick={onMenu} className="on-dark flex h-11 w-11 items-center justify-center rounded-md hover:bg-fundo/10 md:hidden"><Menu size={24}/></button>
          <Logo to={logoTo} tamanho={44}/>
          {papel && <span className="hidden rounded-full bg-destaque/20 px-2.5 py-0.5 texto-legenda font-semibold text-gelo sm:inline">{papel}</span>}
        </div>
        <div ref={ref} className="relative">
          <button type="button" aria-haspopup="menu" aria-expanded={user} onClick={() => setUser(!user)} className="on-dark flex min-h-11 items-center gap-2 rounded-md px-2 hover:bg-fundo/10">
            <span className="hidden texto-pequeno font-medium md:inline">{nome}</span>
            <Avatar nome={nome} tom={1}/>
            <ChevronDown size={16} aria-hidden className="hidden md:block"/>
          </button>
          {user && (<div role="menu" className="anim-pop absolute right-0 top-full mt-2 w-48 rounded-md border border-borda bg-fundo p-1 text-texto shadow-3">
              <Link role="menuitem" to="/" className="flex min-h-11 items-center gap-2 rounded-sm px-3 font-medium hover:bg-superficie"><LogOut size={18}/>Sair</Link>
            </div>)}
        </div>
      </div>
    </header>);
}

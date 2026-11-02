import { Menu, X } from 'lucide-react';
import { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Logo } from '../../../shared/ui';
export function HeaderPublico() {
    const [rolou, setRolou] = useState(false);
    const [menu, setMenu] = useState(false);
    const nav = useNavigate();
    const { pathname } = useLocation();
    useEffect(() => {
        const f = () => setRolou(window.scrollY > 24);
        f();
        window.addEventListener('scroll', f, { passive: true });
        return () => window.removeEventListener('scroll', f);
    }, []);
    const ir = (id) => {
        setMenu(false);
        if (pathname !== '/')
            nav('/');
        setTimeout(() => (id ? document.getElementById(id)?.scrollIntoView({ behavior: 'smooth' }) : window.scrollTo({ top: 0, behavior: 'smooth' })), pathname === '/' ? 0 : 80);
    };
    const item = 'on-dark inline-flex min-h-11 items-center font-semibold text-fundo/80 transition-colors hover:text-fundo';
    const links = (<>
      <button type="button" onClick={() => ir()} className={item}>Home</button>
      <button type="button" onClick={() => ir('marcas')} className={item}>Marcas</button>
      <button type="button" onClick={() => ir('servicos')} className={item}>Serviços</button>
      <Link to="/login" className={`${item} md:hidden`}>Login</Link>
    </>);
    return (<header className={`on-dark fixed inset-x-0 top-0 z-50 text-fundo transition-all duration-300 ${rolou || menu ? 'bg-secundaria/75 shadow-2 backdrop-blur-xl' : 'bg-transparent'}`}>
      <div className={`mx-auto flex max-w-[1200px] items-center justify-between px-4 transition-all duration-300 md:px-6 ${rolou ? 'h-14' : 'h-16 md:h-[76px]'}`}>
        <Logo tamanho={40}/>
        <nav aria-label="Principal" className="hidden items-center gap-8 md:flex">{links}</nav>
        <div className="flex items-center gap-2">
          <Link to="/login" className={`${item} mr-3 hidden md:inline-flex`}>Login</Link>
          <Button to="/cadastro" tamanho="sm">Cadastre-se</Button>
          <button type="button" aria-label={menu ? 'Fechar menu' : 'Abrir menu'} aria-expanded={menu} onClick={() => setMenu(!menu)} className="on-dark flex h-11 w-11 items-center justify-center rounded-md md:hidden">{menu ? <X /> : <Menu />}</button>
        </div>
      </div>
      {menu && <nav aria-label="Menu" className="anim-fade flex flex-col px-4 pb-4 md:hidden">{links}</nav>}
    </header>);
}

import { Mail, Phone } from 'lucide-react';
import { Link } from 'react-router-dom';
import { Logo } from '../../../shared/ui';
/** Rodapé: mesmos links em todas as telas. */
export function Footer() {
    const l = 'on-dark inline-flex min-h-11 items-center text-fundo/70 transition-colors hover:text-fundo hover:underline';
    return (<footer className="on-dark relative overflow-hidden bg-secundaria text-fundo">
      <div className="diag pointer-events-none absolute inset-0 opacity-60" aria-hidden/>
      <div className="relative mx-auto grid max-w-[1200px] gap-10 px-4 py-14 md:grid-cols-[1.4fr_1fr_1fr] md:px-6">
        <div>
          <Logo variante="vertical"/>
          <p className="mt-4 max-w-sm texto-pequeno text-fundo/70">Pós-venda, garantias e assistência técnica para cozinhas profissionais.</p>
        </div>
        <nav aria-label="Rodapé" className="flex flex-col">
          <Link to="/#servicos" className={l}>Serviços</Link>
          <Link to="/#marcas" className={l}>Marcas</Link>
          <Link to="/login" className={l}>Login</Link>
          <Link to="/cadastro" className={l}>Cadastre-se</Link>
        </nav>
        <div className="space-y-2 texto-pequeno text-fundo/80">
          <p className="flex items-center gap-2"><Mail size={16} aria-hidden/>contato@frigotec.com.br</p>
          <p className="flex items-center gap-2"><Phone size={16} aria-hidden/>(00) 0000-0000</p>
        </div>
      </div>
    </footer>);
}

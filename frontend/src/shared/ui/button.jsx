import { Link } from 'react-router-dom';
const V = {
    primario: 'btn-shine bg-primaria text-fundo shadow-[0_6px_16px_-6px_var(--cor-primaria)] hover:bg-primaria-escura hover:-translate-y-0.5 hover:shadow-[0_10px_24px_-6px_var(--cor-destaque)] active:translate-y-px',
    secundario: 'bg-secundaria text-fundo hover:bg-profundo hover:-translate-y-0.5 active:translate-y-px',
    contorno: 'border-2 border-primaria text-primaria bg-fundo hover:bg-primaria-clara',
    texto: 'text-primaria hover:bg-primaria-clara',
    branco: 'bg-fundo text-secundaria hover:bg-primaria-clara hover:-translate-y-0.5 active:translate-y-px',
    'contorno-branco': 'border-2 border-fundo/70 text-fundo hover:bg-fundo/10',
};
const T = { md: 'h-12 px-6', sm: 'h-11 px-4', lg: 'h-14 px-8 text-lg', icone: 'h-11 w-11' };
/** Botão único do projeto. Com `to`, vira link de rota. Nunca desabilitado nesta versão. */
export function Button({ variante = 'primario', tamanho = 'md', to, state, className = '', children, ...rest }) {
    const cls = `inline-flex items-center justify-center gap-2 rounded-md font-semibold whitespace-nowrap transition-all duration-200 ${V[variante]} ${T[tamanho]} ${className}`;
    if (to)
        return (<Link to={to} state={state} className={cls}>
        {children}
      </Link>);
    return (<button type="button" className={cls} {...rest}>
      {children}
    </button>);
}

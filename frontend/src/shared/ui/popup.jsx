import { X } from 'lucide-react';
import { useEffect, useRef } from 'react';
/** Pop-up (não é tela): foco preso, Esc e clique no fundo fecham. */
export function Popup({ aberto, onClose, titulo, id, largura = 480, children, semX = false }) {
    const painel = useRef(null);
    useEffect(() => {
        if (!aberto)
            return;
        const anterior = document.activeElement;
        painel.current?.focus();
        document.body.style.overflow = 'hidden';
        const k = (e) => {
            if (e.key === 'Escape')
                onClose();
            if (e.key === 'Tab' && painel.current) {
                const f = Array.from(painel.current.querySelectorAll('button, a[href], input, textarea, select, [tabindex]:not([tabindex="-1"])'));
                if (!f.length)
                    return;
                const [a, z] = [f[0], f[f.length - 1]];
                if (e.shiftKey && document.activeElement === a) {
                    e.preventDefault();
                    z.focus();
                }
                else if (!e.shiftKey && document.activeElement === z) {
                    e.preventDefault();
                    a.focus();
                }
            }
        };
        document.addEventListener('keydown', k);
        return () => { document.removeEventListener('keydown', k); document.body.style.overflow = ''; anterior?.focus?.(); };
    }, [aberto]); // eslint-disable-line react-hooks/exhaustive-deps
    if (!aberto)
        return null;
    return (<div className="anim-fade fixed inset-0 z-[70] flex items-center justify-center overflow-y-auto bg-profundo/70 p-4 backdrop-blur-sm" onMouseDown={(e) => { if (e.target === e.currentTarget)
        onClose(); }}>
      <div ref={painel} tabIndex={-1} role="dialog" aria-modal="true" aria-labelledby={`${id}-t`} style={{ maxWidth: largura }} className="anim-pop relative my-auto w-full rounded-xl bg-fundo p-6 shadow-3 outline-none sm:p-8">
        {titulo && <h2 id={`${id}-t`} className="pr-10 texto-h2">{titulo}</h2>}
        {!semX && <button type="button" aria-label="Fechar" onClick={onClose} className="absolute right-3 top-3 flex h-11 w-11 items-center justify-center rounded-md text-texto-secundario hover:bg-superficie"><X size={20}/></button>}
        {children}
      </div>
    </div>);
}
/** Painel lateral deslizante. */
export function SidePanel({ aberto, onClose, titulo, children }) {
    useEffect(() => {
        if (!aberto)
            return;
        const k = (e) => e.key === 'Escape' && onClose();
        document.addEventListener('keydown', k);
        return () => document.removeEventListener('keydown', k);
    }, [aberto, onClose]);
    if (!aberto)
        return null;
    return (<div className="anim-fade fixed inset-0 z-[70] bg-profundo/70 backdrop-blur-sm" onMouseDown={(e) => { if (e.target === e.currentTarget)
        onClose(); }}>
      <aside role="dialog" aria-modal="true" aria-label={titulo} className="anim-panel absolute right-0 top-0 flex h-full w-full max-w-[400px] flex-col bg-fundo shadow-3">
        <div className="flex items-center justify-between border-b border-borda p-5">
          <h2 className="texto-h3">{titulo}</h2>
          <button type="button" aria-label="Fechar painel" onClick={onClose} className="flex h-11 w-11 items-center justify-center rounded-md hover:bg-superficie"><X size={20}/></button>
        </div>
        <div className="flex-1 overflow-y-auto">{children}</div>
      </aside>
    </div>);
}

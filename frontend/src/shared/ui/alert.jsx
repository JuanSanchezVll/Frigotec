import { CheckCircle2, Info, TriangleAlert } from 'lucide-react';
const A = {
    info: { cls: 'bg-primaria-clara text-primaria-escura border-primaria', I: Info },
    sucesso: { cls: 'bg-fundo-sucesso text-sucesso border-sucesso', I: CheckCircle2 },
    alerta: { cls: 'bg-fundo-brasa text-texto-brasa border-brasa', I: TriangleAlert },
};
export function Alert({ tipo = 'info', children, action, className = '' }) {
    const { cls, I } = A[tipo];
    return (<div role="status" className={`flex items-start gap-3 rounded-md border-l-4 px-4 py-3 texto-pequeno ${cls} ${className}`}>
      <I size={20} className="mt-0.5 shrink-0" aria-hidden/>
      <div className="flex-1 font-medium">{children}</div>
      {action}
    </div>);
}

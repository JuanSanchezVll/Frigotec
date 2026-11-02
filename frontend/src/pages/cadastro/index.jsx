import { CheckCircle2 } from 'lucide-react';
import { CadastroForm } from '../../features/cadastro';
import { Logo } from '../../shared/ui';
import { BrandPanel } from '../../widgets/brand-panel';
export default function Cadastro() {
    return (<main className="grid min-h-screen lg:grid-cols-2">
      <BrandPanel titulo="Crie sua conta e cuide dos seus equipamentos" className="hidden lg:flex">
        {['Cadastre cada equipamento uma única vez', 'Abra chamados com fotos em poucos passos', 'Acompanhe atendimento e garantia'].map((t) => (<p key={t} className="flex items-center gap-3 font-medium"><CheckCircle2 size={22} className="text-gelo" aria-hidden/>{t}</p>))}
      </BrandPanel>
      <section className="flex flex-col items-center justify-center bg-superficie px-4 py-12 md:px-10">
        <div className="mb-8 lg:hidden"><Logo variante="vertical" tema="claro" tamanho={64}/><h1 className="mt-6 text-center texto-h2">Crie sua conta</h1></div>
        <CadastroForm />
      </section>
    </main>);
}

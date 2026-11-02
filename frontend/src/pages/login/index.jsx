import { Logo } from '../../shared/ui';
import { LoginForm } from '../../features/login';
import { BrandPanel } from '../../widgets/brand-panel';
export default function Login() {
    return (<main className="grid min-h-screen lg:grid-cols-2">
      <section className="flex flex-col items-center justify-center bg-superficie px-4 py-12 md:px-10">
        <div className="mb-10 lg:hidden"><Logo variante="vertical" tema="claro" tamanho={64}/></div>
        <div className="w-full max-w-[480px] rounded-xl border border-borda bg-fundo p-6 shadow-2 md:p-9"><LoginForm /></div>
      </section>
      <BrandPanel titulo="Seja bem-vindo novamente!" className="hidden lg:flex">
        <p className="max-w-sm text-lg text-fundo/90">Seus equipamentos, chamados e conversas com o técnico continuam aqui, do ponto em que você parou.</p>
      </BrandPanel>
    </main>);
}

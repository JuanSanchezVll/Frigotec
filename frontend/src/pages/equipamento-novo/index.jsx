import { ArrowLeft } from 'lucide-react';
import { Link } from 'react-router-dom';
import { EquipamentoForm } from '../../features/cadastrar-equipamento';
export default function EquipamentoNovo() {
    return (<>
      <main className="bg-superficie pb-20">
        <div className="mx-auto max-w-[1100px] px-4 pt-8 md:px-6">
          <Link to="/equipamentos" className="inline-flex min-h-11 items-center gap-2 font-semibold text-primaria hover:underline"><ArrowLeft size={18}/>Voltar para Meus equipamentos</Link>
          <h1 className="mt-2 texto-h1">Cadastre seu equipamento</h1>
          <p className="mb-8 mt-2 max-w-xl text-texto-secundario">Informe os dados uma única vez. Depois é só escolher o equipamento ao abrir um chamado.</p>
          <EquipamentoForm />
        </div>
      </main>
    </>);
}

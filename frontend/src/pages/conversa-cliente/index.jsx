import { ArrowLeft } from 'lucide-react';
import { Link, useParams } from 'react-router-dom';
import { useApp } from '../../entities/estado';
import { TECNICO } from '../../entities/chamado';
import { cliente } from '../../entities/usuario';
import { Conversa } from '../../widgets/conversa';
export default function ConversaCliente() {
    const { id = 'PR19283708' } = useParams();
    const { chamados } = useApp();
    const c = chamados.find((x) => x.id === id);
    return (<main className="min-h-screen bg-superficie pb-16">
      <div className="mx-auto max-w-[900px] px-4 pt-8 md:px-6">
        <Link to={`/chamados/${id}`} className="inline-flex min-h-11 items-center gap-2 font-semibold text-primaria hover:underline"><ArrowLeft size={18}/>Voltar para o chamado</Link>
        <h1 className="mb-6 mt-2 texto-h1">Conversa com o técnico</h1>
        <Conversa eu="cliente" chamadoId={id} nomeCliente={cliente.nome} nomeTecnico={c?.tecnico ?? TECNICO}/>
      </div>
    </main>);
}

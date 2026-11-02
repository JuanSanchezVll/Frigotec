import { useState } from 'react';
import { useApp } from '../../entities/estado';
import { Segmented } from '../../shared/ui';
import { TabelaOrcamentos } from '../../widgets/tabela-orcamentos';
export default function TecnicoOrcamentos() {
    const { orcamentos } = useApp();
    const [f, setF] = useState('todos');
    return (<main className="min-h-screen bg-superficie pb-20">
      <div className="mx-auto max-w-[1200px] px-4 pt-10 md:px-6">
        <h1 className="texto-h1">Orçamentos</h1>
        <p className="mb-8 mt-1 text-texto-secundario">Propostas enviadas aos clientes. O status muda quando o cliente responde.</p>
        <div className="mb-6"><Segmented label="Filtrar por status" value={f} onChange={setF} opcoes={[{ value: 'todos', label: 'Todos' }, { value: 'Pendente', label: 'Pendentes' }, { value: 'Aceita', label: 'Aceitas' }, { value: 'Recusada', label: 'Recusadas' }]}/></div>
        <TabelaOrcamentos lista={orcamentos.filter((o) => f === 'todos' || o.status === f)}/>
      </div>
    </main>);
}

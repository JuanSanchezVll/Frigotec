import { Plus } from 'lucide-react';
import { useState } from 'react';
import { useApp } from '../../entities/estado';
import { statusDe } from '../../entities/chamado';
import { Button, Segmented } from '../../shared/ui';
import { TabelaChamados } from '../../widgets/tabela-chamados';
export default function Chamados() {
    const { meusChamados } = useApp();
    const [f, setF] = useState('todos');
    const lista = meusChamados.filter((c) => { const s = statusDe(c); return f === 'todos' || (f === 'andamento' ? s === 'Chamado enviado' || s === 'Em andamento' : s === 'Concluído'); });
    return (<main className="min-h-screen bg-superficie pb-20">
      <div className="mx-auto max-w-[1200px] px-4 pt-10 md:px-6">
        <div className="flex flex-wrap items-end justify-between gap-4">
          <div><h1 className="texto-h1">Meus chamados</h1><p className="mt-2 text-texto-secundario">Acompanhe todos os atendimentos dos seus equipamentos.</p></div>
          <Button to="/chamados/novo"><Plus size={18}/>Abrir novo chamado</Button>
        </div>
        <div className="mb-6 mt-8"><Segmented label="Filtrar chamados" value={f} onChange={setF} opcoes={[{ value: 'todos', label: 'Todos' }, { value: 'andamento', label: 'Em andamento' }, { value: 'finalizados', label: 'Finalizados' }]}/></div>
        <TabelaChamados lista={lista}/>
      </div>
    </main>);
}

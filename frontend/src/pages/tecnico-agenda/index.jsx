import { CalendarPlus } from 'lucide-react';
import { useState } from 'react';
import { useApp } from '../../entities/estado';
import { AgendarPopup } from '../../features/enviar-proposta';
import { Button } from '../../shared/ui';
import { AgendaSemana } from '../../widgets/agenda-semana';
export default function TecnicoAgenda() {
    const { visitas, meus } = useApp();
    const [nova, setNova] = useState(false);
    return (<main className="min-h-screen bg-superficie pb-20">
      <div className="mx-auto max-w-[1200px] px-4 pt-10 md:px-6">
        <div className="mb-8 flex flex-wrap items-end justify-between gap-4">
          <div><h1 className="texto-h1">Agenda de visitas</h1><p className="mt-1 text-texto-secundario">Semana de <span className="num">28/09</span> a <span className="num">03/10/2026</span>.</p></div>
          <Button onClick={() => setNova(true)}><CalendarPlus size={18}/>Nova visita</Button>
        </div>
        <AgendaSemana visitas={visitas}/>
      </div>
      {nova && <AgendarPopup aberto onClose={() => setNova(false)} chamadoId={meus[0]?.id ?? 'PR19283708'} escolher/>}
    </main>);
}

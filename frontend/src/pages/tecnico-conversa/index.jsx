import { ArrowLeft, CalendarPlus, Download, Eye, FileText, Receipt, Timer } from 'lucide-react';
import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useApp } from '../../entities/estado';
import { TECNICO } from '../../entities/chamado';
import { NotaFiscal } from '../../features/analisar-garantia';
import { AgendarPopup, OrcamentoPopup, PrazoPopup } from '../../features/enviar-proposta';
import { Button, Card, Popup } from '../../shared/ui';
import { Conversa } from '../../widgets/conversa';
export default function TecnicoConversa() {
    const { id = 'PR19283708' } = useParams();
    const { chamados } = useApp();
    const c = chamados.find((x) => x.id === id) ?? chamados[0];
    const [p, setP] = useState(null);
    const fechar = () => setP(null);
    return (<>
      <main className="min-h-screen bg-superficie pb-10">
        <div className="mx-auto grid max-w-[1400px] gap-6 px-4 pt-6 md:px-6 lg:grid-cols-[320px_1fr]">
          <aside><Card className="space-y-5">
            <h2 className="texto-h3">Dados do chamado</h2>
            <dl className="space-y-3 texto-pequeno"><div><dt className="texto-rotulo text-texto-secundario">Solicitante</dt><dd className="font-medium">{c.solicitante}</dd></div><div><dt className="texto-rotulo text-texto-secundario">Categoria</dt><dd className="font-medium">{c.categoria}</dd></div><div><dt className="texto-rotulo text-texto-secundario">Descrição</dt><dd>{c.descricao}</dd></div></dl>
            <div className="flex items-center gap-3 rounded-md border border-borda bg-superficie p-3"><span className="flex h-11 w-11 items-center justify-center rounded-md bg-primaria-clara text-primaria"><FileText size={22}/></span><div className="min-w-0"><p className="texto-pequeno font-semibold">Nota fiscal digitalizada</p><button type="button" onClick={() => setP('nota')} className="inline-flex min-h-8 items-center gap-1 texto-pequeno font-semibold text-primaria hover:underline"><Eye size={14}/>Visualizar nota fiscal</button></div></div>
            <div className="space-y-2">
              <Button className="w-full" variante="contorno" onClick={() => setP('agendar')}><CalendarPlus size={18}/>Solicitar agendamento</Button>
              <Button className="w-full" variante="contorno" onClick={() => setP('orcamento')}><Receipt size={18}/>Solicitar orçamento</Button>
              <Button className="w-full" variante="contorno" onClick={() => setP('prazo')}><Timer size={18}/>Enviar prazo</Button>
            </div>
          </Card></aside>
          <section>
            <Link to={`/tecnico/chamados/${c.id}`} className="inline-flex min-h-11 items-center gap-2 font-semibold text-primaria hover:underline"><ArrowLeft size={18}/>Voltar para o chamado</Link>
            <h1 className="mb-4 mt-1 texto-h2">Conversa com o cliente</h1>
            <Conversa eu="tecnico" chamadoId={c.id} nomeCliente={c.solicitante} nomeTecnico={c.tecnico ?? TECNICO} altura="h-[calc(100vh-240px)] min-h-[520px]"/>
          </section>
        </div>
      </main>
      <AgendarPopup aberto={p === 'agendar'} onClose={fechar} chamadoId={c.id}/>
      <OrcamentoPopup aberto={p === 'orcamento'} onClose={fechar} chamadoId={c.id}/>
      <PrazoPopup aberto={p === 'prazo'} onClose={fechar} chamadoId={c.id}/>
      <Popup aberto={p === 'nota'} onClose={fechar} id="nota" titulo="Nota fiscal digitalizada" largura={720}>
        <div className="mt-6"><NotaFiscal zoom={1.2}/></div>
        <div className="mt-6 flex justify-end gap-3"><Button variante="contorno" onClick={fechar}>Fechar</Button><Button onClick={fechar}><Download size={18}/>Baixar</Button></div>
      </Popup>
    </>);
}

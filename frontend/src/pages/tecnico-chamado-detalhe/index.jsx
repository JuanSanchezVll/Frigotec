import { ArrowLeft, CalendarClock, CheckCheck, MessageSquare, ShieldCheck, UserCheck } from 'lucide-react';
import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useApp } from '../../entities/estado';
import { statusDe } from '../../entities/chamado';
import { ehAdmin } from '../../entities/usuario';
import { AgendarVisitaPopup } from '../../features/agendar-visita';
import { GarantiaPopup } from '../../features/analisar-garantia';
import { AvancarEtapa } from '../../features/avancar-etapa';
import { FinalizarPopup } from '../../features/finalizar-atendimento';
import { Avatar, Badge, Button, Card, Field } from '../../shared/ui';
import { AnexosDoChamado } from '../../widgets/anexos-do-chamado';
import { LinhaDoTempo } from '../../widgets/linha-do-tempo';
export default function TecnicoChamado() {
    const { id = '' } = useParams();
    const { chamados, atribuir, usuario } = useApp();
    const c = chamados.find((x) => x.id === id);
    const [garantia, setGarantia] = useState(false);
    const [fin, setFin] = useState(false);
    const [visita, setVisita] = useState(false);
    if (!c) return <main className="min-h-screen bg-superficie p-10 text-texto-secundario">Carregando chamado…</main>;
    const aberto = !c.finalizado && !c.cancelado;
    const responsavel = c.tecnicoId === usuario?.id;
    return (<>
      <main className="min-h-screen bg-superficie pb-40">
        <div className="mx-auto max-w-[1200px] px-4 pt-8 md:px-6">
          <div className="flex flex-wrap items-start justify-between gap-4">
            <Link to="/tecnico/chamados" className="inline-flex min-h-11 items-center gap-2 font-semibold text-primaria hover:underline"><ArrowLeft size={18}/>Voltar para chamados</Link>
            <div className="md:text-right"><h1 className="texto-h2">Número do chamado: <span className="num">{c.id}</span></h1><p className="text-texto-secundario">Aberto em <span className="num">{c.abertura}</span></p></div>
          </div>
          <div className="mt-6 grid gap-6 lg:grid-cols-[1fr_2fr]">
            <Card><p className="texto-legenda font-bold uppercase tracking-widest text-primaria">Cliente</p>
              <div className="mt-4 flex items-center gap-3"><Avatar nome={c.solicitante} tamanho={52}/><div><p className="texto-h3">{c.solicitante}</p><p className="texto-pequeno text-texto-secundario">{c.cliente?.razaoSocial ?? 'Pessoa física'}</p></div></div>
              <dl className="mt-5 space-y-2 texto-pequeno"><div><dt className="text-texto-secundario">{c.cliente?.cnpj ? 'CNPJ' : 'CPF'}</dt><dd className="num font-medium">{c.cliente?.cnpj ?? c.cliente?.cpf}</dd></div><div><dt className="text-texto-secundario">E-mail</dt><dd className="font-medium">{c.cliente?.email}</dd></div><div><dt className="text-texto-secundario">Telefone</dt><dd className="num font-medium">{c.cliente?.telefone ?? c.cliente?.celular}</dd></div></dl></Card>
            <Card className="grid gap-6 md:grid-cols-[1.4fr_1fr]">
              <div className="space-y-5">
                <div className="flex flex-wrap items-center gap-3"><h2 className="texto-h2">{c.equipamento}</h2><Badge texto={c.garantia}/><Badge texto={statusDe(c)}/></div>
                <Field label="Categoria"><input readOnly className="campo" value={c.categoria}/></Field>
                <Field label="Descrição do problema"><textarea readOnly rows={3} className="campo resize-none" value={c.descricao}/></Field>
              </div>
              <div><p className="mb-2 texto-rotulo">Fotos e vídeos</p><AnexosDoChamado chamadoId={c.chamadoId} colunas={2}/></div>
            </Card>
          </div>
          <Card className="mt-6">
            <div className="flex flex-wrap items-center justify-between gap-3"><h2 className="texto-h2">Status do chamado</h2>
              {aberto && (<div className="flex flex-wrap gap-3">
                {!c.tecnico && <Button onClick={() => atribuir(c.id)}><UserCheck size={18}/>Atribuir a mim</Button>}
                {responsavel && c.etapa === 2 && <AvancarEtapa id={c.id}/>}
                {c.etapa === 3 && <span className="texto-pequeno font-semibold text-texto-secundario">Aguardando o cliente aprovar o serviço</span>}
                {responsavel && c.etapa >= 4 && <Button variante="contorno" onClick={() => setVisita(true)}><CalendarClock size={18}/>{c.etapa === 4 ? 'Agendar visita' : 'Reagendar visita'}</Button>}
                {c.etapa === 5 && (responsavel || ehAdmin(usuario?.perfil)) && <Button onClick={() => setFin(true)}><CheckCheck size={18}/>Finalizar atendimento</Button>}
              </div>)}</div>
            <div className="mt-8"><LinhaDoTempo atual={c.etapa} datas={c.datas}/></div>
            {c.laudo && <div className="mt-8 rounded-md bg-superficie p-4"><p className="texto-rotulo">Resumo do atendimento</p><p className="mt-1 whitespace-pre-line">{c.laudo.resumo}</p><p className="mt-1 texto-pequeno text-texto-secundario">{c.laudo.obs}</p></div>}
            {c.garantiaId && <div className="mt-8 flex flex-wrap items-center justify-between gap-4 rounded-md border-l-4 border-primaria bg-primaria-clara px-5 py-4">
              <p className="flex items-center gap-3 font-medium text-primaria-escura"><ShieldCheck size={22} aria-hidden/>Confira a nota fiscal e a data da compra antes de aprovar a garantia.</p>
              <Button onClick={() => setGarantia(true)}>Analisar garantia</Button>
            </div>}
          </Card>
        </div>
      </main>
      <div className="fixed bottom-5 right-5 z-30"><Button to={`/tecnico/chamados/${c.id}/conversa`} tamanho="lg" className="rounded-full shadow-3"><MessageSquare size={20}/><span className="max-sm:sr-only">Conversar com o cliente</span></Button></div>
      <GarantiaPopup id={c.id} equipamento={c.equipamento} dataCompra={c.dataCompra} aberto={garantia} onClose={() => setGarantia(false)}/>
      <FinalizarPopup id={c.id} aberto={fin} onClose={() => setFin(false)}/>
      <AgendarVisitaPopup id={c.id} aberto={visita} onClose={() => setVisita(false)}/>
    </>);
}

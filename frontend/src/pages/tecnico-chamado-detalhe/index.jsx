import { ArrowLeft, CheckCheck, MessageSquare, ShieldCheck, UserCheck } from 'lucide-react';
import { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useApp } from '../../entities/estado';
import { statusDe } from '../../entities/chamado';
import { cliente } from '../../entities/usuario';
import { GarantiaPopup } from '../../features/analisar-garantia';
import { AvancarEtapa } from '../../features/avancar-etapa';
import { FinalizarPopup } from '../../features/finalizar-atendimento';
import { FOTOS, foto } from '../../shared/lib/img';
import { Avatar, Badge, Button, Card, Field } from '../../shared/ui';
import { LinhaDoTempo } from '../../widgets/linha-do-tempo';
export default function TecnicoChamado() {
    const { id = '' } = useParams();
    const { chamados, atribuir } = useApp();
    const c = chamados.find((x) => x.id === id) ?? chamados[0];
    const [garantia, setGarantia] = useState(false);
    const [fin, setFin] = useState(false);
    const aberto = !c.finalizado && !c.cancelado;
    return (<>
      <main className="min-h-screen bg-superficie pb-40">
        <div className="mx-auto max-w-[1200px] px-4 pt-8 md:px-6">
          <div className="flex flex-wrap items-start justify-between gap-4">
            <Link to="/tecnico/chamados" className="inline-flex min-h-11 items-center gap-2 font-semibold text-primaria hover:underline"><ArrowLeft size={18}/>Voltar para chamados</Link>
            <div className="md:text-right"><h1 className="texto-h2">Número do chamado: <span className="num">{c.id}</span></h1><p className="text-texto-secundario">Aberto em <span className="num">{c.abertura}</span></p></div>
          </div>
          <div className="mt-6 grid gap-6 lg:grid-cols-[1fr_2fr]">
            <Card><p className="texto-legenda font-bold uppercase tracking-widest text-primaria">Cliente</p>
              <div className="mt-4 flex items-center gap-3"><Avatar nome={c.solicitante} tamanho={52}/><div><p className="texto-h3">{c.solicitante}</p><p className="texto-pequeno text-texto-secundario">{cliente.empresa}</p></div></div>
              <dl className="mt-5 space-y-2 texto-pequeno"><div><dt className="text-texto-secundario">CNPJ</dt><dd className="num font-medium">{cliente.cnpj}</dd></div><div><dt className="text-texto-secundario">E-mail</dt><dd className="font-medium">{cliente.email}</dd></div><div><dt className="text-texto-secundario">Telefone</dt><dd className="num font-medium">{cliente.telefone}</dd></div></dl></Card>
            <Card className="grid gap-6 md:grid-cols-[1.4fr_1fr]">
              <div className="space-y-5">
                <div className="flex flex-wrap items-center gap-3"><h2 className="texto-h2">{c.equipamento}</h2><Badge texto={c.garantia}/><Badge texto={statusDe(c)}/></div>
                <Field label="Categoria"><input readOnly className="campo" value={c.categoria}/></Field>
                <Field label="Descrição do problema"><textarea readOnly rows={3} className="campo resize-none" value={c.descricao}/></Field>
              </div>
              <div><p className="mb-2 texto-rotulo">Fotos e vídeos</p><ul className="grid grid-cols-2 gap-2">{[FOTOS.chapa, FOTOS.panela, FOTOS.cozinhaB].map((f) => <li key={f.id}><img src={foto(f.id, 400)} alt={f.alt} loading="lazy" className="aspect-square rounded-md object-cover shadow-1"/></li>)}</ul></div>
            </Card>
          </div>
          <Card className="mt-6">
            <div className="flex flex-wrap items-center justify-between gap-3"><h2 className="texto-h2">Status do chamado</h2>
              {aberto && (<div className="flex flex-wrap gap-3">
                {!c.tecnico && <Button onClick={() => atribuir(c.id)}><UserCheck size={18}/>Atribuir a mim</Button>}
                {c.tecnico && c.etapa < 5 && <AvancarEtapa id={c.id}/>}
                {c.tecnico && <Button onClick={() => setFin(true)}><CheckCheck size={18}/>Finalizar atendimento</Button>}
              </div>)}</div>
            <div className="mt-8"><LinhaDoTempo atual={c.etapa} datas={c.datas}/></div>
            {c.laudo && <div className="mt-8 rounded-md bg-superficie p-4"><p className="texto-rotulo">Resumo do atendimento</p><p className="mt-1">{c.laudo.resumo}</p><p className="mt-1 texto-pequeno text-texto-secundario">Peças trocadas: {c.laudo.pecas}</p></div>}
            <div className="mt-8 flex flex-wrap items-center justify-between gap-4 rounded-md border-l-4 border-primaria bg-primaria-clara px-5 py-4">
              <p className="flex items-center gap-3 font-medium text-primaria-escura"><ShieldCheck size={22} aria-hidden/>Confira a nota fiscal e a data da compra antes de aprovar a garantia.</p>
              <Button onClick={() => setGarantia(true)}>Analisar garantia</Button>
            </div>
          </Card>
        </div>
      </main>
      <div className="fixed bottom-5 right-5 z-30"><Button to={`/tecnico/chamados/${c.id}/conversa`} tamanho="lg" className="rounded-full shadow-3"><MessageSquare size={20}/><span className="max-sm:sr-only">Conversar com o cliente</span></Button></div>
      <GarantiaPopup id={c.id} equipamento={c.equipamento} aberto={garantia} onClose={() => setGarantia(false)}/>
      <FinalizarPopup id={c.id} aberto={fin} onClose={() => setFin(false)}/>
    </>);
}

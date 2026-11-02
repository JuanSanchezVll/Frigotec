import { ArrowLeft, ArrowRight, Check, X } from 'lucide-react';
import { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useApp } from '../../../entities/estado';
import { rotuloEquip } from '../../../entities/equipamento';
import { categorias } from '../../../entities/chamado';
import { FOTOS, foto } from '../../../shared/lib/img';
import { Badge, Button, Card, Combo, Popup, Steps, TextArea, TextInput, Field } from '../../../shared/ui';
const fotos = [FOTOS.chapa, FOTOS.panela, FOTOS.cozinhaB].map((f, i) => ({ id: String(i), src: foto(f.id, 400), alt: ['Chapa com aquecimento irregular', 'Termostato da chapa', 'Superfície da chapa'][i] }));
/** Abrir chamado em 2 passos. Sem validação. */
export function AbrirChamado() {
    const { state } = useLocation();
    const { equipamentos, enviarChamado } = useApp();
    const [numero, setNumero] = useState('');
    const nav = useNavigate();
    const [passo, setPasso] = useState(0);
    const [sel, setSel] = useState(state?.equipamento ?? 'chapa');
    const [cat, setCat] = useState('Temperatura irregular');
    const [outra, setOutra] = useState('');
    const [desc, setDesc] = useState('A chapa esquenta de forma desigual: um lado queima os alimentos e o outro fica frio.');
    const [thumbs, setThumbs] = useState(fotos);
    const [enviado, setEnviado] = useState(false);
    const [seg, setSeg] = useState(5);
    useEffect(() => {
        if (!enviado)
            return;
        setSeg(5);
        const t = setInterval(() => setSeg((s) => s - 1), 1000);
        return () => clearInterval(t);
    }, [enviado]);
    useEffect(() => { if (enviado && seg <= 0)
        nav('/chamados'); }, [seg, enviado, nav]);
    return (<div className="mx-auto max-w-[900px]">
      <Steps etapas={['Selecionar equipamento', 'Descrever problema']} atual={passo}/>
      <Card className="mt-10">
        <div key={passo} className="anim-slide">
          {passo === 0 ? (<>
              <h1 className="texto-h2">Qual equipamento precisa de atendimento?</h1>
              <p className="mt-2 text-texto-secundario">Selecione um equipamento já cadastrado para continuar.</p>
              <div role="radiogroup" aria-label="Equipamentos cadastrados" className="mt-6 space-y-4">
                {equipamentos.map((e) => {
                const ativo = sel === e.id;
                return (<label key={e.id} className={`relative flex cursor-pointer items-center gap-4 rounded-lg border-2 p-4 transition-all has-[:focus-visible]:outline has-[:focus-visible]:outline-2 has-[:focus-visible]:outline-offset-2 has-[:focus-visible]:outline-primaria md:p-5 ${ativo ? 'border-primaria bg-primaria-clara' : 'border-borda hover:border-primaria'}`}>
                      <input type="radio" name="eq" className="sr-only" checked={ativo} onChange={() => setSel(e.id)}/>
                      <span aria-hidden className={`flex h-6 w-6 shrink-0 items-center justify-center rounded-full border-2 ${ativo ? 'border-primaria bg-primaria text-fundo' : 'border-borda-campo bg-fundo'}`}>{ativo && <Check size={14}/>}</span>
                      <span className="min-w-0 flex-1">
                        <span className="block texto-h3">{e.tipo}</span>
                        <span className="num block texto-pequeno text-texto-secundario">{e.marca} · N/S {e.serie}</span>
                      </span>
                      <span className="flex flex-col items-end gap-1"><Badge texto={e.garantia}/>{e.validade && <span className="texto-legenda text-texto-secundario">até {e.validade}</span>}</span>
                    </label>);
            })}
              </div>
            </>) : (<>
              <h1 className="texto-h2">O que está acontecendo com seu equipamento?</h1>
              <div className="mt-6 space-y-6">
                {/* VALIDACAO: categoria obrigatória; "Outros" exige texto */}
                <Combo label="Categoria do problema" options={categorias} value={cat} onChange={setCat} soLista placeholder="Selecione uma categoria"/>
                {cat === 'Outros' && <div className="anim-fade"><TextInput label="Outra categoria" value={outra} onChange={(e) => setOutra(e.target.value)} placeholder="Escreva a categoria do problema"/></div>}
                {/* VALIDACAO: descrição obrigatória */}
                <TextArea label="Descrição do problema" value={desc} onChange={(e) => setDesc(e.target.value)}/>
                <Field label="Fotos e vídeos" optional hint="Fotos ajudam o técnico a entender o defeito antes da visita.">
                  <ul className="flex flex-wrap gap-3">
                    {thumbs.map((t) => (<li key={t.id} className="anim-fade relative"><img src={t.src} alt={t.alt} className="h-24 w-24 rounded-md object-cover shadow-1"/>
                        <button type="button" aria-label={`Remover ${t.alt}`} onClick={() => setThumbs(thumbs.filter((x) => x.id !== t.id))} className="absolute -right-2 -top-2 flex h-7 w-7 items-center justify-center rounded-full bg-secundaria text-fundo shadow-2 hover:bg-primaria"><X size={14}/></button></li>))}
                    <li><button type="button" onClick={() => setThumbs([...thumbs, { ...fotos[thumbs.length % 3], id: `n${Date.now()}` }])} className="flex h-24 w-24 flex-col items-center justify-center rounded-md border-2 border-dashed border-borda-campo bg-superficie texto-legenda font-semibold text-texto-secundario hover:border-primaria hover:text-primaria">+<span>Adicionar</span></button></li>
                  </ul>
                </Field>
              </div>
            </>)}
        </div>
        <div className="mt-10 flex items-center justify-between gap-3">
          {passo === 0 ? <Button variante="contorno" to="/home" className="border-borda-campo text-texto"><ArrowLeft size={18}/>Cancelar</Button> : <Button variante="texto" onClick={() => setPasso(0)}><ArrowLeft size={18}/>Voltar</Button>}
          {passo === 0 ? <Button onClick={() => setPasso(1)}>Próxima etapa <ArrowRight size={18}/></Button> : <Button onClick={() => { const eq = equipamentos.find((x) => x.id === sel) ?? equipamentos[0]; const n = enviarChamado({ equipamento: eq ? rotuloEquip(eq) : 'Equipamento', categoria: cat === 'Outros' && outra ? outra : cat, descricao: desc, garantia: eq?.garantia ?? 'Sem garantia' }); setNumero(n.id); setEnviado(true); }}>Enviar chamado</Button>}
        </div>
      </Card>

      <Popup aberto={enviado} onClose={() => nav('/chamados')} id="enviado" largura={460}>
        <div className="flex flex-col items-center text-center">
          <svg width="72" height="72" viewBox="0 0 72 72" aria-hidden><circle cx="36" cy="36" r="34" fill="var(--cor-fundo-sucesso)"/><path className="check-draw" d="M22 37l10 10 18-20" fill="none" stroke="var(--cor-sucesso)" strokeWidth="5" strokeLinecap="round" strokeLinejoin="round"/></svg>
          <h2 id="enviado-t" className="mt-5 texto-h2">Chamado enviado com sucesso</h2>
          <p className="mt-4 rounded-md bg-primaria-clara px-4 py-3 texto-h3 text-primaria-escura">Número do chamado: <span className="num">{numero}</span></p>
          <p className="mt-4 texto-pequeno text-texto-secundario">Redirecionando para o histórico em {Math.max(seg, 0)}s.</p>
          <Button to="/chamados" className="mt-6 w-full">Ir para histórico de chamados</Button>
        </div>
      </Popup>
    </div>);
}

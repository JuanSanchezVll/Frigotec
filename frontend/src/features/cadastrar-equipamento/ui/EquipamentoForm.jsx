import { Barcode, CheckCircle2 } from 'lucide-react';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useApp } from '../../../entities/estado';
import { dataIso, listarFabricantes, listarModelos } from '../../../entities/equipamento';
import { Alert, AttachArea, Badge, Button, Card, Combo, DateInput, Field, Popup, TextInput } from '../../../shared/ui';

const estadoInicial = { serie: '', marca: '', modelo: '', garantia: 'Garantia comum', data: '' };

/** Cadastro de equipamento do cliente. Marcas e modelos vêm do catálogo do backend; só a marca própria (FrigoPro) pede garantia. */
export function EquipamentoForm() {
    const [v, setV] = useState(estadoInicial);
    const [anexos, setAnexos] = useState([]);
    const [fabricantes, setFabricantes] = useState([]);
    const [modelos, setModelos] = useState([]);
    const [ok, setOk] = useState(false);
    const [criado, setCriado] = useState(null);
    const { addEquipamento, anexarAoEquipamento } = useApp();
    const nav = useNavigate();
    const set = (k) => (x) => setV((p) => ({ ...p, [k]: x }));

    const fabricante = fabricantes.find((f) => f.nome === v.marca);
    const modelo = modelos.find((m) => m.modelo === v.modelo);
    const propria = !!fabricante?.propria;

    useEffect(() => { listarFabricantes().then(setFabricantes).catch(() => setFabricantes([])); }, []);
    useEffect(() => {
        if (!fabricante) { setModelos([]); return; }
        listarModelos(fabricante.id).then(setModelos).catch(() => setModelos([]));
    }, [fabricante]);

    const novo = () => { setV(estadoInicial); setAnexos([]); setOk(false); };
    const podeCadastrar = !!modelo && (!propria || !!dataIso(v.data));
    const cadastrar = async () => {
        const r = await addEquipamento({
            equipamentoId: modelo.id,
            numeroSerie: v.serie.trim() || undefined,
            ...(propria ? { tipoGarantia: v.garantia, dataCompra: dataIso(v.data), notaFiscal: anexos[0]?.nome } : {}),
        });
        if (r) {
            if (propria && anexos[0]?.arquivo) await anexarAoEquipamento(r.id, [anexos[0].arquivo]);
            setCriado(r);
            setOk(true);
        }
    };

    return (<>
      <div className="grid gap-6 lg:grid-cols-2">
        <Card className="space-y-6">
          <h2 className="texto-h3">Dados do equipamento</h2>
          <TextInput label="Número de série do produto" value={v.serie} onChange={(e) => set('serie')(e.target.value)} placeholder="Digite o número de série" hint="Fica na etiqueta do equipamento. Opcional, mas não pode repetir no mesmo modelo."/>
          <div aria-hidden className="flex items-center gap-4 rounded-md border border-dashed border-borda-campo bg-superficie p-4">
            <div className="flex-1 space-y-1.5"><p className="texto-legenda font-bold uppercase tracking-wide text-texto-secundario">Etiqueta de exemplo</p><div className="flex items-end gap-px">{Array.from({ length: 34 }).map((_, i) => <span key={i} style={{ height: 20 + ((i * 7) % 4) * 4, width: i % 3 === 0 ? 3 : 1.5 }} className="bg-texto"/>)}</div><p className="texto-legenda font-mono">N/S: FP-2026-009001</p></div><Barcode size={36} className="text-texto-secundario"/>
          </div>
          <Combo label="Marca" options={fabricantes.map((f) => f.nome)} value={v.marca} onChange={(x) => setV((p) => ({ ...p, marca: x, modelo: '' }))} soLista placeholder="Escolha a marca"/>
          {v.marca && <div className="anim-fade"><Combo label="Modelo" options={modelos.map((m) => m.modelo)} value={v.modelo} onChange={set('modelo')} soLista placeholder="Escolha o modelo"/></div>}
        </Card>
        {propria && (<Card className="anim-slide space-y-6">
            <h2 className="texto-h3">Garantia e nota fiscal</h2>
            <Combo label="Tipo de garantia" options={['Garantia comum', 'Garantia estendida']} value={v.garantia} onChange={set('garantia')} soLista/>
            <DateInput label="Data da compra" value={v.data} onChange={set('data')}/>
            <Alert tipo="alerta">Pedir a garantia não quer dizer que ela foi aprovada: a Frigotec confere a nota fiscal e a data da compra.</Alert>
            <Field label="Nota fiscal"><AttachArea itens={anexos} onChange={setAnexos} multiplo={false} accept="application/pdf,image/jpeg,image/png" titulo="Arraste a nota fiscal aqui ou clique para escolher (PDF, JPG ou PNG)"/></Field>
          </Card>)}
      </div>
      <div className="mt-8 flex justify-end"><Button tamanho="lg" disabled={!podeCadastrar} onClick={cadastrar}>Cadastrar</Button></div>
      <Popup aberto={ok} onClose={novo} id="eq-ok" largura={440}>
        <div className="flex flex-col items-center text-center">
          <svg width="72" height="72" viewBox="0 0 72 72" aria-hidden><circle cx="36" cy="36" r="34" fill="var(--cor-fundo-sucesso)"/><path className="check-draw" d="M22 37l10 10 18-20" fill="none" stroke="var(--cor-sucesso)" strokeWidth="5" strokeLinecap="round" strokeLinejoin="round"/></svg>
          <h2 id="eq-ok-t" className="mt-5 texto-h2">Equipamento cadastrado</h2>
          <p className="mt-2 texto-h3 text-texto-secundario">{criado?.tipo} · {criado?.marca}</p>
          {criado && criado.garantia !== 'Sem garantia' && <div className="mt-4"><Badge texto={criado.garantia}/></div>}
          <div className="mt-8 flex w-full flex-col gap-3">
            <Button onClick={() => nav('/chamados/novo', { state: { equipamento: criado?.id } })}>Abrir chamado agora</Button>
            <Button variante="contorno" onClick={novo}><CheckCircle2 size={18}/>Cadastrar novo equipamento</Button>
          </div>
        </div>
      </Popup>
    </>);
}

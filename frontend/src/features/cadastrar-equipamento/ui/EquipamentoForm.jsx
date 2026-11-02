import { Barcode, CheckCircle2 } from 'lucide-react';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useApp } from '../../../entities/estado';
import { marcasLista, modelosPorMarca } from '../../../entities/equipamento';
import { Alert, AttachArea, Badge, Button, Card, Combo, DateInput, Field, Popup, TextInput } from '../../../shared/ui';
const estadoInicial = { serie: 'FP-2026-009001', marca: 'Frigopro', modelo: 'Chapa', garantia: 'Garantia comum', data: '25/08/2025' };
const nf = [{ id: 'nf', nome: 'nota-fiscal-chapa.pdf', tamanho: '2,1 MB' }];
/** Cadastro de equipamento. Sem validação. */
export function EquipamentoForm() {
    const [v, setV] = useState(estadoInicial);
    const [anexos, setAnexos] = useState(nf);
    const [ok, setOk] = useState(false);
    const [criado, setCriado] = useState(null);
    const { addEquipamento } = useApp();
    const nav = useNavigate();
    const set = (k) => (x) => setV((p) => ({ ...p, [k]: x }));
    const frigopro = v.marca.toLowerCase() === 'frigopro';
    const novo = () => { setV({ serie: '', marca: '', modelo: '', garantia: '', data: '' }); setAnexos([]); setOk(false); };
    return (<>
      <div className="grid gap-6 lg:grid-cols-2">
        <Card className="space-y-6">
          <h2 className="texto-h3">Dados do equipamento</h2>
          {/* VALIDACAO: número de série obrigatório */}
          <TextInput label="Número de série do produto" value={v.serie} onChange={(e) => set('serie')(e.target.value)} placeholder="Digite o número de série" hint="Fica na etiqueta do equipamento."/>
          <div aria-hidden className="flex items-center gap-4 rounded-md border border-dashed border-borda-campo bg-superficie p-4">
            <div className="flex-1 space-y-1.5"><p className="texto-legenda font-bold uppercase tracking-wide text-texto-secundario">Etiqueta de exemplo</p><div className="flex items-end gap-px">{Array.from({ length: 34 }).map((_, i) => <span key={i} style={{ height: 20 + ((i * 7) % 4) * 4, width: i % 3 === 0 ? 3 : 1.5 }} className="bg-texto"/>)}</div><p className="texto-legenda font-mono">N/S: FP-2026-009001</p></div><Barcode size={36} className="text-texto-secundario"/>
          </div>
          {/* VALIDACAO: marca obrigatória e dentro da lista */}
          <Combo label="Marca" options={marcasLista} value={v.marca} onChange={set('marca')} placeholder="Digite ou escolha a marca"/>
          {v.marca && <div className="anim-fade"><Combo label="Modelo" options={modelosPorMarca[v.marca] ?? ['Modelo padrão']} value={v.modelo} onChange={set('modelo')} placeholder="Digite ou escolha o modelo"/></div>}
        </Card>

        {frigopro && (<Card className="anim-slide space-y-6">
            <h2 className="texto-h3">Garantia e nota fiscal</h2>
            <Combo label="Tipo de garantia" options={['Garantia comum', 'Garantia estendida']} value={v.garantia} onChange={set('garantia')} soLista/>
            {/* VALIDACAO: data da compra obrigatória ao pedir garantia; não pode ser futura */}
            <DateInput label="Data da compra" value={v.data} onChange={set('data')}/>
            <Alert tipo="alerta">Pedir a garantia não quer dizer que ela foi aprovada: a Frigotec confere a nota fiscal e a data da compra.</Alert>
            {/* VALIDACAO: nota fiscal obrigatória ao pedir garantia */}
            <Field label="Nota fiscal"><AttachArea itens={anexos} onChange={setAnexos} multiplo={false} titulo="Arraste a nota fiscal aqui ou clique para escolher"/></Field>
          </Card>)}
      </div>
      <div className="mt-8 flex justify-end"><Button tamanho="lg" onClick={() => { setCriado(addEquipamento({ tipo: v.modelo || 'Equipamento', marca: v.marca || 'Sem marca', serie: v.serie || 'S/N', garantia: frigopro && v.garantia ? 'Garantia em análise' : 'Sem garantia' })); setOk(true); }}>Cadastrar</Button></div>

      <Popup aberto={ok} onClose={novo} id="eq-ok" largura={440}>
        <div className="flex flex-col items-center text-center">
          <svg width="72" height="72" viewBox="0 0 72 72" aria-hidden><circle cx="36" cy="36" r="34" fill="var(--cor-fundo-sucesso)"/><path className="check-draw" d="M22 37l10 10 18-20" fill="none" stroke="var(--cor-sucesso)" strokeWidth="5" strokeLinecap="round" strokeLinejoin="round"/></svg>
          <h2 id="eq-ok-t" className="mt-5 texto-h2">Equipamento cadastrado</h2>
          <p className="mt-2 texto-h3 text-texto-secundario">{v.modelo || 'Chapa'} · {v.marca || 'Frigopro'}</p>
          {frigopro && <div className="mt-4"><Badge texto="Garantia em análise"/></div>}
          <div className="mt-8 flex w-full flex-col gap-3">
            <Button onClick={() => nav('/chamados/novo', { state: { equipamento: criado?.id } })}>Abrir chamado agora</Button>
            <Button variante="contorno" onClick={novo}><CheckCircle2 size={18}/>Cadastrar novo equipamento</Button>
          </div>
        </div>
      </Popup>
    </>);
}

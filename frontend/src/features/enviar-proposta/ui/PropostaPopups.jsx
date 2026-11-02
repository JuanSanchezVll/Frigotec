import { Plus, Trash2 } from 'lucide-react';
import { useState } from 'react';
import { useApp } from '../../../entities/estado';
import { brl, itensIniciais } from '../../../entities/proposta';
import { AttachArea, Button, Combo, DateInput, Field, Popup, TextArea, TextInput } from '../../../shared/ui';
export function AgendarPopup({ aberto, onClose, chamadoId, escolher = false }) {
    const { enviar, meus } = useApp();
    const [cid, setCid] = useState(chamadoId);
    const [data, setData] = useState('30/09/2026');
    const [hora, setHora] = useState('Manhã');
    const [end, setEnd] = useState('Rua das Palmeiras, 100, São Paulo');
    return (<Popup aberto={aberto} onClose={onClose} id="agendar" titulo="Agendar visita" largura={500}>
      <div className="mt-6 space-y-5">
        {/* VALIDACAO: data futura, horário e endereço obrigatórios */}
        {escolher && <Combo label="Chamado" options={meus.map((c) => c.id)} value={cid} onChange={setCid} soLista placeholder="Escolha o chamado"/>}
        <DateInput label="Data" value={data} onChange={setData}/>
        <Combo label="Horário" options={['Manhã', 'Tarde']} value={hora} onChange={setHora} soLista/>
        <TextInput label="Endereço" value={end} onChange={(e) => setEnd(e.target.value)}/>
        <TextArea label="Observação" rows={3} placeholder="Alguma orientação para o cliente?"/>
      </div>
      <div className="mt-8 flex justify-end gap-3"><Button variante="contorno" onClick={onClose}>Cancelar</Button>
        <Button onClick={() => { enviar(escolher ? cid : chamadoId, { de: 'tecnico', tipo: 'agendamento', titulo: 'Agendamento de visita', data, periodo: hora === 'Tarde' ? 'Tarde' : 'Manhã', endereco: end, detalhe: `${data} · ${hora.toLowerCase()} · ${end}`, status: 'Pendente' }); onClose(); }}>Enviar agendamento</Button></div>
    </Popup>);
}
export function OrcamentoPopup({ aberto, onClose, chamadoId }) {
    const { enviar } = useApp();
    const [itens, setItens] = useState(itensIniciais);
    const [anexo, setAnexo] = useState([]);
    const total = itens.reduce((s, i) => s + i.qtd * i.valor, 0);
    const alt = (id, k, v) => setItens((l) => l.map((i) => (i.id === id ? { ...i, [k]: k === 'descricao' ? v : Number(v) || 0 } : i)));
    return (<Popup aberto={aberto} onClose={onClose} id="orcamento" titulo="Proposta de orçamento" largura={680}>
      <div className="mt-6 space-y-5">
        {/* VALIDACAO: ao menos um item; quantidade e valor numéricos maiores que zero */}
        <div className="overflow-x-auto"><table className="w-full min-w-[460px] text-left">
          <thead className="texto-rotulo text-texto-secundario"><tr><th className="pb-2">Descrição</th><th className="pb-2 pl-2">Qtd.</th><th className="pb-2 pl-2">Valor unitário</th><th /></tr></thead>
          <tbody>{itens.map((i) => (<tr key={i.id}>
              <td className="py-1"><input aria-label="Descrição" className="campo" value={i.descricao} onChange={(e) => alt(i.id, 'descricao', e.target.value)}/></td>
              <td className="py-1 pl-2"><input aria-label="Quantidade" className="campo w-20" value={i.qtd} onChange={(e) => alt(i.id, 'qtd', e.target.value)}/></td>
              <td className="py-1 pl-2"><input aria-label="Valor unitário" className="campo w-32" value={i.valor} onChange={(e) => alt(i.id, 'valor', e.target.value)}/></td>
              <td className="py-1 pl-1"><button type="button" aria-label="Remover item" onClick={() => setItens(itens.filter((x) => x.id !== i.id))} className="flex h-11 w-11 items-center justify-center rounded-md text-texto-secundario hover:bg-superficie"><Trash2 size={18}/></button></td>
            </tr>))}</tbody>
        </table></div>
        <div className="flex items-center justify-between"><Button variante="texto" tamanho="sm" onClick={() => setItens([...itens, { id: Date.now(), descricao: 'Novo item', qtd: 1, valor: 100 }])}><Plus size={16}/>Adicionar item</Button><p className="texto-h3">Total: {brl(total)}</p></div>
        <TextInput label="Validade" defaultValue="7 dias"/>
        <Field label="Proposta em PDF" optional><AttachArea itens={anexo} onChange={setAnexo} multiplo={false} titulo="Anexar PDF da proposta"/></Field>
      </div>
      <div className="mt-8 flex justify-end gap-3"><Button variante="contorno" onClick={onClose}>Cancelar</Button>
        <Button onClick={() => { enviar(chamadoId, { de: 'tecnico', tipo: 'proposta', titulo: 'Proposta de orçamento', detalhe: `${itens.length} itens · validade 7 dias`, valor: brl(total), validade: '7 dias', status: 'Pendente' }); onClose(); }}>Enviar proposta</Button></div>
    </Popup>);
}
export function PrazoPopup({ aberto, onClose, chamadoId }) {
    const { enviar } = useApp();
    const [dias, setDias] = useState('5');
    return (<Popup aberto={aberto} onClose={onClose} id="prazo" titulo="Prazo do atendimento" largura={460}>
      <div className="mt-6 space-y-5">
        {/* VALIDACAO: prazo em dias úteis, número inteiro maior que zero */}
        <TextInput label="Prazo em dias úteis" value={dias} onChange={(e) => setDias(e.target.value)}/>
        <TextArea label="Observação" rows={3} placeholder="Ex.: o prazo começa após a aprovação do orçamento"/>
      </div>
      <div className="mt-8 flex justify-end gap-3"><Button variante="contorno" onClick={onClose}>Cancelar</Button>
        <Button onClick={() => { enviar(chamadoId, { de: 'tecnico', tipo: 'prazo', titulo: 'Prazo do atendimento', detalhe: `${dias} dias úteis`, status: 'Pendente' }); onClose(); }}>Enviar prazo</Button></div>
    </Popup>);
}

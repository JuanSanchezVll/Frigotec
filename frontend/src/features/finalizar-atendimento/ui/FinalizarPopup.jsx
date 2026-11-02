import { useState } from 'react';
import { useApp } from '../../../entities/estado';
import { AttachArea, Button, Field, Popup, TextArea, TextInput } from '../../../shared/ui';
/** Laudo de finalização. Ao confirmar, o chamado vai para Finalizado e para o Histórico. */
export function FinalizarPopup({ id, aberto, onClose, aoFinalizar }) {
    const { finalizar } = useApp();
    const [resumo, setResumo] = useState('Troca do termostato e calibração da chapa. Teste de aquecimento uniforme concluído.');
    const [pecas, setPecas] = useState('Termostato industrial');
    const [obs, setObs] = useState('');
    const [fotos, setFotos] = useState([]);
    return (<Popup aberto={aberto} onClose={onClose} id="finalizar" titulo="Finalizar atendimento" largura={620}>
      <div className="mt-6 space-y-5">
        {/* VALIDACAO: resumo do serviço obrigatório */}
        <TextArea label="Resumo do serviço" rows={4} value={resumo} onChange={(e) => setResumo(e.target.value)}/>
        <TextInput label="Peças trocadas" value={pecas} onChange={(e) => setPecas(e.target.value)}/>
        <TextArea label="Observações" rows={3} value={obs} onChange={(e) => setObs(e.target.value)} placeholder="Recomendações para o cliente"/>
        <Field label="Fotos do atendimento" optional><AttachArea itens={fotos} onChange={setFotos}/></Field>
      </div>
      <div className="mt-8 flex justify-end gap-3"><Button variante="contorno" onClick={onClose}>Cancelar</Button>
        <Button onClick={() => { finalizar(id, { resumo, pecas, obs: obs || '—' }); onClose(); aoFinalizar?.(); }}>Confirmar finalização</Button></div>
    </Popup>);
}

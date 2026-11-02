import { FileText } from 'lucide-react';
import { useApp } from '../../../entities/estado';
import { Badge, Button, Popup } from '../../../shared/ui';
/** Resumo da proposta com Aceitar e Recusar. */
export function AceitarOrcamentoPopup({ chamadoId, proposta, aberto, onClose }) {
    const { atualizarStatus } = useApp();
    return (<Popup aberto={aberto} onClose={onClose} id="aceitar" titulo="Orçamento do atendimento" largura={480}>
      {proposta ? (<>
          <div className="mt-5 rounded-lg border border-borda p-4">
            <div className="flex items-center gap-3"><span className="flex h-11 w-11 items-center justify-center rounded-md bg-primaria-clara text-primaria"><FileText size={22}/></span><div className="flex-1"><p className="font-semibold">{proposta.titulo}</p><p className="texto-pequeno text-texto-secundario">{proposta.detalhe}</p></div><Badge texto={proposta.status ?? 'Pendente'}/></div>
            <p className="num mt-4 text-3xl font-extrabold tracking-tight">{proposta.valor}</p>
          </div>
          <div className="mt-8 flex justify-end gap-3">
            <Button variante="contorno" onClick={() => { atualizarStatus(chamadoId, proposta.id, 'Recusada'); onClose(); }}>Recusar</Button>
            <Button onClick={() => { atualizarStatus(chamadoId, proposta.id, 'Aceita'); onClose(); }}>Aceitar orçamento</Button>
          </div>
        </>) : (<><p className="mt-5 text-texto-secundario">O técnico ainda não enviou um orçamento para este chamado. Quando enviar, ele aparece aqui.</p><div className="mt-8 flex justify-end"><Button variante="contorno" onClick={onClose}>Fechar</Button></div></>)}
    </Popup>);
}

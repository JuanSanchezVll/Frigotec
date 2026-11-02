import { useNavigate } from 'react-router-dom';
import { useApp } from '../../../entities/estado';
import { Button, Popup } from '../../../shared/ui';
export function CancelarPopup({ id, aberto, onClose }) {
    const { cancelar } = useApp();
    const nav = useNavigate();
    return (<Popup aberto={aberto} onClose={onClose} id="cancelar" titulo="Cancelar este chamado?" largura={440}>
      <p className="mt-4 text-texto-secundario">O técnico será avisado e o chamado <span className="num font-semibold text-texto">{id}</span> deixa de aparecer como em andamento.</p>
      <div className="mt-8 flex justify-end gap-3"><Button variante="contorno" onClick={onClose}>Manter chamado</Button><Button variante="secundario" onClick={() => { cancelar(id); onClose(); nav('/chamados'); }}>Cancelar chamado</Button></div>
    </Popup>);
}

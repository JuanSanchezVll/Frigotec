import { useState } from 'react';
import { useApp } from '../../../entities/estado';
import { Button, Field, Popup } from '../../../shared/ui';

/** O técnico responsável marca (ou remarca) a visita: data e hora futuras (SDD RN-CHM-14). */
export function AgendarVisitaPopup({ id, aberto, onClose }) {
    const { agendarVisita } = useApp();
    const [quando, setQuando] = useState('');
    const confirmar = async () => {
        if (!quando) return;
        // datetime-local devolve "2026-12-01T14:00"; o backend aceita com segundos.
        const ok = await agendarVisita(id, `${quando}:00`);
        if (ok) { setQuando(''); onClose(); }
    };
    return (<Popup aberto={aberto} onClose={onClose} id="agendar-visita" titulo="Agendar visita" largura={460}>
      <div className="mt-6">
        <Field label="Data e hora da visita" htmlFor="visita-quando">
          <input id="visita-quando" type="datetime-local" className="campo" value={quando} onChange={(e) => setQuando(e.target.value)}/>
        </Field>
      </div>
      <div className="mt-8 flex justify-end gap-3">
        <Button variante="contorno" onClick={onClose}>Cancelar</Button>
        <Button onClick={confirmar} disabled={!quando}>Confirmar visita</Button>
      </div>
    </Popup>);
}

import { Minus, Plus, ZoomIn } from 'lucide-react';
import { useState } from 'react';
import { useApp } from '../../../entities/estado';
import { Button, Field, Popup } from '../../../shared/ui';
import { NotaFiscal } from './NotaFiscal';

/** Análise de garantia pelo técnico (SDD RN-GAR-03): VALIDADA exige a data de fim; RECUSADA encerra a análise. */
export function GarantiaPopup({ id, equipamento, dataCompra, aberto, onClose }) {
    const { validarGarantia } = useApp();
    const [decisao, setDecisao] = useState('aprovar');
    const [fim, setFim] = useState('');
    const [zoom, setZoom] = useState(1);
    const confirmar = async () => {
        const ok = await validarGarantia(id, decisao === 'aprovar' ? { validacao: 'VALIDADA', dataFimGarantia: fim } : { validacao: 'RECUSADA' });
        if (ok) onClose();
    };
    return (<Popup aberto={aberto} onClose={onClose} id="garantia" titulo="Análise de garantia" largura={760}>
      <div className="mt-6 grid gap-6 md:grid-cols-2">
        <div>
          <NotaFiscal zoom={zoom}/>
          <div className="mt-2 flex items-center gap-2"><Button variante="contorno" tamanho="icone" aria-label="Diminuir zoom" onClick={() => setZoom(1)}><Minus size={18}/></Button><Button variante="contorno" tamanho="icone" aria-label="Aumentar zoom" onClick={() => setZoom(1.6)}><Plus size={18}/></Button><span className="flex items-center gap-1 texto-pequeno text-texto-secundario"><ZoomIn size={16}/>Zoom {Math.round(zoom * 100)}%</span></div>
        </div>
        <div className="space-y-5">
          <dl className="space-y-3 texto-pequeno">
            <div><dt className="texto-rotulo text-texto-secundario">Equipamento</dt><dd className="texto-corpo font-semibold">{equipamento}</dd></div>
            <div><dt className="texto-rotulo text-texto-secundario">Data da compra</dt><dd className="texto-corpo font-semibold">{dataCompra || '—'}</dd></div>
          </dl>
          <div role="radiogroup" aria-label="Decisão" className="grid grid-cols-2 gap-1 rounded-md border border-borda bg-superficie p-1">
            {['aprovar', 'recusar'].map((d) => (<button key={d} type="button" role="radio" aria-checked={decisao === d} onClick={() => setDecisao(d)} className={`min-h-11 rounded-sm px-2 texto-pequeno font-semibold transition-all ${decisao === d ? (d === 'aprovar' ? 'bg-sucesso text-fundo' : 'bg-secundaria text-fundo') : 'text-texto-secundario'}`}>{d === 'aprovar' ? 'Aprovar garantia' : 'Recusar garantia'}</button>))}
          </div>
          {decisao === 'aprovar' && <div className="anim-fade"><Field label="Garantia válida até" htmlFor="garantia-fim"><input id="garantia-fim" type="date" className="campo" value={fim} onChange={(e) => setFim(e.target.value)}/></Field></div>}
        </div>
      </div>
      <div className="mt-8 flex justify-end gap-3">
        <Button variante="contorno" onClick={onClose}>Cancelar</Button>
        <Button onClick={confirmar} disabled={decisao === 'aprovar' && !fim}>Confirmar</Button>
      </div>
    </Popup>);
}

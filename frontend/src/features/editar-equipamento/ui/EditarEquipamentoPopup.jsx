import { useState } from 'react';
import { useApp } from '../../../entities/estado';
import { Button, Popup, TextInput } from '../../../shared/ui';

/**
 * Corrige a série e/ou a nota fiscal da própria unidade (PATCH /equipamentos/{id}). O back-end recusa série repetida
 * no mesmo modelo (409) e nota fiscal em marca sem garantia (422); a mensagem dele aparece no aviso do topo.
 */
export function EditarEquipamentoPopup({ equipamento, onClose }) {
    const { editarEquipamento } = useApp();
    const [serie, setSerie] = useState(equipamento?.serie ?? '');
    const [nota, setNota] = useState('');
    const comGarantia = equipamento && equipamento.garantia !== 'Sem garantia';
    const salvar = async () => {
        const dados = {};
        if (serie.trim() !== (equipamento.serie ?? '')) dados.numeroSerie = serie.trim();
        if (comGarantia && nota.trim()) dados.notaFiscal = nota.trim();
        if (Object.keys(dados).length === 0) { onClose(); return; }
        if (await editarEquipamento(equipamento.id, dados)) onClose();
    };
    return (<Popup aberto={!!equipamento} onClose={onClose} id="editar-equipamento" largura={460}>
      <h2 id="editar-equipamento-t" className="texto-h2">Editar equipamento</h2>
      <p className="mt-1 text-texto-secundario">{equipamento?.tipo} · {equipamento?.marca}</p>
      <div className="mt-6 space-y-5">
        <TextInput label="Número de série" value={serie} onChange={(e) => setSerie(e.target.value)} placeholder="Digite o número de série" hint="Não pode repetir outra unidade do mesmo modelo."/>
        {comGarantia && <TextInput label="Nota fiscal (número)" value={nota} onChange={(e) => setNota(e.target.value)} placeholder="Número da nota fiscal" hint="Para enviar o arquivo da nota, cadastre-a ao registrar o equipamento."/>}
      </div>
      <div className="mt-8 flex gap-3"><Button variante="contorno" className="flex-1" onClick={onClose}>Cancelar</Button><Button className="flex-1" onClick={salvar}>Salvar</Button></div>
    </Popup>);
}

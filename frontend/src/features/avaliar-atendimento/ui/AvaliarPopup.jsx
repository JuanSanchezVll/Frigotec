import { Download, Star } from 'lucide-react';
import { useState } from 'react';
import { useApp } from '../../../entities/estado';
import { Button, Popup, TextArea } from '../../../shared/ui';
/** Avaliação do atendimento (estrelas e comentário) e resumo do serviço. */
export function AvaliarPopup({ c, aberto, onClose }) {
    const { avaliar } = useApp();
    const [nota, setNota] = useState(c.avaliacao?.nota ?? 5);
    const [com, setCom] = useState(c.avaliacao?.comentario ?? '');
    return (<Popup aberto={aberto} onClose={onClose} id="avaliar" titulo="Avaliar atendimento" largura={520}>
      {c.laudo && (<div className="mt-5 rounded-md bg-superficie p-4 texto-pequeno"><p className="texto-rotulo">Resumo do atendimento</p><p className="mt-1">{c.laudo.resumo}</p><p className="mt-2 text-texto-secundario">Peças trocadas: {c.laudo.pecas}</p>
          <Button variante="texto" tamanho="sm" className="mt-2 -ml-4"><Download size={16}/>Baixar resumo do atendimento</Button></div>)}
      <div className="mt-6" role="radiogroup" aria-label="Nota do atendimento">
        <p className="mb-2 texto-rotulo">Como foi o atendimento?</p>
        <div className="flex gap-1">{[1, 2, 3, 4, 5].map((n) => (<button key={n} type="button" role="radio" aria-checked={nota === n} aria-label={`${n} estrela${n > 1 ? 's' : ''}`} onClick={() => setNota(n)} className="flex h-11 w-11 items-center justify-center rounded-md transition-transform hover:scale-110">
            <Star size={30} className={n <= nota ? 'text-brasa' : 'text-borda'} fill={n <= nota ? 'currentColor' : 'none'}/>
          </button>))}</div>
      </div>
      <div className="mt-4"><TextArea label="Comentário" rows={3} value={com} onChange={(e) => setCom(e.target.value)} placeholder="Conte como foi"/></div>
      <div className="mt-8 flex justify-end gap-3"><Button variante="contorno" onClick={onClose}>Fechar</Button><Button onClick={() => { avaliar(c.id, nota, com); onClose(); }}>Enviar avaliação</Button></div>
    </Popup>);
}

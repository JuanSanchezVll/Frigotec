import { FileText, Plus, X } from 'lucide-react';
import { useRef, useState } from 'react';
const fmt = (b) => (b < 1048576 ? `${Math.max(1, Math.round(b / 1024))} KB` : `${(b / 1048576).toFixed(1)} MB`);
/** Área de anexo com arrastar e soltar. Cada item guarda o File (campo arquivo) para ser enviado ao back-end. */
export function AttachArea({ itens, onChange, multiplo = true, accept, titulo = 'Arraste arquivos aqui ou clique para escolher' }) {
    const input = useRef(null);
    const [arrasta, setArrasta] = useState(false);
    const add = (fs) => {
        if (!fs)
            return;
        const novos = Array.from(fs).map((f) => ({ id: `${f.name}-${Date.now()}-${Math.random()}`, nome: f.name, arquivo: f, tamanho: fmt(f.size), thumb: f.type.startsWith('image/') ? URL.createObjectURL(f) : undefined }));
        onChange(multiplo ? [...itens, ...novos] : novos.slice(0, 1));
    };
    return (<div>
      <input ref={input} type="file" hidden accept={accept} multiple={multiplo} onChange={(e) => { add(e.target.files); e.target.value = ''; }}/>
      <button type="button" onClick={() => input.current?.click()} onDragOver={(e) => { e.preventDefault(); setArrasta(true); }} onDragLeave={() => setArrasta(false)} onDrop={(e) => { e.preventDefault(); setArrasta(false); add(e.dataTransfer.files); }} className={`flex min-h-36 w-full flex-col items-center justify-center gap-2 rounded-lg border-2 border-dashed px-4 text-center transition-colors ${arrasta ? 'border-primaria bg-primaria-clara' : 'border-borda-campo bg-superficie hover:border-primaria hover:bg-primaria-clara'}`}>
        <span className="flex h-11 w-11 items-center justify-center rounded-full bg-fundo text-primaria shadow-1"><Plus size={22}/></span>
        <span className="texto-pequeno font-semibold text-texto">{titulo}</span>
      </button>
      {itens.length > 0 && (<ul className="mt-4 space-y-3">
          {itens.map((a) => (<li key={a.id} className="anim-fade flex items-center gap-3 rounded-md border border-borda bg-fundo p-2 pr-1 shadow-1">
              {a.thumb ? <img src={a.thumb} alt={`Miniatura de ${a.nome}`} className="h-12 w-12 rounded-sm object-cover"/>
                    : <span className="flex h-12 w-12 items-center justify-center rounded-sm bg-primaria-clara text-primaria"><FileText size={22}/></span>}
              <span className="min-w-0 flex-1"><span className="block truncate texto-pequeno font-semibold">{a.nome}</span><span className="texto-legenda text-texto-secundario">{a.tamanho}</span></span>
              <button type="button" aria-label={`Remover ${a.nome}`} onClick={() => onChange(itens.filter((x) => x.id !== a.id))} className="flex h-11 w-11 items-center justify-center rounded-md text-texto-secundario hover:bg-superficie hover:text-texto"><X size={18}/></button>
            </li>))}
        </ul>)}
    </div>);
}

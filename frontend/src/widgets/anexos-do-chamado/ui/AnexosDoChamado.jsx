import { Film } from 'lucide-react';
import { useEffect, useState } from 'react';
import { listarAnexosDoChamado, urlDoArquivo } from '../../../entities/anexo';

/**
 * Fotos e vídeo que o cliente enviou no chamado, vindos do back-end (GET /chamados/{id}/anexos).
 * O conteúdo exige o token JWT, então cada arquivo é baixado como blob e mostrado a partir de um endereço temporário.
 */
export function AnexosDoChamado({ chamadoId, colunas = 3 }) {
    const [itens, setItens] = useState(null);
    useEffect(() => {
        let ativo = true;
        if (!chamadoId) return undefined;
        listarAnexosDoChamado(chamadoId)
            .then((lista) => Promise.all(lista.map((a) => (a.categoria === 'FOTO' ? urlDoArquivo(a).then((src) => ({ ...a, src })) : Promise.resolve(a)))))
            .then((lista) => { if (ativo) setItens(lista); })
            .catch(() => { if (ativo) setItens([]); });
        return () => { ativo = false; };
    }, [chamadoId]);
    const abrir = (a) => urlDoArquivo(a).then((u) => window.open(u, '_blank', 'noopener'));
    if (itens === null) return <p className="texto-pequeno text-texto-secundario">Carregando arquivos…</p>;
    if (itens.length === 0) return <p className="texto-pequeno text-texto-secundario">Nenhum arquivo enviado neste chamado.</p>;
    return (<ul className={`grid gap-3 ${colunas === 2 ? 'grid-cols-2' : 'grid-cols-3'}`}>
      {itens.map((a) => (<li key={a.id}>
          {a.categoria === 'FOTO'
            ? <button type="button" onClick={() => abrir(a)} className="block w-full" aria-label={`Abrir ${a.nome}`}><img src={a.src} alt={a.nome} loading="lazy" className="aspect-square w-full rounded-md object-cover shadow-1"/></button>
            : <button type="button" onClick={() => abrir(a)} className="flex aspect-square w-full flex-col items-center justify-center gap-1 rounded-md bg-primaria-clara text-primaria shadow-1"><Film size={26} aria-hidden/><span className="texto-legenda font-semibold">Abrir vídeo</span></button>}
        </li>))}
    </ul>);
}

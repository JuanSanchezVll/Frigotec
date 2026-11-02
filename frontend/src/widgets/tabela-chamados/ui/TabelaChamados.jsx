import { ArrowRight } from 'lucide-react';
import { Link } from 'react-router-dom';
import { statusDe } from '../../../entities/chamado';
import { Badge, Button } from '../../../shared/ui';
/** Tabela de chamados do cliente (cartões no celular). */
export function TabelaChamados({ lista }) {
    if (!lista.length)
        return <p className="py-16 text-center texto-corpo text-texto-secundario">Você não tem chamados nesta lista. Quando abrir um, ele aparece aqui.</p>;
    return (<>
      <div className="hidden max-h-[640px] overflow-auto rounded-lg border border-borda bg-fundo shadow-1 md:block">
        <table className="w-full text-left">
          <thead className="sticky top-0 z-10 bg-superficie texto-rotulo text-texto-secundario">
            <tr><th className="px-6 py-4">Equipamento</th><th className="px-4 py-4">Status</th><th className="px-4 py-4">Última atualização</th><th className="px-6 py-4 text-right">Acompanhamento</th></tr>
          </thead>
          <tbody>
            {lista.map((c) => (<tr key={c.id} className="border-t border-borda transition-colors hover:bg-primaria-clara/50">
                <td className="px-6 py-4"><p className="font-semibold">{c.equipamento}</p><Link to={`/chamados/${c.id}`} className="texto-pequeno font-medium text-primaria hover:underline"><span className="num">Chamado {c.id}</span></Link></td>
                <td className="px-4 py-4"><Badge texto={statusDe(c)}/></td>
                <td className="px-4 py-4 texto-pequeno text-texto-secundario">{c.atualizacao}</td>
                <td className="px-6 py-4 text-right"><Button to={`/chamados/${c.id}`} variante="contorno" tamanho="sm">Ver detalhes <ArrowRight size={16}/></Button></td>
              </tr>))}
          </tbody>
        </table>
      </div>
      <ul className="space-y-4 md:hidden">
        {lista.map((c) => (<li key={c.id} className="rounded-lg border border-borda bg-fundo p-4 shadow-1">
            <div className="flex items-start justify-between gap-3"><div><p className="font-semibold">{c.equipamento}</p><Link to={`/chamados/${c.id}`} className="texto-pequeno font-medium text-primaria"><span className="num">Chamado {c.id}</span></Link></div><Badge texto={statusDe(c)}/></div>
            <p className="mt-3 texto-pequeno text-texto-secundario">Última atualização: {c.atualizacao}</p>
            <Button to={`/chamados/${c.id}`} variante="contorno" tamanho="sm" className="mt-4 w-full">Ver detalhes</Button>
          </li>))}
      </ul>
    </>);
}

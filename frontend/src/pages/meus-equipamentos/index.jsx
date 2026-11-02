import { Plus, Refrigerator } from 'lucide-react';
import { useApp } from '../../entities/estado';
import { Badge, Button } from '../../shared/ui';
import { Reveal } from '../../shared/lib/reveal';
export default function Equipamentos() {
    const { equipamentos } = useApp();
    return (<main className="min-h-screen bg-superficie pb-20">
      <div className="mx-auto max-w-[1200px] px-4 pt-10 md:px-6">
        <div className="flex flex-wrap items-end justify-between gap-4">
          <div><h1 className="texto-h1">Meus equipamentos</h1><p className="mt-2 text-texto-secundario">Tudo o que você cadastrou, com a situação da garantia.</p></div>
          <Button to="/equipamentos/novo"><Plus size={18}/>Cadastrar equipamento</Button>
        </div>
        {equipamentos.length === 0 ? (<div className="mt-10 flex flex-col items-center gap-3 rounded-xl border border-dashed border-borda-campo bg-fundo p-14 text-center"><span className="flex h-16 w-16 items-center justify-center rounded-full bg-primaria-clara text-primaria"><Refrigerator size={30}/></span><p className="texto-h3">Você ainda não cadastrou equipamentos</p><p className="text-texto-secundario">Cadastre o primeiro para abrir chamados com poucos cliques.</p><Button to="/equipamentos/novo">Cadastrar equipamento</Button></div>) : (<ul className="mt-10 grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {equipamentos.map((e, i) => (<Reveal key={e.id} ordem={i}>
                <li className="card-hover flex h-full list-none flex-col rounded-xl border border-borda bg-fundo p-6 shadow-1">
                  <div className="flex items-start justify-between gap-3"><span className="flex h-12 w-12 items-center justify-center rounded-lg bg-primaria-clara text-primaria"><Refrigerator size={24} aria-hidden/></span><Badge texto={e.garantia}/></div>
                  <h2 className="mt-5 texto-h2">{e.tipo}</h2>
                  <dl className="mt-3 space-y-1 texto-pequeno"><div className="flex gap-2"><dt className="text-texto-secundario">Marca</dt><dd className="font-semibold">{e.marca}</dd></div><div className="flex gap-2"><dt className="text-texto-secundario">Modelo</dt><dd className="font-semibold">{e.tipo}</dd></div><div className="flex gap-2"><dt className="text-texto-secundario">N/S</dt><dd className="num font-semibold">{e.serie}</dd></div>{e.validade && <div className="flex gap-2"><dt className="text-texto-secundario">Garantia até</dt><dd className="num font-semibold">{e.validade}</dd></div>}</dl>
                  <Button to="/chamados/novo" state={{ equipamento: e.id }} variante="contorno" className="mt-6 w-full">Abrir chamado</Button>
                </li>
              </Reveal>))}
          </ul>)}
      </div>
    </main>);
}

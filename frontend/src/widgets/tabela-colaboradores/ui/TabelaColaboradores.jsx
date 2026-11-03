import { PERFIL } from '../../../entities/usuario';

const ROTULO = { [PERFIL.TECNICO]: 'Técnico', [PERFIL.ADMIN]: 'Administrador' };
const ESTILO = { [PERFIL.TECNICO]: 'bg-primaria-clara text-primaria-escura', [PERFIL.ADMIN]: 'bg-profundo text-fundo' };

const Perfil = ({ perfil }) => (<span className={`inline-flex h-7 items-center rounded-full px-3 texto-legenda font-semibold ${ESTILO[perfil] ?? 'bg-superficie text-texto-secundario'}`}>{ROTULO[perfil] ?? perfil}</span>);

/** Tabela de colaboradores da Frigotec (cartões no celular). Só desenha: os dados chegam por props. */
export function TabelaColaboradores({ lista, carregando }) {
    if (carregando)
        return <p className="py-16 text-center texto-corpo text-texto-secundario">Carregando colaboradores…</p>;
    if (!lista.length)
        return <p className="py-16 text-center texto-corpo text-texto-secundario">Nenhum colaborador cadastrado ainda.</p>;
    return (<>
      <div className="hidden overflow-auto rounded-lg border border-borda bg-fundo shadow-1 md:block">
        <table className="w-full text-left">
          <thead className="bg-superficie texto-rotulo text-texto-secundario">
            <tr><th className="px-6 py-4">Nome</th><th className="px-4 py-4">E-mail</th><th className="px-4 py-4">Perfil</th><th className="px-6 py-4">Celular</th></tr>
          </thead>
          <tbody>
            {lista.map((c) => (<tr key={c.id} className="border-t border-borda transition-colors hover:bg-primaria-clara/50">
                <td className="px-6 py-4 font-semibold">{c.nomeCompleto}</td>
                <td className="px-4 py-4 texto-pequeno text-texto-secundario">{c.email}</td>
                <td className="px-4 py-4"><Perfil perfil={c.perfil}/></td>
                <td className="px-6 py-4 texto-pequeno num text-texto-secundario">{c.celular}</td>
              </tr>))}
          </tbody>
        </table>
      </div>
      <ul className="space-y-4 md:hidden">
        {lista.map((c) => (<li key={c.id} className="rounded-lg border border-borda bg-fundo p-4 shadow-1">
            <div className="flex items-start justify-between gap-3"><p className="font-semibold">{c.nomeCompleto}</p><Perfil perfil={c.perfil}/></div>
            <p className="mt-2 texto-pequeno text-texto-secundario">{c.email}</p>
            <p className="mt-1 texto-pequeno num text-texto-secundario">{c.celular}</p>
          </li>))}
      </ul>
    </>);
}

import { useColaboradores } from '../../entities/usuario';
import { CadastroColaboradorForm } from '../../features/cadastro-colaborador';
import { Alert } from '../../shared/ui';
import { TabelaColaboradores } from '../../widgets/tabela-colaboradores';

/** Página /admin: o administrador vê a equipe e cadastra técnicos e administradores. */
export default function AdminColaboradores() {
    const { lista, carregando, erro, recarregar } = useColaboradores();
    return (<main className="mx-auto w-full max-w-[1200px] px-4 py-10 md:px-8">
      <h1 className="texto-h1">Colaboradores da Frigotec</h1>
      <p className="mt-2 text-texto-secundario">Técnicos e administradores. Só o administrador cria novos acessos da equipe.</p>
      {erro && <Alert tipo="alerta" className="mt-6">{erro}</Alert>}
      <div className="mt-8 grid items-start gap-8 lg:grid-cols-[1fr_480px]">
        <section aria-label="Lista de colaboradores"><TabelaColaboradores lista={lista} carregando={carregando}/></section>
        <CadastroColaboradorForm onCadastrado={recarregar}/>
      </div>
    </main>);
}

import { FileText, KeyRound, Lock, Mail, MapPin, Phone, UserRound } from 'lucide-react';
import { Alert, Button, FloatField, Segmented } from '../../../shared/ui';
import { useCadastroColaborador } from '../model/useCadastroColaborador';

/** Segmento ui da feature cadastro-colaborador (formulário do ADMIN). A regra e o envio ficam em ../model. */
export function CadastroColaboradorForm({ onCadastrado }) {
    const { campo, perfil, setPerfil, erro, sucesso, enviando, cadastrar } = useCadastroColaborador({ onCadastrado });
    return (<div className="w-full rounded-xl border border-borda bg-fundo p-6 shadow-2 md:p-8">
      <h2 className="texto-h2">Novo colaborador</h2>
      <p className="mt-2 text-texto-secundario">O colaborador entra com o e-mail e a senha que você definir aqui.</p>
      <div className="mt-6 space-y-4">
        {sucesso && <Alert tipo="sucesso">{sucesso}</Alert>}
        {erro && <Alert tipo="alerta">{erro}</Alert>}
        <Segmented label="Perfil do colaborador" value={perfil} onChange={setPerfil} opcoes={[{ value: 'TECNICO', label: 'Técnico' }, { value: 'ADMIN', label: 'Administrador' }]}/>
        <FloatField icon={UserRound} label="Nome completo" {...campo('nome')}/>
        <FloatField icon={Mail} label="E-mail" type="email" {...campo('email')}/>
        <FloatField icon={FileText} label="CPF" {...campo('cpf')}/>
        <FloatField icon={Phone} label="Telefone / celular" {...campo('celular')}/>
        <FloatField icon={Phone} label="Telefone fixo (opcional)" {...campo('telefone')}/>
        <p className="pt-2 texto-rotulo">Endereço</p>
        <div className="grid grid-cols-2 gap-4">
          <FloatField icon={MapPin} label="CEP" {...campo('cep')}/>
          <FloatField icon={MapPin} label="Estado (UF)" maxLength={2} {...campo('estado')}/>
        </div>
        <FloatField icon={MapPin} label="Cidade" {...campo('cidade')}/>
        <div className="grid grid-cols-[1fr_120px] gap-4">
          <FloatField icon={MapPin} label="Rua" {...campo('rua')}/>
          <FloatField icon={MapPin} label="Número" {...campo('numero')}/>
        </div>
        <p className="pt-2 texto-rotulo">Acesso</p>
        <FloatField icon={Lock} label="Senha (mínimo 6 caracteres)" type="password" {...campo('senha')}/>
        <FloatField icon={KeyRound} label="Confirme a senha" type="password" {...campo('confirma')}/>
      </div>
      <Button tamanho="lg" className="mt-8 w-full" onClick={cadastrar}>{enviando ? 'Cadastrando…' : 'Cadastrar colaborador'}</Button>
    </div>);
}

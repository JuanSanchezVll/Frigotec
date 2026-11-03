import { ArrowLeft, ArrowRight, Building2, Factory, FileText, KeyRound, Lock, Mail, MapPin, Phone, User, UserRound } from 'lucide-react';
import { Link } from 'react-router-dom';
import { Alert, Button, FloatField, SegmentedCards, Steps } from '../../../shared/ui';
import { useCadastro } from '../model/useCadastro';

/** Segmento ui da feature cadastro (3 passos). Estado, validação e envio ficam em ../model/useCadastro. */
export function CadastroForm() {
    const { passo, setPasso, tipo, setTipo, pessoaJuridica: pj, campo, erro, enviando, cadastrar } = useCadastro();
    return (<div className="w-full max-w-[520px] rounded-xl border border-borda bg-fundo p-6 shadow-2 md:p-9">
      <Steps etapas={['Tipo de pessoa', 'Seus dados', 'Acesso']} atual={passo}/>
      <div key={passo} className="anim-slide mt-10">
        {passo === 0 && (<>
            <h2 className="texto-h1">Pessoa física ou jurídica?</h2>
            <p className="mt-2 text-texto-secundario">Isso define os dados que vamos pedir no próximo passo.</p>
            <div className="mt-8">
              <SegmentedCards label="Tipo de pessoa" value={tipo} onChange={setTipo} opcoes={[
                { value: 'fisica', label: 'Física', icon: User, desc: 'Cozinheiro autônomo ou pessoa física' },
                { value: 'juridica', label: 'Jurídica', icon: Building2, desc: 'Restaurante, hotel ou empresa' },
            ]}/>
            </div>
          </>)}
        {passo === 1 && (<>
            <h2 className="texto-h1">{pj ? 'Dados da empresa' : 'Dados pessoais'}</h2>
            <div className="mt-8 space-y-4">
              {pj ? (<>
                <FloatField icon={Building2} label="Razão social" {...campo('razaoSocial')}/>
                <FloatField icon={FileText} label="Inscrição estadual" {...campo('inscricaoEstadual')}/>
                <FloatField icon={Factory} label="CNPJ" {...campo('cnpj')}/>
              </>) : (<FloatField icon={FileText} label="CPF" {...campo('cpf')}/>)}
              <FloatField icon={Phone} label="Telefone fixo ou comercial (opcional)" {...campo('telefone')}/>
              <FloatField icon={Phone} label="Telefone / celular" {...campo('celular')}/>
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
            </div>
          </>)}
        {passo === 2 && (<>
            <h2 className="texto-h1">Dados de acesso</h2>
            <div className="mt-8 space-y-4">
              {erro && <Alert tipo="alerta">{erro}</Alert>}
              <FloatField icon={Mail} label="E-mail" type="email" {...campo('email')}/>
              <FloatField icon={UserRound} label={pj ? 'Nome completo do responsável' : 'Nome completo'} {...campo('nome')}/>
              <FloatField icon={Lock} label="Senha (mínimo 6 caracteres)" type="password" {...campo('senha')}/>
              <FloatField icon={KeyRound} label="Confirme sua senha" type="password" {...campo('confirma')}/>
            </div>
          </>)}
      </div>
      <div className="mt-10 flex items-center justify-between gap-3">
        {passo > 0 ? <Button variante="texto" onClick={() => setPasso(passo - 1)}><ArrowLeft size={18}/>Voltar</Button> : <span />}
        {passo < 2
            ? <Button onClick={() => setPasso(passo + 1)}>Próximo <ArrowRight size={18}/></Button>
            : <Button onClick={cadastrar}>{enviando ? 'Cadastrando…' : 'Cadastrar'}</Button>}
      </div>
      <p className="mt-8 texto-pequeno text-texto-secundario">Já tem conta? <Link to="/login" className="font-semibold text-primaria hover:underline">Já tenho conta</Link></p>
    </div>);
}

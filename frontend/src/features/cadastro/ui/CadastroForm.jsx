import { ArrowLeft, ArrowRight, Building2, Factory, FileText, KeyRound, Lock, Mail, Phone, User, UserRound } from 'lucide-react';
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useApp } from '../../../entities/estado';
import { Button, FloatField, SegmentedCards, Steps } from '../../../shared/ui';
/** Cadastro em 3 passos. Sem validação: todos os botões avançam. */
export function CadastroForm() {
    const [passo, setPasso] = useState(0);
    const [tipo, setTipo] = useState('juridica');
    const nav = useNavigate();
    const { setCadastroConcluido } = useApp();
    const pj = tipo === 'juridica';
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
              {/* VALIDACAO: razão social, inscrição estadual e CNPJ obrigatórios; CNPJ com máscara e dígitos */}
              {pj ? (<>
                <FloatField icon={Building2} label="Razão social" defaultValue="Restaurante Sabor & Brasa Ltda."/>
                <FloatField icon={FileText} label="Inscrição estadual" defaultValue="110042490114"/>
                <FloatField icon={Factory} label="CNPJ" defaultValue="12.345.678/0001-90"/>
              </>) : (<>
                {/* VALIDACAO: CPF obrigatório com máscara; celular obrigatório; telefone fixo opcional */}
                <FloatField icon={FileText} label="CPF" defaultValue="123.456.789-09"/>
                <FloatField icon={Phone} label="Telefone fixo ou comercial (opcional)" defaultValue="(11) 4002-8922"/>
                <FloatField icon={Phone} label="Telefone / celular" defaultValue="(11) 98765-4321"/>
              </>)}
            </div>
          </>)}
        {passo === 2 && (<>
            <h2 className="texto-h1">Dados de acesso</h2>
            <div className="mt-8 space-y-4">
              {/* VALIDACAO: e-mail válido e único; senha com no mínimo 6 caracteres; confirmação igual à senha */}
              <FloatField icon={Mail} label="E-mail" type="email" defaultValue="gustavo@saborebrasa.com.br"/>
              <FloatField icon={UserRound} label={pj ? 'Nome completo do responsável' : 'Nome completo'} defaultValue="Gustavo Mendes"/>
              <FloatField icon={Lock} label="Senha" type="password" defaultValue="senha123"/>
              <FloatField icon={KeyRound} label="Confirme sua senha" type="password" defaultValue="senha123"/>
            </div>
          </>)}
      </div>
      <div className="mt-10 flex items-center justify-between gap-3">
        {passo > 0 ? <Button variante="texto" onClick={() => setPasso(passo - 1)}><ArrowLeft size={18}/>Voltar</Button> : <span />}
        {passo < 2
            ? <Button onClick={() => setPasso(passo + 1)}>Próximo <ArrowRight size={18}/></Button>
            : <Button onClick={() => { setCadastroConcluido(true); nav('/login'); }}>Cadastrar</Button>}
      </div>
      <p className="mt-8 texto-pequeno text-texto-secundario">Já tem conta? <Link to="/login" className="font-semibold text-primaria hover:underline">Já tenho conta</Link></p>
    </div>);
}

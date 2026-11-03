import { Lock, Mail, ShieldCheck } from 'lucide-react';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useApp } from '../../../entities/estado';
import { Alert, Button, FloatField } from '../../../shared/ui';
import { ADMIN_DEMO, useLogin } from '../model/useLogin';

/** Segmento ui da feature login: só desenha a tela. Estado e regras ficam em ../model/useLogin. */
export function LoginForm() {
    const { email, setEmail, senha, setSenha, erro, carregando, entrar, entrarComo } = useLogin();
    const { cadastroConcluido } = useApp();
    const [esqueci, setEsqueci] = useState(false);
    return (<form className="w-full max-w-[440px]" onSubmit={(e) => { e.preventDefault(); entrar(); }}>
      <h2 className="texto-h1">Acesse sua conta</h2>
      <p className="mt-2 text-texto-secundario">Entre para abrir chamados e acompanhar seus equipamentos.</p>
      <div className="mt-6 space-y-3">
        {cadastroConcluido && <Alert tipo="sucesso">Cadastro concluído. Entre para continuar.</Alert>}
        {erro && <Alert tipo="alerta">{erro}</Alert>}
        {esqueci && <Alert>Enviamos um link para redefinir sua senha no e-mail informado.</Alert>}
      </div>
      <div className="mt-6 space-y-4">
        <FloatField icon={Mail} label="E-mail" type="email" value={email} onChange={(e) => setEmail(e.target.value)}/>
        <FloatField icon={Lock} label="Senha" type="password" value={senha} onChange={(e) => setSenha(e.target.value)}/>
      </div>
      <div className="mt-3 flex items-center justify-between">
        <button type="button" onClick={() => setEsqueci(true)} className="min-h-11 texto-pequeno font-semibold text-primaria hover:underline">Esqueci a senha</button>
        <Link to="/cadastro" className="inline-flex min-h-11 items-center texto-pequeno font-semibold text-primaria hover:underline">Cadastrar-se</Link>
      </div>
      <Button type="submit" tamanho="lg" className="mt-4 w-full">{carregando ? <><span className="h-5 w-5 animate-spin rounded-full border-2 border-fundo/40 border-t-fundo" aria-hidden/>Entrando…</> : 'Acessar'}</Button>
      <Button variante="contorno" tamanho="md" className="mt-3 w-full" onClick={() => entrarComo(ADMIN_DEMO)}><ShieldCheck size={18}/>Administrador</Button>
      <p className="mt-4 text-center texto-pequeno text-texto-secundario">Técnicos e administradores recebem o acesso de um administrador da Frigotec.</p>
    </form>);
}

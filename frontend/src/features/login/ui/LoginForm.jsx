import { Lock, Mail, User, Wrench } from 'lucide-react';
import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useApp } from '../../../entities/estado';
import { Alert, Button, FloatField } from '../../../shared/ui';
const CLIENTE = 'gustavo@saborebrasa.com.br';
const TECNICO = 'davi@frigotec.com';
/** Login. Sem validação: e-mail @frigotec.com abre o dashboard do técnico; qualquer outro abre a Home. */
export function LoginForm() {
    const [email, setEmail] = useState(CLIENTE);
    const [senha, setSenha] = useState('senha123');
    const [esqueci, setEsqueci] = useState(false);
    const [carregando, setCarregando] = useState(false);
    const nav = useNavigate();
    const { cadastroConcluido } = useApp();
    const entrar = (mail) => {
        setCarregando(true);
        setTimeout(() => nav(mail.toLowerCase().includes('@frigotec.com') ? '/tecnico' : '/home'), 600);
    };
    const atalho = (mail) => { setEmail(mail); setSenha('senha123'); entrar(mail); };
    return (<form className="w-full max-w-[440px]" onSubmit={(e) => { e.preventDefault(); entrar(email); }}>
      <h2 className="texto-h1">Acesse sua conta</h2>
      <p className="mt-2 text-texto-secundario">Entre para abrir chamados e acompanhar seus equipamentos.</p>
      <div className="mt-6 space-y-3">
        {cadastroConcluido && <Alert tipo="sucesso">Cadastro concluído. Entre para continuar.</Alert>}
        {esqueci && <Alert>Enviamos um link para redefinir sua senha no e-mail informado.</Alert>}
      </div>
      <div className="mt-6 space-y-4">
        {/* VALIDACAO: e-mail obrigatório e em formato válido; senha obrigatória; credenciais incorretas */}
        <FloatField icon={Mail} label="E-mail" type="email" value={email} onChange={(e) => setEmail(e.target.value)}/>
        <FloatField icon={Lock} label="Senha" type="password" value={senha} onChange={(e) => setSenha(e.target.value)}/>
      </div>
      <div className="mt-3 flex items-center justify-between">
        <button type="button" onClick={() => setEsqueci(true)} className="min-h-11 texto-pequeno font-semibold text-primaria hover:underline">Esqueci a senha</button>
        <Link to="/cadastro" className="inline-flex min-h-11 items-center texto-pequeno font-semibold text-primaria hover:underline">Cadastrar-se</Link>
      </div>
      <Button type="submit" tamanho="lg" className="mt-4 w-full">{carregando ? <><span className="h-5 w-5 animate-spin rounded-full border-2 border-fundo/40 border-t-fundo" aria-hidden/>Entrando…</> : 'Acessar'}</Button>
      <div className="my-6 flex items-center gap-3 texto-legenda font-semibold uppercase tracking-widest text-texto-secundario"><span className="h-px flex-1 bg-borda"/>demonstração<span className="h-px flex-1 bg-borda"/></div>
      <div className="grid gap-3 sm:grid-cols-2">
        <Button variante="contorno" tamanho="sm" onClick={() => atalho(CLIENTE)}><User size={16}/>Entrar como cliente</Button>
        <Button variante="contorno" tamanho="sm" onClick={() => atalho(TECNICO)}><Wrench size={16}/>Entrar como técnico Frigotec</Button>
      </div>
      <p className="mt-4 text-center texto-pequeno text-texto-secundario">Para ver o painel do técnico, entre com um e-mail @frigotec.com.</p>
    </form>);
}

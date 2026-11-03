import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useApp } from '../../../entities/estado';
import { login, rotaInicial } from '../../../entities/usuario';
import { mensagemDeErro } from '../../../shared/api';

export const ADMIN_DEMO = 'admin@frigotec.com';
export const SENHA_DEMO = 'senha123';

/**
 * Segmento model (FSD) da feature login: estado do formulário e regra de entrar.
 * O ui (LoginForm) só desenha; quem fala com o backend é entities/usuario/api.
 */
export function useLogin() {
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState('');
    const [carregando, setCarregando] = useState(false);
    const navigate = useNavigate();
    const { entrar: iniciarSessao } = useApp();

    // POST /usuarios/login -> guarda a sessão e abre a home do perfil devolvido pelo backend.
    const entrar = (emailInformado = email, senhaInformada = senha) => {
        setCarregando(true);
        setErro('');
        login(emailInformado.trim(), senhaInformada)
            .then((resposta) => {
                // resposta = { token, tipo, expiraEm (segundos), perfil, usuario }
                iniciarSessao({ ...resposta.usuario, token: resposta.token, expiraEmMs: Date.now() + resposta.expiraEm * 1000 });
                navigate(rotaInicial(resposta.perfil));
            })
            .catch((e) => setErro(mensagemDeErro(e, 'Não foi possível entrar.')))
            .finally(() => setCarregando(false));
    };

    // Atalho do botão Administrador: entra com o administrador que já vem no banco.
    const entrarComo = (emailDemo) => {
        setEmail(emailDemo);
        setSenha(SENHA_DEMO);
        entrar(emailDemo, SENHA_DEMO);
    };

    return { email, setEmail, senha, setSenha, erro, carregando, entrar, entrarComo };
}

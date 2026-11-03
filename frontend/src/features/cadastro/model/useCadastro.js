import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useApp } from '../../../entities/estado';
import { cadastrarUsuario, montarCadastroCliente, validarSenha } from '../../../entities/usuario';
import { mensagemDeErro } from '../../../shared/api';

export const FORMULARIO_VAZIO = { razaoSocial: '', inscricaoEstadual: '', cnpj: '', cpf: '', telefone: '', celular: '', cep: '', estado: '', cidade: '', rua: '', numero: '', email: '', nome: '', senha: '', confirma: '' };

/** Segmento model da feature cadastro: passos, campos, validação e envio do cliente. */
export function useCadastro() {
    const [passo, setPasso] = useState(0);
    const [tipo, setTipo] = useState('juridica');
    const [valores, setValores] = useState(FORMULARIO_VAZIO);
    const [erro, setErro] = useState('');
    const [enviando, setEnviando] = useState(false);
    const navigate = useNavigate();
    const { setCadastroConcluido } = useApp();
    const pessoaJuridica = tipo === 'juridica';

    // Props prontas para um campo controlado: { value, onChange }
    const campo = (nome) => ({ value: valores[nome], onChange: (e) => setValores((atual) => ({ ...atual, [nome]: e.target.value })) });

    // POST /usuarios -> cria o CLIENTE e leva ao login.
    const cadastrar = () => {
        const problema = validarSenha(valores.senha, valores.confirma);
        if (problema) { setErro(problema); return; }
        setErro('');
        setEnviando(true);
        cadastrarUsuario(montarCadastroCliente(valores, pessoaJuridica))
            .then(() => { setCadastroConcluido(true); navigate('/login'); })
            .catch((e) => setErro(mensagemDeErro(e, 'Não foi possível concluir o cadastro.')))
            .finally(() => setEnviando(false));
    };

    return { passo, setPasso, tipo, setTipo, pessoaJuridica, campo, erro, enviando, cadastrar };
}

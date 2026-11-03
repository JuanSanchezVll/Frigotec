import { useState } from 'react';
import { PERFIL, cadastrarColaborador, montarCadastroColaborador, validarSenha } from '../../../entities/usuario';
import { mensagemDeErro } from '../../../shared/api';

const VAZIO = { nome: '', email: '', cpf: '', telefone: '', celular: '', cep: '', estado: '', cidade: '', rua: '', numero: '', senha: '', confirma: '' };

/**
 * Segmento model da feature cadastro-colaborador: o ADMIN cria um técnico ou outro administrador
 * (SDD RN-USR-03). O token do admin logado vai no cabeçalho; o backend só aceita ROLE_ADMIN.
 */
export function useCadastroColaborador({ onCadastrado } = {}) {
    const [valores, setValores] = useState(VAZIO);
    const [perfil, setPerfil] = useState(PERFIL.TECNICO);
    const [erro, setErro] = useState('');
    const [sucesso, setSucesso] = useState('');
    const [enviando, setEnviando] = useState(false);

    const campo = (nome) => ({ value: valores[nome], onChange: (e) => setValores((atual) => ({ ...atual, [nome]: e.target.value })) });

    // POST /usuarios/colaboradores -> cria o colaborador, limpa o formulário e avisa a página para recarregar a lista.
    const cadastrar = () => {
        const problema = validarSenha(valores.senha, valores.confirma);
        if (problema) { setErro(problema); setSucesso(''); return; }
        setErro('');
        setSucesso('');
        setEnviando(true);
        cadastrarColaborador(montarCadastroColaborador(valores, perfil))
            .then((novo) => {
                setValores(VAZIO);
                setSucesso(`${novo.nomeCompleto} foi cadastrado como ${perfil === PERFIL.ADMIN ? 'administrador' : 'técnico'}.`);
                onCadastrado?.(novo);
            })
            .catch((e) => setErro(mensagemDeErro(e, 'Não foi possível cadastrar o colaborador.')))
            .finally(() => setEnviando(false));
    };

    return { campo, perfil, setPerfil, erro, sucesso, enviando, cadastrar };
}

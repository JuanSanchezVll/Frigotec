import { useEffect, useState } from 'react';
import { mensagemDeErro } from '../../../shared/api';
import { listarColaboradores } from '../api/usuarioApi';

/**
 * Segmento model da entidade usuario: lista de técnicos e administradores (só o ADMIN consegue).
 * O efeito busca a lista quando a tela abre; recarregar() busca de novo (ex.: depois de cadastrar alguém).
 */
export function useColaboradores() {
    const [lista, setLista] = useState([]);
    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState('');
    const [versao, setVersao] = useState(0);
    useEffect(() => {
        let ativo = true; // evita atualizar o estado se a tela for fechada antes da resposta chegar
        listarColaboradores()
            .then((dados) => { if (ativo) { setLista(dados); setErro(''); } })
            .catch((e) => { if (ativo) setErro(mensagemDeErro(e, 'Não foi possível carregar os colaboradores.')); })
            .finally(() => { if (ativo) setCarregando(false); });
        return () => { ativo = false; };
    }, [versao]);
    return { lista, carregando, erro, recarregar: () => setVersao((v) => v + 1) };
}

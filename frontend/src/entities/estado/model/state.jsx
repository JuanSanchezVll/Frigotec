import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { abrirChamado, atribuirChamado, agendarVisita, chamadoDaApi, encerrarChamado, listarChamadosDoCliente, listarTodosChamados, mudarStatus, ORDEM_STATUS, statusDe } from '../../chamado';
import { cadastrarEquipamento, equipamentoDaApi, listarEquipamentosDoUsuario, validarGarantia } from '../../equipamento';
import { threadsIniciais } from '../../mensagem';
import { ehAgendamento } from '../../visita';
import { gravarSessao, lerSessao, limparSessao, PERFIL } from '../../usuario';
import { definirAoNaoAutenticado, definirToken, mensagemDeErro } from '../../../shared/api';

const C = createContext(null);
export const useApp = () => useContext(C);

const agora = () => new Date().toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' }).replace(' ', ', ');

/**
 * Estado global das telas.
 * - Sessão, chamados e equipamentos vêm do backend (axios) e são recarregados depois de cada ação.
 * - Conversa (threads), avaliação e cancelamento continuam simulados: o SDD não tem endpoint para eles.
 */
export function AppProvider({ children }) {
    const [chamadosApi, setChamadosApi] = useState([]);
    const [equipamentos, setEquipamentos] = useState([]);
    const [threads, setThreads] = useState(threadsIniciais);
    const [cancelados, setCancelados] = useState([]);
    const [avaliacoes, setAvaliacoes] = useState({});
    const [erro, setErro] = useState('');
    const [cadastroConcluido, setCadastroConcluido] = useState(false);
    // Sessão: usuário devolvido pelo login (inclui o id do backend e o token). Guardado no navegador para sobreviver ao F5.
    const [usuario, setUsuario] = useState(() => { const u = lerSessao(); definirToken(u?.token ?? null); return u; });
    const entrar = useCallback((u) => { gravarSessao(u); definirToken(u.token); setUsuario(u); }, []);
    const sair = useCallback(() => { limparSessao(); definirToken(null); setUsuario(null); setChamadosApi([]); setEquipamentos([]); }, []);
    // 401 do backend (token vencido ou inválido): encerra a sessão; a RotaProtegida leva ao login.
    useEffect(() => { definirAoNaoAutenticado(sair); }, [sair]);

    // Busca no backend o que o perfil logado pode ver: cliente = os próprios; técnico/admin = todos os chamados.
    const recarregar = useCallback(async () => {
        if (!usuario) return;
        try {
            if (usuario.perfil === PERFIL.CLIENTE) {
                const [ch, eq] = await Promise.all([listarChamadosDoCliente(usuario.id), listarEquipamentosDoUsuario(usuario.id)]);
                setChamadosApi(ch.map(chamadoDaApi));
                setEquipamentos(eq.map(equipamentoDaApi));
            } else {
                setChamadosApi((await listarTodosChamados()).map(chamadoDaApi));
            }
        } catch (e) {
            setErro(mensagemDeErro(e, 'Não foi possível carregar os dados.'));
        }
    }, [usuario]);
    useEffect(() => { recarregar(); }, [recarregar]);

    // Executa uma chamada à API; se o backend recusar (regra de negócio), mostra a mensagem dele e devolve null.
    const tentar = useCallback(async (acao) => {
        setErro('');
        try {
            const resultado = await acao();
            await recarregar();
            return resultado ?? true;
        } catch (e) {
            setErro(mensagemDeErro(e, 'Não foi possível concluir a ação.'));
            return null;
        }
    }, [recarregar]);

    const chamados = useMemo(() => chamadosApi.map((c) => ({ ...c, cancelado: cancelados.includes(c.id), avaliacao: avaliacoes[c.id] })), [chamadosApi, cancelados, avaliacoes]);
    const idDe = (protocolo) => chamadosApi.find((c) => c.id === protocolo)?.chamadoId;

    const api = {
        addEquipamento: async (dados) => { const r = await tentar(() => cadastrarEquipamento(usuario.id, dados)); return r && r !== true ? equipamentoDaApi(r) : null; },
        enviarChamado: async (d) => { const r = await tentar(() => abrirChamado({ equipamentoUsuarioId: d.equipamentoUsuarioId, problema: d.categoria, descricao: d.descricao })); return r && r !== true ? chamadoDaApi(r) : null; },
        atribuir: (id) => tentar(() => atribuirChamado(idDe(id))),
        // Próximo status da sequência; o backend só aceita para quem a regra permite (técnico responsável, cliente dono...).
        avancar: (id) => tentar(() => mudarStatus(idDe(id), ORDEM_STATUS[chamadosApi.find((c) => c.id === id).etapa])),
        aprovarServico: (id) => tentar(() => mudarStatus(idDe(id), 'APROVACAO_DO_SERVICO')),
        agendarVisita: (id, quando) => tentar(() => agendarVisita(idDe(id), quando)),
        finalizar: (id, laudo) => tentar(() => encerrarChamado(idDe(id), {
            motivo: 'Atendimento concluído',
            descricao: [laudo.resumo, laudo.pecas && `Peças trocadas: ${laudo.pecas}`, laudo.obs && laudo.obs !== '—' && `Observações: ${laudo.obs}`].filter(Boolean).join('\n'),
        })),
        // Aprovar exige a data de fim da garantia (SDD RN-GAR-03).
        validarGarantia: (id, dados) => {
            const garantiaId = chamadosApi.find((c) => c.id === id)?.garantiaId;
            return garantiaId ? tentar(() => validarGarantia(garantiaId, dados)) : Promise.resolve(null);
        },
        cancelar: (id) => setCancelados((l) => [...l, id]),
        avaliar: (id, nota, comentario) => setAvaliacoes((a) => ({ ...a, [id]: { nota, comentario } })),
        limparErro: () => setErro(''),
        enviar: (chamadoId, m) => setThreads((t) => ({ ...t, [chamadoId]: [...(t[chamadoId] ?? []), { ...m, id: `m${Date.now()}${Math.random()}`, hora: agora() }] })),
        atualizarStatus: (chamadoId, msgId, status) => setThreads((t) => ({ ...t, [chamadoId]: (t[chamadoId] ?? []).map((m) => (m.id === msgId ? { ...m, status } : m)) })),
    };

    const valor = useMemo(() => {
        const meusChamados = chamados;
        const ativos = chamados.filter((c) => !c.cancelado);
        const novos = ativos.filter((c) => !c.tecnico && !c.finalizado);
        const meus = ativos.filter((c) => c.tecnicoId === usuario?.id && !c.finalizado);
        const visitas = [];
        const orcamentos = [];
        for (const c of chamados) {
            for (const m of threads[c.id] ?? []) {
                if (ehAgendamento(m))
                    visitas.push({ id: m.id, chamadoId: c.id, cliente: c.solicitante, equipamento: c.equipamento, endereco: m.endereco ?? '', data: m.data, periodo: m.periodo ?? 'Manhã', status: m.status ?? 'Pendente' });
                if (m.tipo === 'proposta')
                    orcamentos.push({ id: m.id, chamadoId: c.id, cliente: c.solicitante, valor: m.valor ?? '', validade: m.validade ?? '7 dias', status: m.status ?? 'Pendente' });
            }
        }
        return { chamados, meusChamados, novos, meus, equipamentos, threads, visitas, orcamentos, erro, cadastroConcluido, setCadastroConcluido, usuario, entrar, sair, ...api };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [chamados, equipamentos, threads, erro, cadastroConcluido, usuario, chamadosApi]);

    return (<C.Provider value={valor}>
      {children}
      {erro && (<div role="alert" className="fixed left-1/2 top-4 z-[70] flex w-[min(92vw,560px)] -translate-x-1/2 items-start gap-3 rounded-md border border-brasa/50 bg-fundo-brasa px-4 py-3 text-texto-brasa shadow-3">
        <p className="flex-1 font-medium">{erro}</p>
        <button type="button" aria-label="Fechar aviso" onClick={() => setErro('')} className="font-bold">×</button>
      </div>)}
    </C.Provider>);
}
export { statusDe };

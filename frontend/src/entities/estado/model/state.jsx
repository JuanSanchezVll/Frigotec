import { createContext, useCallback, useContext, useMemo, useState } from 'react';
import { chamadosIniciais, statusDe, TECNICO } from '../../chamado';
import { equipamentosIniciais, rotuloEquip } from '../../equipamento';
import { threadsIniciais } from '../../mensagem';
import { ehAgendamento } from '../../visita';
import { cliente } from '../../usuario';
const C = createContext(null);
export const useApp = () => useContext(C);
const agora = () => new Date().toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' }).replace(' ', ', ');
const hoje = () => new Date().toLocaleDateString('pt-BR');
/** Estado simulado em memória (nada persiste ao recarregar). */
export function AppProvider({ children }) {
    const [chamados, setChamados] = useState(chamadosIniciais);
    const [equipamentos, setEquipamentos] = useState(equipamentosIniciais);
    const [threads, setThreads] = useState(threadsIniciais);
    const [seq, setSeq] = useState(0);
    const [cadastroConcluido, setCadastroConcluido] = useState(false);
    const alt = useCallback((id, f) => setChamados((l) => l.map((c) => (c.id === id ? f({ ...c, atualizacao: hoje() }) : c))), []);
    const api = {
        addEquipamento: (e) => { const n = { ...e, id: `e${Date.now()}` }; setEquipamentos((l) => [...l, n]); return n; },
        enviarChamado: (d) => {
            const n = { id: `PR${19292000 + seq}`, equipamento: d.equipamento, solicitante: cliente.nome, data: hoje(), abertura: `${hoje()} às ${new Date().toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' })}`, atualizacao: hoje(), categoria: d.categoria, descricao: d.descricao, etapa: 1, datas: [hoje()], garantia: d.garantia };
            setSeq((s) => s + 1);
            setChamados((l) => [n, ...l]);
            return n;
        },
        atribuir: (id) => alt(id, (c) => ({ ...c, tecnico: TECNICO, etapa: Math.max(c.etapa, 2), datas: [...c.datas, hoje()].slice(0, 6) })),
        avancar: (id) => alt(id, (c) => (c.etapa >= 5 ? c : { ...c, etapa: c.etapa + 1, datas: [...c.datas, hoje()] })),
        finalizar: (id, laudo) => alt(id, (c) => ({ ...c, etapa: 6, finalizado: true, laudo, datas: [...c.datas, hoje()] })),
        cancelar: (id) => alt(id, (c) => ({ ...c, cancelado: true })),
        avaliar: (id, nota, comentario) => alt(id, (c) => ({ ...c, avaliacao: { nota, comentario } })),
        setGarantia: (id, g) => {
            const alvo = chamados.find((c) => c.id === id)?.equipamento;
            if (!alvo)
                return;
            const validade = g === 'Garantia ativa' ? '25/08/2027' : undefined;
            setChamados((l) => l.map((c) => (c.equipamento === alvo ? { ...c, garantia: g, validade } : c)));
            setEquipamentos((l) => l.map((e) => (rotuloEquip(e) === alvo ? { ...e, garantia: g, validade } : e)));
        },
        enviar: (chamadoId, m) => setThreads((t) => ({ ...t, [chamadoId]: [...(t[chamadoId] ?? []), { ...m, id: `m${Date.now()}${Math.random()}`, hora: agora() }] })),
        atualizarStatus: (chamadoId, msgId, status) => setThreads((t) => ({ ...t, [chamadoId]: (t[chamadoId] ?? []).map((m) => (m.id === msgId ? { ...m, status } : m)) })),
    };
    const valor = useMemo(() => {
        const meusChamados = chamados.filter((c) => c.solicitante === cliente.nome);
        const ativos = chamados.filter((c) => !c.cancelado);
        const novos = ativos.filter((c) => !c.tecnico && !c.finalizado);
        const meus = ativos.filter((c) => c.tecnico === TECNICO && !c.finalizado);
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
        return { chamados, meusChamados, novos, meus, equipamentos, threads, visitas, orcamentos, cadastroConcluido, setCadastroConcluido, ...api };
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [chamados, equipamentos, threads, cadastroConcluido, seq]);
    return <C.Provider value={valor}>{children}</C.Provider>;
}
export { statusDe };

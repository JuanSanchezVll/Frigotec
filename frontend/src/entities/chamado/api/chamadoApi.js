import { http } from "../../../shared/api"

// Segmento api (FSD) da entidade chamado: só fala com o backend (axios). Controller: ChamadoController.

// POST /chamados  -> só o CLIENTE; o dono vem do token. Corpo: { equipamentoUsuarioId, problema (categoria), descricao }
export function abrirChamado(dados) {
    return http.post("/chamados", dados).then((r) => r.data)
}

// GET /chamados/usuarios/{id}  -> chamados do cliente (o próprio)
export function listarChamadosDoCliente(usuarioId) {
    return http.get(`/chamados/usuarios/${usuarioId}`).then((r) => r.data)
}

// GET /chamados  -> TECNICO/ADMIN: todos (a fila é o que não tem técnico)
export function listarTodosChamados() {
    return http.get("/chamados").then((r) => r.data)
}

// POST /chamados/{id}/atribuicao  -> "atribuir a mim" (avança para APROVADO_PELO_TECNICO)
export function atribuirChamado(id) {
    return http.post(`/chamados/${id}/atribuicao`, {}).then((r) => r.data)
}

// PATCH /chamados/{id}/status?novoStatus=...  -> só o PRÓXIMO status da sequência, por quem a regra permite
export function mudarStatus(id, novoStatus) {
    return http.patch(`/chamados/${id}/status`, null, { params: { novoStatus } }).then((r) => r.data)
}

// PATCH /chamados/{id}/visita  -> técnico responsável; data e hora futuras (ISO local: 2026-12-01T14:00:00)
export function agendarVisita(id, visitaAgendadaEm) {
    return http.patch(`/chamados/${id}/visita`, { visitaAgendadaEm }).then((r) => r.data)
}

// POST /chamados/{id}/encerramento  -> { motivo, descricao }; só com a visita agendada e sem orçamento pendente
export function encerrarChamado(id, dados) {
    return http.post(`/chamados/${id}/encerramento`, dados).then((r) => r.data)
}

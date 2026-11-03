import { http } from "../../../shared/api"

// Segmento api (FSD) da entidade equipamento: só fala com o backend (axios).
// O token JWT é anexado pelo axios (shared/api/http.js); quem pode chamar cada rota é decidido no SegurancaConfig.

// GET /fabricantes (público)  -> marcas; "propria: true" é a FrigoPro (única com garantia)
export function listarFabricantes() {
    return http.get("/fabricantes").then((r) => r.data)
}

// GET /fabricantes/{id}/equipamentos  -> modelos da marca escolhida (EquipamentoDto)
export function listarModelos(fabricanteId) {
    return http.get(`/fabricantes/${fabricanteId}/equipamentos`).then((r) => r.data)
}

// GET /usuarios/{id}/equipamentos  -> equipamentos do cliente (EquipamentoUsuarioDto com a garantia calculada)
export function listarEquipamentosDoUsuario(usuarioId) {
    return http.get(`/usuarios/${usuarioId}/equipamentos`).then((r) => r.data)
}

// POST /equipamentos/{usuarioId}  -> o cliente cadastra a própria unidade (o id precisa ser o do token)
export function cadastrarEquipamento(usuarioId, dados) {
    return http.post(`/equipamentos/${usuarioId}`, dados).then((r) => r.data)
}

// PATCH /equipamentos/{id}  -> o cliente corrige a série e/ou a nota fiscal da própria unidade
export function atualizarEquipamento(id, dados) {
    return http.patch(`/equipamentos/${id}`, dados).then((r) => r.data)
}

// DELETE /equipamentos/{id}  -> o cliente exclui a própria unidade (409 se ela já teve chamado: o histórico é preservado)
export function excluirEquipamento(id) {
    return http.delete(`/equipamentos/${id}`)
}

// PATCH /garantias/{id}/validacao  -> TECNICO/ADMIN validam ou recusam a garantia
export function validarGarantia(garantiaId, dados) {
    return http.patch(`/garantias/${garantiaId}/validacao`, dados).then((r) => r.data)
}

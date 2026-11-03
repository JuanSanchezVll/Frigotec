import { http } from "../../../shared/api"

// Segmento api (FSD) da entidade anexo: arquivos enviados pelo cliente. Controller: AnexoController.
// O envio é multipart (campo "arquivos"); o conteúdo é baixado com o token JWT, por isso vira um blob.

const formulario = (arquivos) => {
    const f = new FormData()
    arquivos.forEach((a) => f.append("arquivos", a))
    return f
}

// POST /chamados/{id}/anexos  -> fotos (JPG/PNG) e vídeo (MP4) do defeito; só o cliente dono do chamado
export function enviarAnexosDoChamado(chamadoId, arquivos) {
    return http.post(`/chamados/${chamadoId}/anexos`, formulario(arquivos)).then((r) => r.data)
}

// GET /chamados/{id}/anexos  -> dono, técnico ou admin
export function listarAnexosDoChamado(chamadoId) {
    return http.get(`/chamados/${chamadoId}/anexos`).then((r) => r.data)
}

// POST /equipamentos/{id}/documentos  -> nota fiscal e comprovantes (PDF/JPG/PNG), só marca própria
export function enviarDocumentosDoEquipamento(equipamentoUsuarioId, arquivos) {
    return http.post(`/equipamentos/${equipamentoUsuarioId}/documentos`, formulario(arquivos)).then((r) => r.data)
}

// GET /equipamentos/{id}/documentos
export function listarDocumentosDoEquipamento(equipamentoUsuarioId) {
    return http.get(`/equipamentos/${equipamentoUsuarioId}/documentos`).then((r) => r.data)
}

// GET /anexos/{id}/arquivo  -> devolve um endereço temporário (blob:) para mostrar a imagem ou abrir o arquivo
export function urlDoArquivo(anexo) {
    return http.get(anexo.url, { responseType: "blob" }).then((r) => URL.createObjectURL(r.data))
}

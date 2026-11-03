// Converte o que o backend devolve no formato que as telas já usam.

/** "2027-01-10" ou "2027-01-10T09:00:00" -> "10/01/2027" (sem passar por Date, para não deslocar o dia por fuso). */
export const dataBr = (iso) => {
    if (!iso) return ""
    const [a, m, d] = iso.slice(0, 10).split("-")
    return `${d}/${m}/${a}`
}

export const horaBr = (iso) => (iso ? iso.slice(11, 16) : "")

// situacao calculada pelo backend (GarantiaMapper) -> selo da tela
const ROTULO_GARANTIA = {
    ATIVA: "Garantia ativa",
    EM_ANALISE: "Garantia em análise",
    VENCIDA: "Garantia vencida",
    RECUSADA: "Garantia recusada",
    NAO_SE_APLICA: "Sem garantia",
}

export const rotuloGarantia = (garantia) => ROTULO_GARANTIA[garantia?.situacao] ?? "Sem garantia"

/** EquipamentoUsuarioDto -> { id, tipo (modelo), marca, serie, garantia, validade, garantiaId } */
export function equipamentoDaApi(dto) {
    return {
        id: dto.equipamentoUsuarioId,
        tipo: dto.modelo,
        marca: dto.marca,
        serie: dto.numeroSerie ?? "S/N",
        garantia: rotuloGarantia(dto.garantia),
        validade: dataBr(dto.garantia?.dataFimGarantia),
        garantiaId: dto.garantia?.id,
    }
}

/** "25/08/2025" (DateInput) -> "2025-08-25" (o que o backend espera). */
export const dataIso = (br) => {
    const [d, m, a] = (br ?? "").split("/")
    return d && m && a ? `${a}-${m.padStart(2, "0")}-${d.padStart(2, "0")}` : null
}

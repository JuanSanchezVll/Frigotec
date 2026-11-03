import { dataBr, horaBr, rotuloGarantia } from "../../equipamento"

// Ordem dos status no backend (StatusChamadoEnum). A posição + 1 é a "etapa" que a linha do tempo desenha.
export const ORDEM_STATUS = ["ENVIADO", "APROVADO_PELO_TECNICO", "CONTATO_DO_TECNICO", "APROVACAO_DO_SERVICO", "VISITA_AGENDADA", "FINALIZADO"]

/** ChamadoDto -> formato das telas. "id" é o protocolo (PR00001001); "chamadoId" é o id numérico usado nas chamadas à API. */
export function chamadoDaApi(dto) {
    const historico = dto.historico ?? []
    const primeiro = historico[0]
    const ultimo = historico[historico.length - 1]
    const eq = dto.equipamentoUsuario
    return {
        id: dto.protocolo,
        chamadoId: dto.id,
        equipamento: eq ? `${eq.modelo} · ${eq.marca}` : "Equipamento",
        solicitante: dto.usuario?.nomeCompleto ?? "",
        cliente: dto.usuario,
        data: dataBr(dto.dataPedido),
        abertura: primeiro ? `${dataBr(primeiro.dataAlteracao)} às ${horaBr(primeiro.dataAlteracao)}` : dataBr(dto.dataPedido),
        atualizacao: ultimo ? dataBr(ultimo.dataAlteracao) : dataBr(dto.dataPedido),
        categoria: dto.problema,
        descricao: dto.descricao,
        statusAtual: dto.statusAtual,
        etapa: ORDEM_STATUS.indexOf(dto.statusAtual) + 1,
        datas: historico.map((h) => dataBr(h.dataAlteracao)),
        garantia: rotuloGarantia(eq?.garantia),
        validade: dataBr(eq?.garantia?.dataFimGarantia),
        garantiaId: eq?.garantia?.id,
        dataCompra: dataBr(eq?.garantia?.dataCompra),
        tecnico: dto.tecnico?.nomeCompleto,
        tecnicoId: dto.tecnico?.id,
        visitaAgendadaEm: dto.visitaAgendadaEm,
        finalizado: dto.statusAtual === "FINALIZADO",
        laudo: dto.encerramento ? { resumo: dto.encerramento.descricao, obs: dto.encerramento.motivo } : undefined,
    }
}

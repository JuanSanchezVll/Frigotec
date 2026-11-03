import { http } from "../../../shared/api"

// Segmento api (FSD) da entidade dashboard: só fala com o backend (axios).

// GET /dashboard/tecnico  -> KPIs do painel do técnico calculados no banco (TECNICO/ADMIN)
// { chamadosNovos, meusChamados, visitasDestaSemana, orcamentosAguardandoResposta, proximasVisitas: [{ protocolo, cliente, equipamento, quando }] }
export function buscarDashboardTecnico() {
    return http.get("/dashboard/tecnico").then((r) => r.data)
}

package com.frigo.frigotec.dto;

import java.time.LocalDateTime;
import java.util.List;

/** Números do painel do técnico, calculados no banco a cada consulta (nada simulado). */
public record DashboardTecnicoDto(
        long chamadosNovos,
        long meusChamados,
        long visitasDestaSemana,
        long orcamentosAguardandoResposta,
        List<VisitaResumo> proximasVisitas) {

    public record VisitaResumo(String protocolo, String cliente, String equipamento, LocalDateTime quando) {
    }
}

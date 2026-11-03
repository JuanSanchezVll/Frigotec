package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class VisitaDto {

    @NotNull
    private LocalDateTime visitaAgendadaEm;

    public LocalDateTime getVisitaAgendadaEm() { return visitaAgendadaEm; }
    public void setVisitaAgendadaEm(LocalDateTime visitaAgendadaEm) { this.visitaAgendadaEm = visitaAgendadaEm; }
}

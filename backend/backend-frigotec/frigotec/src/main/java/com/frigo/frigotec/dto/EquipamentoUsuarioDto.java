package com.frigo.frigotec.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class EquipamentoUsuarioDto {


    @NotNull
    private Long equipamentoId;

    @NotNull
    private UUID usuarioId;
    @NotBlank
    @NotNull
    private String numeroSerie;


    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Long getEquipamentoId() {
        return equipamentoId;
    }

    public void setEquipamentoId(Long equipamentoId) {
        this.equipamentoId = equipamentoId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }
}

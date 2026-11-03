package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotNull;

public class RespostaOrcamentoDto {

    @NotNull
    private Boolean aceito;

    public Boolean getAceito() { return aceito; }
    public void setAceito(Boolean aceito) { this.aceito = aceito; }
}

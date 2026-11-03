package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotBlank;

public class EncerramentoDto {

    @NotBlank
    private String motivo;

    @NotBlank
    private String descricao;

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}

package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ChamadoCadastroDto {

    @NotBlank
    private String descricao;

    @NotBlank
    private String problema;

    @NotNull
    private UUID usuarioId;

    @NotNull
    private Long equipamentoUsuarioId;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getEquipamentoUsuarioId() {
        return equipamentoUsuarioId;
    }

    public void setEquipamentoUsuarioId(Long equipamentoUsuarioId) {
        this.equipamentoUsuarioId = equipamentoUsuarioId;
    }
}
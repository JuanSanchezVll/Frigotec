package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** O dono do chamado vem do token (SDD RN-USR-06), não do corpo da requisição. */
public class ChamadoCadastroDto {

    @NotBlank
    private String descricao;

    @NotBlank
    private String problema;

    @NotNull
    private Long equipamentoUsuarioId;

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getProblema() { return problema; }
    public void setProblema(String problema) { this.problema = problema; }
    public Long getEquipamentoUsuarioId() { return equipamentoUsuarioId; }
    public void setEquipamentoUsuarioId(Long equipamentoUsuarioId) { this.equipamentoUsuarioId = equipamentoUsuarioId; }
}

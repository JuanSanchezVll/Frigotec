package com.frigo.frigotec.dto;

import com.frigo.frigotec.util.StatusChamadoEnum;

import java.time.LocalDateTime;

public class HistoricoDto {

    private StatusChamadoEnum status;
    private LocalDateTime dataAlteracao;
    private String responsavel;

    public StatusChamadoEnum getStatus() { return status; }
    public void setStatus(StatusChamadoEnum status) { this.status = status; }
    public LocalDateTime getDataAlteracao() { return dataAlteracao; }
    public void setDataAlteracao(LocalDateTime dataAlteracao) { this.dataAlteracao = dataAlteracao; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
}

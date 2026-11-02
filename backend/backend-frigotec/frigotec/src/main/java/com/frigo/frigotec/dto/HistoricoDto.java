package com.frigo.frigotec.dto;

import com.frigo.frigotec.util.StatusChamadoEnum;

import java.time.LocalDate;

public class HistoricoDto {

    private StatusChamadoEnum status;
    private LocalDate dataAlteracao;

    public StatusChamadoEnum getStatus() {
        return status;
    }

    public void setStatus(StatusChamadoEnum status) {
        this.status = status;
    }

    public LocalDate getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDate dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}

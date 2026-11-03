package com.frigo.frigotec.dto;

import com.frigo.frigotec.entity.ValidacaoGarantia;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** Corpo de PATCH /garantias/{id}/validacao. Para VALIDADA a data de fim da garantia é obrigatória. */
public class ValidacaoGarantiaDto {

    @NotNull(message = "Informe VALIDADA ou RECUSADA")
    private ValidacaoGarantia validacao;

    private LocalDate dataFimGarantia;

    public ValidacaoGarantia getValidacao() { return validacao; }
    public void setValidacao(ValidacaoGarantia validacao) { this.validacao = validacao; }
    public LocalDate getDataFimGarantia() { return dataFimGarantia; }
    public void setDataFimGarantia(LocalDate dataFimGarantia) { this.dataFimGarantia = dataFimGarantia; }
}

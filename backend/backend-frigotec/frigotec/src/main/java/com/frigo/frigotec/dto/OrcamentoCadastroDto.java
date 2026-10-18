package com.frigo.frigotec.dto;

import com.frigo.frigotec.entity.Chamado;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class OrcamentoCadastroDto {


    @NotNull
    @Positive
    private BigDecimal valor;

    @NotNull
    private Chamado chamado;

    @NotNull
    private Boolean isAceito;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Chamado getChamado() {
        return chamado;
    }

    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }

    public Boolean getAceito() {
        return isAceito;
    }

    public void setAceito(Boolean aceito) {
        isAceito = aceito;
    }
}

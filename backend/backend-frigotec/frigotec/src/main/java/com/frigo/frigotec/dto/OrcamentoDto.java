package com.frigo.frigotec.dto;

import com.frigo.frigotec.entity.Chamado;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

public class OrcamentoDto {


    private BigDecimal valor;
    private ChamadoDto chamado;
    private Boolean isAceito;


    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public ChamadoDto getChamado() {
        return chamado;
    }

    public void setChamado(ChamadoDto chamado) {
        this.chamado = chamado;
    }

    public Boolean getAceito() {
        return isAceito;
    }

    public void setAceito(Boolean aceito) {
        isAceito = aceito;
    }
}

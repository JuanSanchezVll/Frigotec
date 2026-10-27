package com.frigo.frigotec.dto;

import com.frigo.frigotec.entity.Chamado;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class OrcamentoDto {

    private Long id;

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotNull
    private Long chamadoId;

    private ChamadoDto chamado;

    private Boolean isAceito;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Long getChamadoId() {
        return chamadoId;
    }

    public void setChamadoId(Long chamadoId) {
        this.chamadoId = chamadoId;
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

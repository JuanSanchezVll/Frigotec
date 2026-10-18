package com.frigo.frigotec.dto;



import com.frigo.frigotec.entity.Equipamento;

import java.time.LocalDate;

public class GarantiaDto {


    private LocalDate dataCompra;

    private Integer garantiaAtiva;

    private String notaFiscal;

    private EquipamentoDto equipamentoDto;


    public Integer getGarantiaAtiva() {
        return garantiaAtiva;
    }

    public void setGarantiaAtiva(Integer garantiaAtiva) {
        this.garantiaAtiva = garantiaAtiva;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public EquipamentoDto getEquipamentoDto() {
        return equipamentoDto;
    }

    public void setEquipamentoDto(EquipamentoDto equipamentoDto) {
        this.equipamentoDto = equipamentoDto;
    }
}

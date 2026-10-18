package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EquipamentoDto {

    @NotNull
    private FabricanteEquipamentoDto fabricante;
    @NotBlank
    @NotNull
    private String modelo;


    public FabricanteEquipamentoDto getFabricante() {
        return fabricante;
    }

    public void setFabricante(FabricanteEquipamentoDto fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }



}

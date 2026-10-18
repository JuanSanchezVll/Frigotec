package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EquipamentoDto {

    @NotNull
    private FabricanteEquipamentoDto marca;
    @NotBlank
    @NotNull
    private String modelo;





    public FabricanteEquipamentoDto getMarca() {
        return marca;
    }

    public void setMarca(FabricanteEquipamentoDto marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }



}

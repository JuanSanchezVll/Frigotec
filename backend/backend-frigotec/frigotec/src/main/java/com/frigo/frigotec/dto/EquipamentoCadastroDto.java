package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EquipamentoCadastroDto {

    @NotBlank
    @NotNull
    private FabricanteEquipamentoDto marca;
    @NotBlank
    @NotNull
    private ModeloEquipamentoDto modelo;

    @NotNull
    private UsuarioCadastroDto usuario;


    public FabricanteEquipamentoDto getMarca() {
        return marca;
    }

    public void setMarca(FabricanteEquipamentoDto marca) {
        this.marca = marca;
    }

    public ModeloEquipamentoDto getModelo() {
        return modelo;
    }

    public void setModelo(ModeloEquipamentoDto modelo) {
        this.modelo = modelo;
    }

    public UsuarioCadastroDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioCadastroDto usuario) {
        this.usuario = usuario;
    }
}

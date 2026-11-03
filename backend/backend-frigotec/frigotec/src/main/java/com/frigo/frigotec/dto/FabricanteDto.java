package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FabricanteDto {

    @NotNull
    private Long id;
    @NotBlank
    @NotNull
    private String nome;

    private boolean propria;

    public boolean isPropria() {
        return propria;
    }

    public void setPropria(boolean propria) {
        this.propria = propria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

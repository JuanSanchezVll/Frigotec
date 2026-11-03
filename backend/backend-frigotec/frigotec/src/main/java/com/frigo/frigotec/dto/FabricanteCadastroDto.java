package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FabricanteCadastroDto {



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
   // TODO criar um campo para armazenar imgens das empresas dos equipamentos



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}



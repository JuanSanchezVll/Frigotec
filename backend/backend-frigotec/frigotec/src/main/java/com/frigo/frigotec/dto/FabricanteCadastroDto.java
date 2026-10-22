package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FabricanteCadastroDto {



    @NotBlank
    @NotNull
    private String nome;
   // TODO criar um campo para armazenar imgens das empresas dos equipamentos



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}



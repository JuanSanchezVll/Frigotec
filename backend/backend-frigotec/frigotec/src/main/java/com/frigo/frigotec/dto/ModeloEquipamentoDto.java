package com.frigo.frigotec.dto;

public class ModeloEquipamentoDto {

    private Long id;

    private String nome;

    private FabricanteEquipamentoDto fabricante;

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

    public FabricanteEquipamentoDto getFabricante() {
        return fabricante;
    }

    public void setFabricante(FabricanteEquipamentoDto fabricante) {
        this.fabricante = fabricante;
    }
}

package com.frigo.frigotec.dto;


public class EquipamentoUsuarioDto {


    private EquipamentoDto equipamentoDto;

    private UsuarioDto usuarioDto;

    private String numeroSerie;


    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }


    public EquipamentoDto getEquipamentoDto() {
        return equipamentoDto;
    }

    public void setEquipamentoDto(EquipamentoDto equipamentoCadastroDto) {
        this.equipamentoDto = equipamentoCadastroDto;
    }

    public UsuarioDto getUsuarioDto() {
        return usuarioDto;
    }

    public void setUsuarioDto(UsuarioDto usuarioDto) {
        this.usuarioDto = usuarioDto;
    }
}

package com.frigo.frigotec.dto;


public class EquipamentoUsuarioDto {


    private EquipamentoDto equipamentoDto;

    private UsuarioDto usuarioCadastroDto;

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

    public UsuarioDto getUsuarioCadastroDto() {
        return usuarioCadastroDto;
    }

    public void setUsuarioCadastroDto(UsuarioDto usuarioCadastroDto) {
        this.usuarioCadastroDto = usuarioCadastroDto;
    }
}

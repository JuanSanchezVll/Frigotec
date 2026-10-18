package com.frigo.frigotec.dto;


public class EquipamentoUsuarioDto {


    private EquipamentoCadastroDto equipamentoCadastroDto;

    private UsuarioCadastroDto usuarioCadastroDto;

    private String numeroSerie;


    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }


    public EquipamentoCadastroDto getEquipamentoCadastroDto() {
        return equipamentoCadastroDto;
    }

    public void setEquipamentoCadastroDto(EquipamentoCadastroDto equipamentoCadastroDto) {
        this.equipamentoCadastroDto = equipamentoCadastroDto;
    }

    public UsuarioCadastroDto getUsuarioCadastroDto() {
        return usuarioCadastroDto;
    }

    public void setUsuarioCadastroDto(UsuarioCadastroDto usuarioCadastroDto) {
        this.usuarioCadastroDto = usuarioCadastroDto;
    }
}

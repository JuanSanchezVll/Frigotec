package com.frigo.frigotec.dto;

import java.time.LocalDate;

public class ChamadoDto {

    private LocalDate dataPedido;
    private String descricao;
    private String problema;
    private UsuarioDto usuario;
    private EquipamentoUsuarioDto equipamentoUsuario;


    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getProblema() {
        return problema;
    }

    public void setProblema(String problema) {
        this.problema = problema;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDto usuario) {
        this.usuario = usuario;
    }

    public EquipamentoUsuarioDto getEquipamentoUsuario() {
        return equipamentoUsuario;
    }

    public void setEquipamentoUsuario(EquipamentoUsuarioDto equipamentoUsuario) {
        this.equipamentoUsuario = equipamentoUsuario;
    }
}

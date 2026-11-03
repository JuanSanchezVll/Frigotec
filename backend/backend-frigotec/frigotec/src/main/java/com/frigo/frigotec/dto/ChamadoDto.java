package com.frigo.frigotec.dto;

import com.frigo.frigotec.util.StatusChamadoEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ChamadoDto {

    private Long id;
    private String protocolo;
    private LocalDate dataPedido;
    private String descricao;
    private String problema;
    private StatusChamadoEnum statusAtual;
    private UsuarioDto usuario;
    private UsuarioDto tecnico;
    private EquipamentoUsuarioDto equipamentoUsuario;
    private LocalDateTime visitaAgendadaEm;
    private List<HistoricoDto> historico;
    private EncerramentoDto encerramento;

    public EncerramentoDto getEncerramento() { return encerramento; }
    public void setEncerramento(EncerramentoDto encerramento) { this.encerramento = encerramento; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProtocolo() { return protocolo; }
    public void setProtocolo(String protocolo) { this.protocolo = protocolo; }
    public LocalDate getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDate dataPedido) { this.dataPedido = dataPedido; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getProblema() { return problema; }
    public void setProblema(String problema) { this.problema = problema; }
    public StatusChamadoEnum getStatusAtual() { return statusAtual; }
    public void setStatusAtual(StatusChamadoEnum statusAtual) { this.statusAtual = statusAtual; }
    public UsuarioDto getUsuario() { return usuario; }
    public void setUsuario(UsuarioDto usuario) { this.usuario = usuario; }
    public UsuarioDto getTecnico() { return tecnico; }
    public void setTecnico(UsuarioDto tecnico) { this.tecnico = tecnico; }
    public EquipamentoUsuarioDto getEquipamentoUsuario() { return equipamentoUsuario; }
    public void setEquipamentoUsuario(EquipamentoUsuarioDto equipamentoUsuario) { this.equipamentoUsuario = equipamentoUsuario; }
    public LocalDateTime getVisitaAgendadaEm() { return visitaAgendadaEm; }
    public void setVisitaAgendadaEm(LocalDateTime visitaAgendadaEm) { this.visitaAgendadaEm = visitaAgendadaEm; }
    public List<HistoricoDto> getHistorico() { return historico; }
    public void setHistorico(List<HistoricoDto> historico) { this.historico = historico; }
}

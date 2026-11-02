package com.frigo.frigotec.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="dataPedido")
    private LocalDate dataPedido;

    @Column(name = "descricao")
    private String descricao;
    @Column(name = "problema")
    private String problema;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "equipamento_usuario_id")
    private EquipamentoUsuario equipamentoUsuario;

    @OneToMany(mappedBy = "chamado", cascade = CascadeType.ALL)
    private List<Historico> historicos = new ArrayList<>();

    public List<Historico> getHistoricos() { return historicos; }

    public void adicionarHistorico(Historico historico) {
        historico.setChamado(this);
        this.historicos.add(historico);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public EquipamentoUsuario getEquipamentoUsuario() {
        return equipamentoUsuario;
    }

    public void setEquipamentoUsuario(EquipamentoUsuario equipamentoUsuario) {
        this.equipamentoUsuario = equipamentoUsuario;
    }
}

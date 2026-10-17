package com.frigo.frigotec.entity;

import jakarta.persistence.*;

@Entity
public class mensagemEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "chamado_id")
    private Chamado chamado;

    @ManyToOne
    @JoinColumn(name= "usuario_id")
    private Usuario usuario;

    private String descricao;

    private String assunto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chamado getChamado() {
        return chamado;
    }

    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
}

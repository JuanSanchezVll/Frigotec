package com.frigo.frigotec.entity;

import jakarta.persistence.*;

@Entity
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "descricao")
    private String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }
}

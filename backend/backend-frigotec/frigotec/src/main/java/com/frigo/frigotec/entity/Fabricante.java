package com.frigo.frigotec.entity;

import jakarta.persistence.*;

@Entity
public class Fabricante {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String nome;

    /** Marca da própria Frigotec (FrigoPro): só ela tem garantia cadastrável (SDD RN-EQP-01/RN-EUS-03). */
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean propria;

    public boolean isPropria() {
        return propria;
    }

    public void setPropria(boolean propria) {
        this.propria = propria;
    }

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
}

package com.frigo.frigotec.entity;

import jakarta.persistence.*;

@Entity
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "modelo")
    private String modelo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Fabricante fabricante;

    public Long getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fabricante getFabricante() {
        return fabricante;
    }

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }
}

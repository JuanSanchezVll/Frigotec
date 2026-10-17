package com.frigo.frigotec.entity;


import jakarta.persistence.*;

@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private  Long id;

    @Column(name = "razaoSocial")
    private String razaoSocial;

    @Column(name= "inscricaoEstadual")
    private String inscricaoEstadual;

    @Column(name = "cnpj")
    private String cnpj;



    public Long getId() {
        return id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}

package com.frigo.frigotec.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Garantia {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataOrigem")
    private LocalDate dataOrigem;

    @Column(name = "dataFim")
    private LocalDate dataDestino;

    @Column(name = "garantiaAtiva")
    private Integer garantiaAtiva;

    @Column(name = "notaFiscal")
    private String notaFiscal;


    @OneToOne
    @JoinColumn(name = "id_equipamento")
    private Equipamento equipamento;

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataOrigem() {
        return dataOrigem;
    }

    public void setDataOrigem(LocalDate dataOrigem) {
        this.dataOrigem = dataOrigem;
    }

    public LocalDate getDataDestino() {
        return dataDestino;
    }

    public void setDataDestino(LocalDate dataDestino) {
        this.dataDestino = dataDestino;
    }

    public Integer getGarantiaAtiva() {
        return garantiaAtiva;
    }

    public void setGarantiaAtiva(Integer garantiaAtiva) {
        this.garantiaAtiva = garantiaAtiva;
    }

    public String getNotaFiscal() {
        return notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
}

package com.frigo.frigotec.entity;

import com.frigo.frigotec.dto.EquipamentoCadastroDto;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Garantia {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataCompra")
    private LocalDate dataCompra;

    @Column(name = "garantiaAtiva")
    private Integer garantiaAtiva;

    @Column(name = "notaFiscal")
    private String notaFiscal;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id")
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


    public Integer getGarantiaAtiva() {
        return garantiaAtiva;
    }

    public void setGarantiaAtiva(Integer garantiaAtiva) {
        this.garantiaAtiva = garantiaAtiva;
    }

    public String getNotaFiscal(String notaFiscal) {
        return this.notaFiscal;
    }

    public void setNotaFiscal(String notaFiscal) {
        this.notaFiscal = notaFiscal;
    }
}

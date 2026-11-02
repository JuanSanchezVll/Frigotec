package com.frigo.frigotec.entity;


import com.frigo.frigotec.util.StatusChamadoEnum;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Historico {



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Enumerated(EnumType.STRING)
        private StatusChamadoEnum status;

        private LocalDate dataAlteracao;

        @ManyToOne
        @JoinColumn(name = "chamado_id")
        private Chamado chamado;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusChamadoEnum getStatus() {
        return status;
    }

    public void setStatus(StatusChamadoEnum status) {
        this.status = status;
    }

    public LocalDate getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDate dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Chamado getChamado() {
        return chamado;
    }

    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }
}




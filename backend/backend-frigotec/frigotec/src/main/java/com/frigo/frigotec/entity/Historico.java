package com.frigo.frigotec.entity;

import com.frigo.frigotec.util.StatusChamadoEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Uma transição de status: nunca é alterada nem apagada (SDD RN-CHM-16). Guarda quando e quem executou. */
@Entity
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StatusChamadoEnum status;

    private LocalDateTime dataAlteracao;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    @ManyToOne
    @JoinColumn(name = "chamado_id")
    private Chamado chamado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public StatusChamadoEnum getStatus() { return status; }
    public void setStatus(StatusChamadoEnum status) { this.status = status; }
    public LocalDateTime getDataAlteracao() { return dataAlteracao; }
    public void setDataAlteracao(LocalDateTime dataAlteracao) { this.dataAlteracao = dataAlteracao; }
    public Usuario getResponsavel() { return responsavel; }
    public void setResponsavel(Usuario responsavel) { this.responsavel = responsavel; }
    public Chamado getChamado() { return chamado; }
    public void setChamado(Chamado chamado) { this.chamado = chamado; }
}

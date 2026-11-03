package com.frigo.frigotec.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Auditoria da troca de técnico de um chamado; não altera o status (SDD RN-CHM-12). */
@Entity
public class Redesignacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "chamado_id")
    private Chamado chamado;

    @ManyToOne
    @JoinColumn(name = "tecnico_anterior_id")
    private Usuario tecnicoAnterior;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tecnico_novo_id")
    private Usuario tecnicoNovo;

    @Column(nullable = false)
    private String motivo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "feito_por_id")
    private Usuario feitoPor;

    @Column(name = "feito_em", nullable = false)
    private LocalDateTime feitoEm;

    public Long getId() { return id; }
    public Chamado getChamado() { return chamado; }
    public void setChamado(Chamado chamado) { this.chamado = chamado; }
    public Usuario getTecnicoAnterior() { return tecnicoAnterior; }
    public void setTecnicoAnterior(Usuario tecnicoAnterior) { this.tecnicoAnterior = tecnicoAnterior; }
    public Usuario getTecnicoNovo() { return tecnicoNovo; }
    public void setTecnicoNovo(Usuario tecnicoNovo) { this.tecnicoNovo = tecnicoNovo; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Usuario getFeitoPor() { return feitoPor; }
    public void setFeitoPor(Usuario feitoPor) { this.feitoPor = feitoPor; }
    public LocalDateTime getFeitoEm() { return feitoEm; }
    public void setFeitoEm(LocalDateTime feitoEm) { this.feitoEm = feitoEm; }
}

package com.frigo.frigotec.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Garantia de UMA unidade do cliente (SDD RN-GAR-01): liga a EquipamentoUsuario, não ao modelo do catálogo.
 * A situação (ativa, vencida...) não é gravada: é calculada em GarantiaMapper (RN-GAR-02).
 */
@Entity
public class Garantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_usuario_id", nullable = false, unique = true)
    private EquipamentoUsuario equipamentoUsuario;

    /** Tipo de garantia informado pelo cliente (a lista de tipos ainda é uma pergunta em aberto do SDD, seção 10). */
    @Column(name = "tipo")
    private String tipo;

    @Column(name = "data_compra")
    private LocalDate dataCompra;

    @Column(name = "nota_fiscal")
    private String notaFiscal;

    @Enumerated(EnumType.STRING)
    @Column(name = "validacao", nullable = false)
    private ValidacaoGarantia validacao = ValidacaoGarantia.EM_ANALISE;

    @Column(name = "data_fim_garantia")
    private LocalDate dataFimGarantia;

    public Long getId() { return id; }
    public EquipamentoUsuario getEquipamentoUsuario() { return equipamentoUsuario; }
    public void setEquipamentoUsuario(EquipamentoUsuario equipamentoUsuario) { this.equipamentoUsuario = equipamentoUsuario; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDate getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDate dataCompra) { this.dataCompra = dataCompra; }
    public String getNotaFiscal() { return notaFiscal; }
    public void setNotaFiscal(String notaFiscal) { this.notaFiscal = notaFiscal; }
    public ValidacaoGarantia getValidacao() { return validacao; }
    public void setValidacao(ValidacaoGarantia validacao) { this.validacao = validacao; }
    public LocalDate getDataFimGarantia() { return dataFimGarantia; }
    public void setDataFimGarantia(LocalDate dataFimGarantia) { this.dataFimGarantia = dataFimGarantia; }
}

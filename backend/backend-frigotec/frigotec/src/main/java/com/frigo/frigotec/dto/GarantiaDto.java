package com.frigo.frigotec.dto;

import java.time.LocalDate;

/**
 * Garantia como a API devolve. "situacao" é calculada: ATIVA, VENCIDA, EM_ANALISE, RECUSADA ou NAO_SE_APLICA
 * (marcas que não são próprias). Na entrada (cadastro do equipamento) usa-se tipo, dataCompra e notaFiscal.
 */
public class GarantiaDto {

    private Long id;
    private String situacao;
    private String tipo;
    private LocalDate dataCompra;
    private LocalDate dataFimGarantia;
    private String notaFiscal;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDate getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDate dataCompra) { this.dataCompra = dataCompra; }
    public LocalDate getDataFimGarantia() { return dataFimGarantia; }
    public void setDataFimGarantia(LocalDate dataFimGarantia) { this.dataFimGarantia = dataFimGarantia; }
    public String getNotaFiscal() { return notaFiscal; }
    public void setNotaFiscal(String notaFiscal) { this.notaFiscal = notaFiscal; }
}

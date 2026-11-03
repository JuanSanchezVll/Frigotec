package com.frigo.frigotec.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** situacao: PENDENTE, ACEITO ou RECUSADO (SDD RN-ORC-04). */
public class OrcamentoDto {

    private Long id;
    private Long chamadoId;
    private BigDecimal valor;
    private String descricao;
    private String situacao;
    private LocalDateTime criadoEm;
    private LocalDateTime respondidoEm;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getChamadoId() { return chamadoId; }
    public void setChamadoId(Long chamadoId) { this.chamadoId = chamadoId; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
    public LocalDateTime getRespondidoEm() { return respondidoEm; }
    public void setRespondidoEm(LocalDateTime respondidoEm) { this.respondidoEm = respondidoEm; }
}

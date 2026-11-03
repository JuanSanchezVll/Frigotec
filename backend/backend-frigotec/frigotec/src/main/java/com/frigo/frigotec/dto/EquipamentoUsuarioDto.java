package com.frigo.frigotec.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Equipamento de um cliente (a unidade física). Entrada do POST /equipamentos/{idUsuario}:
 * equipamentoId (item do catálogo), numeroSerie (opcional, único por modelo) e, só para marca própria (FrigoPro),
 * tipoGarantia, dataCompra e notaFiscal. Saída: equipamentoUsuarioId, modelo, marca, numeroSerie e garantia.
 */
public class EquipamentoUsuarioDto {

    // ---- saída
    private Long equipamentoUsuarioId;
    private String modelo;
    private String marca;
    private GarantiaDto garantia;

    // ---- entrada (e saída)
    @NotNull(message = "O equipamento do catálogo é obrigatório")
    private Long equipamentoId;
    private UUID usuarioId; // o dono vem do token; este campo é ignorado na entrada
    private String numeroSerie;

    // ---- entrada: só para marca própria
    private String tipoGarantia;
    private LocalDate dataCompra;
    private String notaFiscal;

    public Long getEquipamentoUsuarioId() { return equipamentoUsuarioId; }
    public void setEquipamentoUsuarioId(Long equipamentoUsuarioId) { this.equipamentoUsuarioId = equipamentoUsuarioId; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public GarantiaDto getGarantia() { return garantia; }
    public void setGarantia(GarantiaDto garantia) { this.garantia = garantia; }
    public Long getEquipamentoId() { return equipamentoId; }
    public void setEquipamentoId(Long equipamentoId) { this.equipamentoId = equipamentoId; }
    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
    public String getTipoGarantia() { return tipoGarantia; }
    public void setTipoGarantia(String tipoGarantia) { this.tipoGarantia = tipoGarantia; }
    public LocalDate getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDate dataCompra) { this.dataCompra = dataCompra; }
    public String getNotaFiscal() { return notaFiscal; }
    public void setNotaFiscal(String notaFiscal) { this.notaFiscal = notaFiscal; }
}

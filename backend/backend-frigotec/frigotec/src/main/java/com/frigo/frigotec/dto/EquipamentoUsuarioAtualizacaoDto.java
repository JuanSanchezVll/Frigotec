package com.frigo.frigotec.dto;

/**
 * Entrada do PATCH /equipamentos/{id}: o cliente corrige o número de série e/ou a nota fiscal da própria unidade.
 * Só os campos enviados são alterados (campo ausente = não muda; texto em branco = apaga). Nota fiscal só existe
 * para marca própria (FrigoPro).
 */
public class EquipamentoUsuarioAtualizacaoDto {

    private String numeroSerie;
    private String notaFiscal;

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
    public String getNotaFiscal() { return notaFiscal; }
    public void setNotaFiscal(String notaFiscal) { this.notaFiscal = notaFiscal; }
}

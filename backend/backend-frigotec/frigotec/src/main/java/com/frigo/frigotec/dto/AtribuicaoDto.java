package com.frigo.frigotec.dto;

import java.util.UUID;

/**
 * "Atribuir a mim": corpo vazio (ou o próprio id). ADMIN informa o técnico.
 * Redesignação de chamado já atribuído: tecnicoId e motivo obrigatórios.
 */
public class AtribuicaoDto {

    private UUID tecnicoId;
    private String motivo;

    public UUID getTecnicoId() { return tecnicoId; }
    public void setTecnicoId(UUID tecnicoId) { this.tecnicoId = tecnicoId; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}

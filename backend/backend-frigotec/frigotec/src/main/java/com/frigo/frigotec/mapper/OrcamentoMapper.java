package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.OrcamentoDto;
import com.frigo.frigotec.entity.Orcamento;
import org.springframework.stereotype.Component;

@Component
public class OrcamentoMapper {

    public OrcamentoDto toResponse(Orcamento orcamento) {
        OrcamentoDto dto = new OrcamentoDto();
        dto.setId(orcamento.getId());
        dto.setChamadoId(orcamento.getChamado() != null ? orcamento.getChamado().getId() : null);
        dto.setValor(orcamento.getValor());
        dto.setDescricao(orcamento.getDescricao());
        dto.setSituacao(orcamento.getAceito() == null ? "PENDENTE" : orcamento.getAceito() ? "ACEITO" : "RECUSADO");
        dto.setCriadoEm(orcamento.getCriadoEm());
        dto.setRespondidoEm(orcamento.getRespondidoEm());
        return dto;
    }
}

package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.HistoricoDto;
import com.frigo.frigotec.entity.Historico;
import org.springframework.stereotype.Component;

@Component
public class HistoricoMapper {

    public HistoricoDto toHistoricoDto(Historico historico) {
        HistoricoDto dto = new HistoricoDto();
        dto.setStatus(historico.getStatus());
        dto.setDataAlteracao(historico.getDataAlteracao());
        dto.setResponsavel(historico.getResponsavel() != null ? historico.getResponsavel().getNomeCompleto() : null);
        return dto;
    }
}

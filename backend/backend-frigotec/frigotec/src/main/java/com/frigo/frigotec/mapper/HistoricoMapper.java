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
        return dto;
    }

    public Historico toHistoricoEntity(HistoricoDto dto) {
        Historico entity = new Historico();
        entity.setStatus(dto.getStatus());
        entity.setDataAlteracao(dto.getDataAlteracao());
        return entity;
    }
}

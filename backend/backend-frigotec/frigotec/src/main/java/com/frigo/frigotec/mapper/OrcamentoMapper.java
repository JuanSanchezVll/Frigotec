package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.*;
import com.frigo.frigotec.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrcamentoMapper {

    private final ChamadoMapper chamadoMapper;

    @Autowired
    public OrcamentoMapper(ChamadoMapper chamadoMapper) {
        this.chamadoMapper = chamadoMapper;
    }

    public  OrcamentoDto toResponse(Orcamento orcamento) {
        if (orcamento == null) {
            return null;
        }

        OrcamentoDto dto = new OrcamentoDto();

        dto.setValor(orcamento.getValor());
        dto.setChamado(chamadoMapper.toResponse(orcamento.getChamado()));
        dto.setAceito(orcamento.getAceito());

        return dto;
    }

    public  Orcamento toEntity(OrcamentoDto dto) {
        if (dto == null) {
            return null;
        }

        Orcamento entity = new Orcamento();

        entity.setValor(dto.getValor());
        entity.setChamado(chamadoMapper.toEntity(dto.getChamado()));
        entity.setAceito(dto.getAceito());

        return entity;

    }
}

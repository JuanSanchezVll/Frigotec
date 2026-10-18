package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.FabricanteEquipamentoDto;
import com.frigo.frigotec.entity.Fabricante;
import org.springframework.stereotype.Component;

@Component
public class FabricanteMapper {

    public Fabricante toEntity(FabricanteEquipamentoDto dto) {
        if (dto == null) {
            return null;
        }

        Fabricante entity = new Fabricante();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());

        return entity;
    }

    public FabricanteEquipamentoDto toDto(Fabricante entity) {
        if (entity == null) {
            return null;
        }

        FabricanteEquipamentoDto dto = new FabricanteEquipamentoDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());

        return dto;
    }
}
package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.EquipamentoDto;
import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.dto.FabricanteEquipamentoDto;
import com.frigo.frigotec.dto.UsuarioDto;
import com.frigo.frigotec.entity.Equipamento;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoMapper {
   private final FabricanteMapper fabricanteMapper;

    public EquipamentoMapper(FabricanteMapper fabricanteMapper) {
        this.fabricanteMapper = fabricanteMapper;
    }

    public  EquipamentoDto toResponse(Equipamento equipamento){
        EquipamentoDto dto = new EquipamentoDto();

        dto.setFabricante(fabricanteMapper.toDto(equipamento.getFabricante()));
        dto.setModelo(equipamento.getModelo());

    return dto;
    }

    public Equipamento toEntity (EquipamentoDto equipamentoDto){


        Equipamento entity = new Equipamento();

        entity.setFabricante(fabricanteMapper.toEntity(equipamentoDto.getFabricante()));
        entity.setModelo(equipamentoDto.getModelo());

        return entity;
    }
}

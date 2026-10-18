package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoUsuarioMapper {

    private final UsuarioMapper usuarioMapper;

    private final EquipamentoMapper equipamentoMapper;

    public EquipamentoUsuarioMapper(UsuarioMapper usuarioMapper, EquipamentoMapper equipamentoMapper) {
        this.usuarioMapper = usuarioMapper;
        this.equipamentoMapper = equipamentoMapper;
    }



    public EquipamentoUsuarioDto toResponse(EquipamentoUsuario equipamento){

        EquipamentoUsuarioDto dto = new EquipamentoUsuarioDto();

        dto.setEquipamentoDto(equipamentoMapper.toResponse(equipamento.getEquipamento()));
        dto.setUsuarioDto(usuarioMapper.toDto(equipamento.getUsuario()));
        dto.setNumeroSerie(equipamento.getNumeroSerie());

        return dto;
    }

    public EquipamentoUsuario toEntity (EquipamentoUsuarioDto equipamentoDto){
        EquipamentoUsuario entity = new EquipamentoUsuario();

        entity.setUsuario(usuarioMapper.toEntity(equipamentoDto.getUsuarioDto()));
        entity.setNumeroSerie(equipamentoDto.getNumeroSerie());
        entity.setEquipamento(equipamentoMapper.toEntity(equipamentoDto.getEquipamentoDto()));

        return entity;
    }
}

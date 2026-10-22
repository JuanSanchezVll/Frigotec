package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.entity.Equipamento;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.entity.Usuario;
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

        dto.setEquipamentoId(equipamento.getId());
        dto.setUsuarioId(equipamento.getUsuario().getId());
        dto.setNumeroSerie(equipamento.getNumeroSerie());

        return dto;
    }

    public EquipamentoUsuario toEntity (EquipamentoUsuarioDto equipamentoDto){
        EquipamentoUsuario entity = new EquipamentoUsuario();

        Usuario entityUsuario = new Usuario();
        entityUsuario.setId(equipamentoDto.getUsuarioId());

        Equipamento entityEquipamento = new Equipamento();
        entityEquipamento.setId(equipamentoDto.getEquipamentoId());

        entity.setUsuario(entityUsuario);
        entity.setNumeroSerie(equipamentoDto.getNumeroSerie());
        entity.setEquipamento(entityEquipamento);

        return entity;
    }
}

package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.entity.Equipamento;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoUsuarioMapper {

    private final GarantiaMapper garantiaMapper;

    public EquipamentoUsuarioMapper(GarantiaMapper garantiaMapper) {
        this.garantiaMapper = garantiaMapper;
    }

    public EquipamentoUsuarioDto toResponse(EquipamentoUsuario equipamento) {
        EquipamentoUsuarioDto dto = new EquipamentoUsuarioDto();
        dto.setEquipamentoUsuarioId(equipamento.getId());
        dto.setEquipamentoId(equipamento.getEquipamento().getId());
        dto.setModelo(equipamento.getEquipamento().getModelo());
        if (equipamento.getEquipamento().getFabricante() != null) {
            dto.setMarca(equipamento.getEquipamento().getFabricante().getNome());
        }
        dto.setUsuarioId(equipamento.getUsuario().getId());
        dto.setNumeroSerie(equipamento.getNumeroSerie());
        dto.setGarantia(garantiaMapper.toDto(equipamento.getGarantia()));
        return dto;
    }

    public EquipamentoUsuario toEntity(EquipamentoUsuarioDto equipamentoDto) {
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

package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.EmpresaDto;
import com.frigo.frigotec.dto.GarantiaDto;
import com.frigo.frigotec.entity.Empresa;
import com.frigo.frigotec.entity.Equipamento;
import com.frigo.frigotec.entity.Garantia;
import org.springframework.stereotype.Component;

@Component
public class GarantiaMapper {

    private final EquipamentoMapper equipamentoMapper;

    public GarantiaMapper(EquipamentoMapper equipamentoMapper) {
        this.equipamentoMapper = equipamentoMapper;
    }

    public Garantia toEntity(GarantiaDto dto) {
        if (dto == null) {
            return null;
        }

        Garantia entity = new Garantia();

        entity.setGarantiaAtiva(dto.getGarantiaAtiva());
        entity.setEquipamento(equipamentoMapper.toEntity(dto.getEquipamentoDto()));
        entity.setNotaFiscal(dto.getNotaFiscal());

        return entity;
    }

    public GarantiaDto toDto(Garantia entity) {
        if (entity == null) {
            return null;
        }

        GarantiaDto dto = new GarantiaDto();
        dto.setEquipamentoDto(equipamentoMapper.toResponse(entity.getEquipamento()));
        dto.setGarantiaAtiva(entity.getGarantiaAtiva());


        return dto;
    }
}

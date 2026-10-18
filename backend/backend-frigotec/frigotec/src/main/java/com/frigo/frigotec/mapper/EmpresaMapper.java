package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.EmpresaDto;
import com.frigo.frigotec.entity.Empresa;
import org.springframework.stereotype.Component;

@Component
public class EmpresaMapper {

    public Empresa toEntity(EmpresaDto dto) {
        if (dto == null) {
            return null;
        }

        Empresa entity = new Empresa();
        entity.setRazaoSocial(dto.getRazaoSocial());
        entity.setInscricaoEstadual(dto.getInscricaoEstadual());
        entity.setCnpj(dto.getCnpj());

        return entity;
    }

    public EmpresaDto toDto(Empresa entity) {
        if (entity == null) {
            return null;
        }

        EmpresaDto dto = new EmpresaDto();
        dto.setRazaoSocial(entity.getRazaoSocial());
        dto.setInscricaoEstadual(entity.getInscricaoEstadual());
        dto.setCnpj(entity.getCnpj());

        return dto;
    }
}
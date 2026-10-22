package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.FabricanteCadastroDto;
import com.frigo.frigotec.dto.FabricanteDto;
import com.frigo.frigotec.entity.Fabricante;

public final class FabricanteMapper {

    public static Fabricante toEntity(FabricanteDto dto) {
        if (dto == null) {
            return null;
        }

        Fabricante entity = new Fabricante();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());

        return entity;
    }

    public static FabricanteDto toDto(Fabricante entity) {
        if (entity == null) {
            return null;
        }

        FabricanteDto dto = new FabricanteDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());

        return dto;
    }

    public static Fabricante toEntityCadastro(FabricanteCadastroDto dto) {
        if (dto == null) {
            return null;
        }

        Fabricante entity = new Fabricante();
        entity.setNome(dto.getNome());

        return entity;
    }

    public static FabricanteCadastroDto toDtoCadastro(Fabricante entity) {
        if (entity == null) {
            return null;
        }

        FabricanteCadastroDto dto = new FabricanteCadastroDto();
        dto.setNome(entity.getNome());

        return dto;
    }
}
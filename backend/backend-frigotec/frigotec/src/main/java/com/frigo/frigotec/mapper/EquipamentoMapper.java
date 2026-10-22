package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.*;
import com.frigo.frigotec.entity.Equipamento;
import com.frigo.frigotec.entity.Fabricante;
import org.springframework.stereotype.Component;

@Component
public final class EquipamentoMapper {



    public static EquipamentoCadastroDto toResponseCadastro(Equipamento equipamento){
        EquipamentoCadastroDto dto = new EquipamentoCadastroDto();

        dto.setFabricanteId(equipamento.getFabricante().getId());
        dto.setModelo(equipamento.getModelo());

    return dto;
    }

    public static Equipamento toEntityCadastro (EquipamentoCadastroDto equipamentoDto){


        Equipamento entity = new Equipamento();
        Fabricante entidadeFabricante = new  Fabricante();
        entidadeFabricante.setId(equipamentoDto.getFabricanteId());

        entity.setFabricante(entidadeFabricante);
        entity.setModelo(equipamentoDto.getModelo());

        return entity;
    }

    public static EquipamentoDto toResponse(Equipamento equipamento){
        EquipamentoDto dto = new EquipamentoDto();
        dto.setId(equipamento.getId());
        dto.setFabricanteId(equipamento.getFabricante().getId());
        dto.setModelo(equipamento.getModelo());

        return dto;
    }

    public static Equipamento toEntity (EquipamentoDto equipamentoDto){


        Equipamento entity = new Equipamento();
        Fabricante entidadeFabricante = new  Fabricante();
        entidadeFabricante.setId(equipamentoDto.getFabricanteId());

        entity.setId(equipamentoDto.getId());
        entity.setFabricante(entidadeFabricante);
        entity.setModelo(equipamentoDto.getModelo());

        return entity;
    }
}

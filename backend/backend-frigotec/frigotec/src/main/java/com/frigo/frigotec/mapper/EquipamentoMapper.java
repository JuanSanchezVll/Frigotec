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
   private final EquipamentoUsuarioMapper EquipamentoUsuarioMapper;

    public EquipamentoMapper(FabricanteMapper fabricanteMapper, EquipamentoUsuarioMapper equipamentoUsuarioMapper) {
        this.fabricanteMapper = fabricanteMapper;
        EquipamentoUsuarioMapper = equipamentoUsuarioMapper;
    }

    public  EquipamentoDto toResponse(Equipamento equipamento){
        EquipamentoDto equipamentoDto = new EquipamentoDto();

        FabricanteEquipamentoDto fabricanteEquipamentoDto = fabricanteMapper.toDto(equipamento.getFabricante());
        equipamentoDto.setMarca(fabricanteEquipamentoDto);
        equipamentoDto.setModelo(equipamento.getModelo());

        UsuarioDto dto =




    }
}

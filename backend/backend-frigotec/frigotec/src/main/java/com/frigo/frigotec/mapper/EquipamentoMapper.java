package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.ModeloEquipamentoDto;
import com.frigo.frigotec.entity.Equipamento;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoMapper {

    private final FabricanteMapper fabricanteMapper;

    // Injeta o mapper do fabricante
    public EquipamentoMapper(FabricanteMapper fabricanteMapper) {
        this.fabricanteMapper = fabricanteMapper;
    }

    public Equipamento toEntity(ModeloEquipamentoDto dto) {
        if (dto == null) {
            return null;
        }

        Equipamento entity = new Equipamento();
        entity.setId(dto.getId());

        // Mapeia o 'nome' do DTO para 'modelo' na Entidade
        entity.setModelo(dto.getNome());

        // Delega o fabricante
        if (dto.getFabricante() != null) {
            entity.setFabricante(fabricanteMapper.toEntity(dto.getFabricante()));
        }

        return entity;
    }

    public ModeloEquipamentoDto toDto(Equipamento entity) {
        if (entity == null) {
            return null;
        }

        ModeloEquipamentoDto dto = new ModeloEquipamentoDto();
        dto.setId(entity.getId());

        // Mapeia o 'modelo' da Entidade para 'nome' no DTO
        dto.setNome(entity.getModelo());

        // Delega o fabricante
        if (entity.getFabricante() != null) {
            dto.setFabricante(fabricanteMapper.toDto(entity.getFabricante()));
        }

        return dto;
    }
}
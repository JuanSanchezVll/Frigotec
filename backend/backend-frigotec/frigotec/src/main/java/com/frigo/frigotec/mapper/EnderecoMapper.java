package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.EnderecoDto;
import com.frigo.frigotec.entity.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {

    public Endereco toEntity(EnderecoDto dto) {
        if (dto == null) {
            return null;
        }

        Endereco entity = new Endereco();
        entity.setId(dto.getId());
        entity.setEstado(dto.getEstado());
        entity.setCidade(dto.getCidade());
        entity.setRua(dto.getRua());
        entity.setNumero(dto.getNumero());
        entity.setCep(dto.getCep());

        return entity;
    }

    public EnderecoDto toDto(Endereco entity) {
        if (entity == null) {
            return null;
        }

        EnderecoDto dto = new EnderecoDto();
        dto.setId(entity.getId());
        dto.setEstado(entity.getEstado());
        dto.setCidade(entity.getCidade());
        dto.setRua(entity.getRua());
        dto.setNumero(entity.getNumero());
        dto.setCep(entity.getCep());

        return dto;
    }
}
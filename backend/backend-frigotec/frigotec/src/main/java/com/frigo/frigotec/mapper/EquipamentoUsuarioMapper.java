package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.EquipamentoCadastroDto;
import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoUsuarioMapper {

    private final UsuarioMapper usuarioMapper;
    private final EquipamentoMapper equipamentoMapper;

    // Injeta apenas os mappers de alto nível
    public EquipamentoUsuarioMapper(UsuarioMapper usuarioMapper, EquipamentoMapper equipamentoMapper) {
        this.usuarioMapper = usuarioMapper;
        this.equipamentoMapper = equipamentoMapper;
    }


    public EquipamentoUsuario toEntity(EquipamentoCadastroDto dto) {
        if (dto == null) {
            return null;
        }

        EquipamentoUsuario entity = new EquipamentoUsuario();

        if (dto.getModelo() != null) {
            entity.setEquipamento(equipamentoMapper.toEntity(dto.getModelo()));
        }

        if (dto.getUsuario() != null) {
            entity.setUsuario(usuarioMapper.toEntity(dto.getUsuario()));
        }

        return entity;
    }

    public EquipamentoCadastroDto toDto(EquipamentoUsuario entity) {
        if (entity == null) {
            return null;
        }

        EquipamentoCadastroDto dto = new EquipamentoCadastroDto();

        // Delega e extrai o Equipamento
        if (entity.getEquipamento() != null) {
            dto.setModelo(equipamentoMapper.toDto(entity.getEquipamento()));

            if (dto.getModelo() != null) {
                dto.setMarca(dto.getModelo().getFabricante());
            }
        }

        if (entity.getUsuario() != null) {
            dto.setUsuario(usuarioMapper.toDto(entity.getUsuario()));
        }

        return dto;
    }


    public EquipamentoUsuario toEntity(EquipamentoUsuarioDto dto) {
        if (dto == null) {
            return null;
        }

        EquipamentoUsuario entity = new EquipamentoUsuario();
        entity.setId(dto.getIdEquipamentoUsuario());

        if (dto.getEquipamentoCadastroDto() != null) {
            EquipamentoUsuario tempEntity = toEntity(dto.getEquipamentoCadastroDto());
            if (tempEntity != null) {
                entity.setEquipamento(tempEntity.getEquipamento());
            }
        }

        if (dto.getUsuarioCadastroDto() != null) {
            entity.setUsuario(usuarioMapper.toEntity(dto.getUsuarioCadastroDto()));
        }

        return entity;
    }

    public EquipamentoUsuarioDto toEquipamentoUsuarioDto(EquipamentoUsuario entity) {
        if (entity == null) {
            return null;
        }

        EquipamentoUsuarioDto dto = new EquipamentoUsuarioDto();
        dto.setIdEquipamentoUsuario(entity.getId());

        dto.setEquipamentoCadastroDto(toDto(entity));

        if (entity.getUsuario() != null) {
            dto.setUsuarioCadastroDto(usuarioMapper.toDto(entity.getUsuario()));
        }

        return dto;
    }
}
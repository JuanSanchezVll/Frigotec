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

    // =======================================================
    // MAPEAMENTO: EquipamentoCadastroDto <-> EquipamentoUsuario
    // =======================================================

    public EquipamentoUsuario toEntity(EquipamentoCadastroDto dto) {
        if (dto == null) {
            return null;
        }

        EquipamentoUsuario entity = new EquipamentoUsuario();

        // Delega totalmente a construção do Equipamento para o EquipamentoMapper
        if (dto.getModelo() != null) {
            entity.setEquipamento(equipamentoMapper.toEntity(dto.getModelo()));
        }

        // Delega totalmente a construção do Usuário
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

        if (entity.getEquipamento() != null) {
            dto.setModelo(equipamentoMapper.toDto(entity.getEquipamento()));


            if (dto.getModelo() != null) {
                dto.setMarca(dto.getModelo().getFabricante());
            }
        }

        // Delega e extrai o Usuário
        if (entity.getUsuario() != null) {
            dto.setUsuario(usuarioMapper.toDto(entity.getUsuario()));
        }

        return dto;
    }



}
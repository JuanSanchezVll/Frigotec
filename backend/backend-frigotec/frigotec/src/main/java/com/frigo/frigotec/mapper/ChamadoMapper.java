package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.ChamadoDto;
import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.entity.EquipamentoUsuario;

public class ChamadoMapper {

    private final UsuarioMapper usuarioMapper;

private final EquipamentoUsuarioMapper equipamentoUsuarioMapper;

    public ChamadoMapper(UsuarioMapper usuarioMapper, EquipamentoUsuarioMapper equipamentoMapper) {
        this.usuarioMapper = usuarioMapper;
        this.equipamentoUsuarioMapper = equipamentoMapper;
    }

    public ChamadoDto toResponse(Chamado chamado){


        ChamadoDto dto = new ChamadoDto();
        dto.setDataPedido(chamado.getDataPedido());
        dto.setDescricao(chamado.getDescricao());
        dto.setProblema(chamado.getProblema());

        dto.setUsuario(usuarioMapper.toDto(chamado.getUsuario()));

        dto.setEquipamentoUsuario(equipamentoUsuarioMapper.toResponse(chamado.getEquipamentoUsuario()));


        return dto;
    }

    public Chamado toEntity (ChamadoDto dto){

        Chamado entity = new Chamado();


        entity.setDataPedido(dto.getDataPedido());
        entity.setDescricao(dto.getDescricao());
        entity.setProblema(dto.getProblema());
        entity.setUsuario(usuarioMapper.toEntity(dto.getUsuario()));
        entity.setEquipamentoUsuario(equipamentoUsuarioMapper.toEntity(dto.getEquipamentoUsuario()));

        return entity;
    }
}

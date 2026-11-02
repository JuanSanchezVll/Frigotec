package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.ChamadoDto;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.dto.ChamadoCadastroDto;
import org.springframework.stereotype.Component;

@Component
public class ChamadoMapper {

    private final UsuarioMapper usuarioMapper;
    private final HistoricoMapper historicoMapper;

private final EquipamentoUsuarioMapper equipamentoUsuarioMapper;

    public ChamadoMapper(UsuarioMapper usuarioMapper, HistoricoMapper historicoMapper, EquipamentoUsuarioMapper equipamentoUsuarioMapper) {
        this.usuarioMapper = usuarioMapper;
        this.historicoMapper = historicoMapper;
        this.equipamentoUsuarioMapper = equipamentoUsuarioMapper;
    }

    public ChamadoDto toDto(Chamado chamado){


        ChamadoDto dto = new ChamadoDto();
        dto.setDataPedido(chamado.getDataPedido());
        dto.setDescricao(chamado.getDescricao());
        dto.setProblema(chamado.getProblema());

        dto.setUsuario(usuarioMapper.toDto(chamado.getUsuario()));

        dto.setHistorico(chamado.getHistoricos().stream()
                .map(historicoMapper::toHistoricoDto)
                .toList());

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
        if (dto.getHistorico() != null) {
            dto.getHistorico().forEach(h ->
                    entity.adicionarHistorico(historicoMapper.toHistoricoEntity(h)));
        }
        return entity;
    }


    public Chamado toEntityCadastro(ChamadoCadastroDto dto) {
        if (dto == null) {
            return null;
        }

        Chamado entity = new Chamado();
        entity.setDescricao(dto.getDescricao());
        entity.setProblema(dto.getProblema());

        return entity;
    }
}

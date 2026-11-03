package com.frigo.frigotec.mapper;

import com.frigo.frigotec.dto.ChamadoCadastroDto;
import com.frigo.frigotec.dto.ChamadoDto;
import com.frigo.frigotec.dto.EncerramentoDto;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.entity.Encerramento;
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

    public ChamadoDto toDto(Chamado chamado) {
        ChamadoDto dto = new ChamadoDto();
        dto.setId(chamado.getId());
        dto.setProtocolo(chamado.getProtocolo());
        dto.setDataPedido(chamado.getDataPedido());
        dto.setDescricao(chamado.getDescricao());
        dto.setProblema(chamado.getProblema());
        dto.setStatusAtual(chamado.statusAtual());
        dto.setUsuario(usuarioMapper.toDto(chamado.getUsuario()));
        dto.setTecnico(chamado.getTecnico() != null ? usuarioMapper.toDto(chamado.getTecnico()) : null);
        dto.setVisitaAgendadaEm(chamado.getVisitaAgendadaEm());
        dto.setHistorico(chamado.getHistoricos().stream().map(historicoMapper::toHistoricoDto).toList());
        if (!chamado.getEncerramentos().isEmpty()) {
            Encerramento ultimo = chamado.getEncerramentos().get(chamado.getEncerramentos().size() - 1);
            EncerramentoDto encerramento = new EncerramentoDto();
            encerramento.setMotivo(ultimo.getMotivo());
            encerramento.setDescricao(ultimo.getDescricao());
            dto.setEncerramento(encerramento);
        }
        dto.setEquipamentoUsuario(equipamentoUsuarioMapper.toResponse(chamado.getEquipamentoUsuario()));
        return dto;
    }

    public Chamado toEntityCadastro(ChamadoCadastroDto dto) {
        Chamado entity = new Chamado();
        entity.setDescricao(dto.getDescricao());
        entity.setProblema(dto.getProblema());
        return entity;
    }
}

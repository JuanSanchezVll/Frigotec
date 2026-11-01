package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.ChamadoCadastroDto;
import com.frigo.frigotec.dto.ChamadoDto;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.entity.Usuario;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.mapper.ChamadoMapper;
import com.frigo.frigotec.repository.ChamadoRepository;
import com.frigo.frigotec.repository.EquipamentoUsuarioRepository;
import com.frigo.frigotec.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChamadoService {
    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipamentoUsuarioRepository equipamentoUsuarioRepository;
    private final ChamadoMapper mapper;

    public ChamadoService(ChamadoRepository chamadoRepository,
                          UsuarioRepository usuarioRepository,
                          EquipamentoUsuarioRepository equipamentoUsuarioRepository,
                          ChamadoMapper mapper) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.equipamentoUsuarioRepository = equipamentoUsuarioRepository;
        this.mapper = mapper;
    }

    public List<ChamadoDto> listarChamados() {
        return chamadoRepository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public ChamadoDto salvar(ChamadoCadastroDto novoChamado) {
        Usuario usuario = usuarioRepository.findById(novoChamado.getUsuarioId())
                .orElseThrow(() -> new BuscaNaoEncontradaException("Usuário não encontrado!"));

        EquipamentoUsuario equipamentoUsuario = equipamentoUsuarioRepository.findById(novoChamado.getEquipamentoUsuarioId())
                .orElseThrow(() -> new BuscaNaoEncontradaException("Equipamento do usuário não encontrado!"));

        if (!equipamentoUsuario.getUsuario().getId().equals(usuario.getId())) {
            throw new BuscaNaoEncontradaException("Esse equipamento não pertence ao usuário informado!");
        }

        Chamado chamado = mapper.toEntityCadastro(novoChamado);
        chamado.setDataPedido(LocalDate.now());
        chamado.setUsuario(usuario);
        chamado.setEquipamentoUsuario(equipamentoUsuario);

        return mapper.toResponse(chamadoRepository.save(chamado));
    }
}
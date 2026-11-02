package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.ChamadoCadastroDto;
import com.frigo.frigotec.dto.ChamadoDto;
import com.frigo.frigotec.dto.StatusChamadoAlteradoEvento;
import com.frigo.frigotec.entity.Chamado;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.entity.Historico;
import com.frigo.frigotec.entity.Usuario;
import com.frigo.frigotec.exception.BuscaEncontradaException;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.mapper.ChamadoMapper;
import com.frigo.frigotec.repository.ChamadoRepository;
import com.frigo.frigotec.repository.EquipamentoUsuarioRepository;
import com.frigo.frigotec.repository.UsuarioRepository;
import com.frigo.frigotec.util.StatusChamadoEnum;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChamadoService {
    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipamentoUsuarioRepository equipamentoUsuarioRepository;
    private final ChamadoMapper mapper;
    private final ApplicationEventPublisher publisher; // injetar no construtor

    public ChamadoService(ChamadoRepository chamadoRepository,
                          UsuarioRepository usuarioRepository,
                          EquipamentoUsuarioRepository equipamentoUsuarioRepository,
                          ChamadoMapper mapper, ApplicationEventPublisher publisher) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.equipamentoUsuarioRepository = equipamentoUsuarioRepository;
        this.mapper = mapper;
        this.publisher = publisher;
    }

    public List<ChamadoDto> listarChamados() {
        return chamadoRepository.findAll().stream()
                .map(mapper::toDto)
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

        Historico historicoInicial = new Historico();
        historicoInicial.setStatus(StatusChamadoEnum.ENVIADO);
        historicoInicial.setDataAlteracao(LocalDate.now());
        chamado.adicionarHistorico(historicoInicial);

        return mapper.toDto(chamadoRepository.save(chamado));
    }


    public ChamadoDto buscarPorId(Long idChamado){
        if(!chamadoRepository.existsById(idChamado)){
            throw new BuscaEncontradaException("Chamado não encontrado");
        }
        Optional<Chamado> chamado = chamadoRepository.findById(idChamado);

        return mapper.toDto(chamado.get());
    }


    public List<ChamadoDto> buscarPorUsuario(UUID idUsuario){
        if(!usuarioRepository.existsById(idUsuario)){
            throw new BuscaEncontradaException("Usuario inexistente ");
        }

        List <ChamadoDto> dtos = chamadoRepository.findbyUsuarioId(idUsuario).stream().map(mapper::toDto).toList();

        return dtos;
    }

    @Transactional(readOnly = true)
    public List<ChamadoDto> buscarPorStatus(String status) {
        StatusChamadoEnum statusEnum = StatusChamadoEnum.valueOf(status.toUpperCase());

        return chamadoRepository.findByHistoricosStatus(statusEnum).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public ChamadoDto atualizarStatus(Long id, StatusChamadoEnum novoStatus) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new BuscaNaoEncontradaException("Chamado não encontrado!"));

        Historico historico = new Historico();
        historico.setStatus(novoStatus);
        historico.setDataAlteracao(LocalDate.now());
        chamado.adicionarHistorico(historico);

        chamadoRepository.save(chamado);
        publisher.publishEvent(new StatusChamadoAlteradoEvento(chamado, novoStatus));

        return mapper.toDto(chamado);
    }
}
package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.EquipamentoCadastroDto;
import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.entity.Equipamento;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.entity.Usuario;
import com.frigo.frigotec.exception.BuscaEncontradaException;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.mapper.EquipamentoMapper;
import com.frigo.frigotec.repository.EquipamentoRepository;
import com.frigo.frigotec.repository.EquipamentoUsuarioRepository;
import com.frigo.frigotec.repository.FabricanteRepository;
import com.frigo.frigotec.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EquipamentoService {


    private final EquipamentoRepository equipamentoRepository;

    private final EquipamentoUsuarioRepository equipamentoUsuarioRepository;

    private final UsuarioRepository usuarioRepository;

    private final FabricanteRepository fabricanteRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository, EquipamentoUsuarioRepository equipamentoUsuarioRepository, UsuarioRepository usuarioRepository, FabricanteRepository fabricanteRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.equipamentoUsuarioRepository = equipamentoUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.fabricanteRepository = fabricanteRepository;
    }



    public EquipamentoUsuario salvarEquipamentoUsuario(EquipamentoUsuarioDto dto, UUID idUsuario) {


        if(!(usuarioRepository.existsById(idUsuario))) {
            throw new BuscaNaoEncontradaException("Usuario não encontrado");
        }

        if(!(equipamentoRepository.existsById(dto.getEquipamentoId()))) {
            throw new BuscaNaoEncontradaException("Equipamento não encontrado");
        }

        Usuario  usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if(usuario == null) {
            throw new BuscaNaoEncontradaException("Usuario não encontrado");
        }

        Equipamento equipamento ;
        equipamento = equipamentoRepository.findById(dto.getEquipamentoId()).orElse(null);
        if(equipamento == null) {
            throw new BuscaNaoEncontradaException("Não trabalhamos com o equipamento cadastrado");
        }
        EquipamentoUsuario equipamentoUsuario = new  EquipamentoUsuario();

        equipamentoUsuario.setEquipamento(equipamento);
        equipamentoUsuario.setUsuario(usuario);



        equipamentoUsuarioRepository.save(equipamentoUsuario);

        return equipamentoUsuario;
    }
    @Transactional
    public Equipamento salvarEquipamento(EquipamentoCadastroDto equipamentoDto){

        if(!(fabricanteRepository.existsById(equipamentoDto.getFabricanteId()))) {
            throw new BuscaEncontradaException("A fabrica não existe para esse modelo");
        }


        if(equipamentoRepository.existsByModeloIgnoreCaseAndFabricanteId(equipamentoDto.getModelo(), equipamentoDto.getFabricanteId())){
            throw new BuscaEncontradaException("Esse modelo de equipamento já existe");
        }


        return equipamentoRepository.save( EquipamentoMapper.toEntityCadastro(equipamentoDto));
    }
}

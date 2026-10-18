package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.EquipamentoDto;
import com.frigo.frigotec.entity.Equipamento;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.entity.Usuario;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.repository.EquipamentoRepository;
import com.frigo.frigotec.repository.EquipamentoUsuarioRepository;
import com.frigo.frigotec.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EquipamentoService {


    private final EquipamentoRepository equipamentoRepository;

    private final EquipamentoUsuarioRepository equipamentoUsuarioRepository;


    private final UsuarioRepository usuarioRepository;


    public EquipamentoService(EquipamentoRepository equipamentoRepository, EquipamentoUsuarioRepository equipamentoUsuarioRepository,  UsuarioRepository usuarioRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.equipamentoUsuarioRepository = equipamentoUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public EquipamentoDto save(EquipamentoDto dto, UUID idUsuario) {

        Usuario  usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if(usuario == null) {
            throw new BuscaNaoEncontradaException("Usuario não encontrado");
        }

        Equipamento equipamento = new  Equipamento();
        equipamento = equipamentoRepository.findByModeloAndFabricanteNome(dto.getModelo(), dto.getMarca().getNome());
        if(equipamento == null) {
            throw new BuscaNaoEncontradaException("Não trabalhamos com o equipamento cadastrado");
        }
        EquipamentoUsuario equipamentoUsuario = new  EquipamentoUsuario();

        equipamentoUsuario.setEquipamento(equipamento);
        equipamentoUsuario.setUsuario(usuario);

        equipamentoUsuarioRepository.save(equipamentoUsuario);

        return dto;
    }
}

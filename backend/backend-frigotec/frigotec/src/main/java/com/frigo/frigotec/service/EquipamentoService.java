package com.frigo.frigotec.service;

import com.frigo.frigotec.dto.EquipamentoCadastroDto;
import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.entity.Equipamento;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.entity.Usuario;
import com.frigo.frigotec.exception.BuscaNaoEncontradaException;
import com.frigo.frigotec.mapper.EquipamentoUsuarioMapper;
import com.frigo.frigotec.repository.EquipamentoRepository;
import com.frigo.frigotec.repository.EquipamentoUsuarioRepository;
import com.frigo.frigotec.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EquipamentoService {


    @Autowired
    private EquipamentoRepository equipamentoRepository;

    private EquipamentoUsuarioRepository equipamentoUsuarioRepository;

    @Autowired
    private EquipamentoUsuarioMapper equipamentoUsuarioMapper;

    private UsuarioRepository usuarioRepository;


    public EquipamentoService(EquipamentoRepository equipamentoRepository) {
        this.equipamentoRepository = equipamentoRepository;
    }

    public EquipamentoCadastroDto save(EquipamentoCadastroDto dto, UUID idUsuario) {

        Usuario usuario;
        usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if(usuario == null) {
            throw new BuscaNaoEncontradaException("Usuario não encontrado");
        }

        Equipamento equipamento;
        equipamento = equipamentoRepository.findByModeloAndFabricante(dto.getModelo().getNome(), dto.getModelo().getFabricante().getNome());
        if(equipamento == null) {
            throw new BuscaNaoEncontradaException("Não trabalhamos com o equipamento cadastrado");
        }
        EquipamentoUsuario equipamentoUsuario = new EquipamentoUsuario();

        equipamentoUsuario.setUsuario(usuario);
        equipamentoUsuario.setEquipamento(equipamento);


        return null;
    }
}

package com.frigo.frigotec.controller;

import com.frigo.frigotec.dto.EquipamentoCadastroDto;
import com.frigo.frigotec.dto.EquipamentoDto;
import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.mapper.EquipamentoMapper;
import com.frigo.frigotec.mapper.EquipamentoUsuarioMapper;
import com.frigo.frigotec.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private EquipamentoService service;
    @Autowired
    private EquipamentoUsuarioMapper equipamentoUsuarioMapper;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.service = equipamentoService;
    }



    @PostMapping("/{idUsuario}")
    public ResponseEntity<EquipamentoUsuarioDto> salvarEquipamentoUsuario(@RequestBody @Valid EquipamentoUsuarioDto dto, @PathVariable UUID idUsuario) {


        EquipamentoUsuario equipamentoUsuario = service.salvarEquipamentoUsuario(dto, idUsuario);

        EquipamentoUsuarioDto equipamentoUsuarioDto =equipamentoUsuarioMapper.toResponse(equipamentoUsuario);

        return ResponseEntity.ok().body(equipamentoUsuarioDto);
    }

    @PostMapping
    public ResponseEntity<EquipamentoDto> salvarEquipamentoFrigo(@RequestBody @Valid EquipamentoCadastroDto dto) {

       EquipamentoDto equipamentoResposta = EquipamentoMapper.toResponse(service.salvarEquipamento(dto));


       return ResponseEntity.status(HttpStatus.CREATED).body(equipamentoResposta);
    }

}

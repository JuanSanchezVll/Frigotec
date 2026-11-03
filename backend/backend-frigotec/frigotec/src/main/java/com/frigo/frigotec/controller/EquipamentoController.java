package com.frigo.frigotec.controller;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.EquipamentoCadastroDto;
import com.frigo.frigotec.dto.EquipamentoDto;
import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.entity.EquipamentoUsuario;
import com.frigo.frigotec.mapper.EquipamentoMapper;
import com.frigo.frigotec.mapper.EquipamentoUsuarioMapper;
import com.frigo.frigotec.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private final EquipamentoService service;
    private final EquipamentoUsuarioMapper equipamentoUsuarioMapper;

    public EquipamentoController(EquipamentoService service, EquipamentoUsuarioMapper equipamentoUsuarioMapper) {
        this.service = service;
        this.equipamentoUsuarioMapper = equipamentoUsuarioMapper;
    }

    /** CLIENTE cadastra a própria unidade (o id do caminho precisa ser o do token). */
    @PostMapping("/{idUsuario}")
    public ResponseEntity<EquipamentoUsuarioDto> salvarEquipamentoUsuario(@RequestBody @Valid EquipamentoUsuarioDto dto,
                                                                          @PathVariable UUID idUsuario,
                                                                          @AuthenticationPrincipal Jwt jwt) {
        EquipamentoUsuario salvo = service.salvarEquipamentoUsuario(dto, idUsuario, AutorAutenticado.de(jwt));
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamentoUsuarioMapper.toResponse(salvo));
    }

    /** TECNICO/ADMIN cadastram modelos no catálogo. */
    @PostMapping
    public ResponseEntity<EquipamentoDto> salvarEquipamentoFrigo(@RequestBody @Valid EquipamentoCadastroDto dto) {
        EquipamentoDto resposta = EquipamentoMapper.toResponse(service.salvarEquipamento(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
}

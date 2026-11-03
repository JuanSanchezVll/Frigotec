package com.frigo.frigotec.controller;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.EquipamentoCadastroDto;
import com.frigo.frigotec.dto.EquipamentoDto;
import com.frigo.frigotec.dto.EquipamentoUsuarioAtualizacaoDto;
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

    /** CRUD (atualizar): CLIENTE corrige série e nota fiscal da própria unidade. Outro cliente: 404. */
    @PatchMapping("/{id}")
    public ResponseEntity<EquipamentoUsuarioDto> atualizarEquipamentoUsuario(@PathVariable Long id,
                                                                             @RequestBody EquipamentoUsuarioAtualizacaoDto dto,
                                                                             @AuthenticationPrincipal Jwt jwt) {
        EquipamentoUsuario atualizado = service.atualizarEquipamentoUsuario(id, dto, AutorAutenticado.de(jwt));
        return ResponseEntity.ok(equipamentoUsuarioMapper.toResponse(atualizado));
    }

    /** CRUD (excluir): CLIENTE exclui a própria unidade. Com chamado vinculado: 409 (o histórico é preservado). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEquipamentoUsuario(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        service.excluirEquipamentoUsuario(id, AutorAutenticado.de(jwt));
        return ResponseEntity.noContent().build();
    }

    /** TECNICO/ADMIN cadastram modelos no catálogo. */
    @PostMapping
    public ResponseEntity<EquipamentoDto> salvarEquipamentoFrigo(@RequestBody @Valid EquipamentoCadastroDto dto) {
        EquipamentoDto resposta = EquipamentoMapper.toResponse(service.salvarEquipamento(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }
}

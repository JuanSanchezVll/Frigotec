package com.frigo.frigotec.controller;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.ColaboradorCadastroDto;
import com.frigo.frigotec.dto.EquipamentoUsuarioDto;
import com.frigo.frigotec.dto.LoginRespostaDto;
import com.frigo.frigotec.dto.UsuarioCadastroDto;
import com.frigo.frigotec.dto.UsuarioDto;
import com.frigo.frigotec.dto.UsuarioLoginDto;
import com.frigo.frigotec.mapper.EquipamentoUsuarioMapper;
import com.frigo.frigotec.service.EquipamentoService;
import com.frigo.frigotec.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final EquipamentoService equipamentoService;
    private final EquipamentoUsuarioMapper equipamentoUsuarioMapper;

    public UsuarioController(UsuarioService usuarioService, EquipamentoService equipamentoService,
                             EquipamentoUsuarioMapper equipamentoUsuarioMapper) {
        this.usuarioService = usuarioService;
        this.equipamentoService = equipamentoService;
        this.equipamentoUsuarioMapper = equipamentoUsuarioMapper;
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid UsuarioCadastroDto novoUsuario) {
        UsuarioDto usuarioDto = usuarioService.cadastrar(novoUsuario);
        return ResponseEntity.status(201).body(usuarioDto);
    }

    @PostMapping("/colaboradores")
    public ResponseEntity<UsuarioDto> cadastrarColaborador(@RequestBody @Valid ColaboradorCadastroDto novoColaborador) {
        UsuarioDto usuarioDto = usuarioService.cadastrarColaborador(novoColaborador);
        return ResponseEntity.status(201).body(usuarioDto);
    }

    @GetMapping("/colaboradores")
    public ResponseEntity<List<UsuarioDto>> listarColaboradores() {
        return ResponseEntity.ok(usuarioService.listarColaboradores());
    }

    /** SDD RN-EUS-06: o cliente vê só os próprios equipamentos; técnico e admin veem os de qualquer cliente. */
    @GetMapping("/{id}/equipamentos")
    public ResponseEntity<List<EquipamentoUsuarioDto>> equipamentosDoUsuario(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
        List<EquipamentoUsuarioDto> equipamentos = equipamentoService.listarPorUsuario(id, AutorAutenticado.de(jwt)).stream()
                .map(equipamentoUsuarioMapper::toResponse)
                .toList();
        return ResponseEntity.ok(equipamentos);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRespostaDto> login(@RequestBody @Valid UsuarioLoginDto dadosLogin) {
        LoginRespostaDto resposta = usuarioService.autenticar(dadosLogin);
        return ResponseEntity.status(200).body(resposta);
    }
}
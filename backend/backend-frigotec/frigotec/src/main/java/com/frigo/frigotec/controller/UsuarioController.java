package com.frigo.frigotec.controller;

import com.frigo.frigotec.dto.UsuarioCadastroDto;
import com.frigo.frigotec.dto.UsuarioDto;
import com.frigo.frigotec.dto.UsuarioLoginDto;
import com.frigo.frigotec.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> cadastrar(@RequestBody @Valid UsuarioCadastroDto novoUsuario) {
        UsuarioDto usuarioDto = usuarioService.cadastrar(novoUsuario);
        return ResponseEntity.status(201).body(usuarioDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDto> login(@RequestBody @Valid UsuarioLoginDto dadosLogin) {
        UsuarioDto usuarioDto = usuarioService.autenticar(dadosLogin);
        return ResponseEntity.status(200).body(usuarioDto);
    }
}
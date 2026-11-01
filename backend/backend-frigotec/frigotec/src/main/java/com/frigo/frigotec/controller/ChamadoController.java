package com.frigo.frigotec.controller;

import com.frigo.frigotec.dto.ChamadoCadastroDto;
import com.frigo.frigotec.dto.ChamadoDto;
import com.frigo.frigotec.service.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @GetMapping
    public ResponseEntity<List<ChamadoDto>> listarChamados() {
        List<ChamadoDto> chamados = chamadoService.listarChamados();

        return ResponseEntity.status(200).body(chamados);
    }

    @PostMapping
    public ResponseEntity<ChamadoDto> salvar(@RequestBody @Valid ChamadoCadastroDto novoChamado) {
        ChamadoDto chamadoDto = chamadoService.salvar(novoChamado);

        return ResponseEntity.status(201).body(chamadoDto);
    }
}
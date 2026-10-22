package com.frigo.frigotec.controller;

import com.frigo.frigotec.dto.OrcamentoCadastroDto;
import com.frigo.frigotec.dto.OrcamentoDto;
import com.frigo.frigotec.service.OrcamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {
    private final OrcamentoService orcamentoService;

    public OrcamentoController(OrcamentoService orcamentoService) {
        this.orcamentoService = orcamentoService;
    }

    @PostMapping
    public ResponseEntity<OrcamentoDto> salvar(@RequestBody @Valid OrcamentoDto novoOrcamento){
        OrcamentoDto orcamentoDto = orcamentoService.salvar(novoOrcamento);

        return ResponseEntity.status(201).body(orcamentoDto);

    }

    @GetMapping
    public ResponseEntity<OrcamentoDto> buscarOrcamentoPorId(@PathVariable Long id){
        OrcamentoDto orcamento = orcamentoService.buscarOrcamentoPorId(id);

        return ResponseEntity.status(200).body(orcamento);
    }
}

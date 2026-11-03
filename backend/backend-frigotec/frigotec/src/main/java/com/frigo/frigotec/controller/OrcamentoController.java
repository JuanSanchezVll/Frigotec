package com.frigo.frigotec.controller;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.OrcamentoCadastroDto;
import com.frigo.frigotec.dto.OrcamentoDto;
import com.frigo.frigotec.dto.RespostaOrcamentoDto;
import com.frigo.frigotec.service.OrcamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orcamentos")
public class OrcamentoController {

    private final OrcamentoService orcamentoService;

    public OrcamentoController(OrcamentoService orcamentoService) {
        this.orcamentoService = orcamentoService;
    }

    /** TECNICO/ADMIN (SegurancaConfig); a regra do técnico responsável está no service. */
    @PostMapping
    public ResponseEntity<OrcamentoDto> salvar(@RequestBody @Valid OrcamentoCadastroDto dto, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(201).body(orcamentoService.salvar(dto, AutorAutenticado.de(jwt)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrcamentoDto> buscarOrcamentoPorId(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(orcamentoService.buscarOrcamentoPorId(id, AutorAutenticado.de(jwt)));
    }

    /** CLIENTE dono responde: {"aceito": true|false}. */
    @PatchMapping("/{id}/resposta")
    public ResponseEntity<OrcamentoDto> responder(@PathVariable Long id,
                                                  @RequestBody @Valid RespostaOrcamentoDto dto,
                                                  @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(orcamentoService.responder(id, dto, AutorAutenticado.de(jwt)));
    }
}

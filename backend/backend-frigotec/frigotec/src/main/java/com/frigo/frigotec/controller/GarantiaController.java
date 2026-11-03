package com.frigo.frigotec.controller;

import com.frigo.frigotec.dto.GarantiaDto;
import com.frigo.frigotec.dto.ValidacaoGarantiaDto;
import com.frigo.frigotec.service.GarantiaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/garantias")
public class GarantiaController {

    private final GarantiaService service;

    public GarantiaController(GarantiaService service) {
        this.service = service;
    }

    /** TECNICO/ADMIN validam ou recusam a garantia (regra de acesso em SegurancaConfig). */
    @PatchMapping("/{id}/validacao")
    public ResponseEntity<GarantiaDto> validar(@PathVariable Long id, @RequestBody @Valid ValidacaoGarantiaDto dto) {
        return ResponseEntity.ok(service.validar(id, dto));
    }
}

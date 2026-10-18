package com.frigo.frigotec.controller;

import com.frigo.frigotec.dto.EquipamenentoFrigoDto;
import com.frigo.frigotec.dto.EquipamentoDto;
import com.frigo.frigotec.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private EquipamentoService service;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.service = equipamentoService;
    }



    @PostMapping("/{idUsuario}")
    public ResponseEntity<EquipamentoDto> save(@RequestBody @Valid EquipamentoDto dto, @PathVariable UUID idUsuario) {


        service.save(dto, idUsuario);

        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/saveFrigo")
    public ResponseEntity<EquipamenentoFrigoDto> saveFrigo(@RequestBody EquipamenentoFrigoDto dto) {
        return null;
    }

}

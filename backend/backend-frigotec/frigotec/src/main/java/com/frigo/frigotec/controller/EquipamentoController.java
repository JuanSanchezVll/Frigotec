package com.frigo.frigotec.controller;

import com.frigo.frigotec.dto.EquipamenentoFrigoDto;
import com.frigo.frigotec.dto.EquipamentoCadastroDto;
import com.frigo.frigotec.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private EquipamentoService equipamentoService;

    public EquipamentoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }



    @PostMapping
    public ResponseEntity<EquipamentoCadastroDto> save(@RequestBody @Valid EquipamentoCadastroDto dto, @PathVariable Long idUsuario) {



        return null;
    }

    @PostMapping("/saveFrigo")
    public ResponseEntity<EquipamenentoFrigoDto> saveFrigo(@RequestBody EquipamenentoFrigoDto dto) {
        return null;
    }

}

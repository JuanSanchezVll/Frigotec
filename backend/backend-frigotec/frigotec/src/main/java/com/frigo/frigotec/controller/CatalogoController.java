package com.frigo.frigotec.controller;

import com.frigo.frigotec.dto.EquipamentoDto;
import com.frigo.frigotec.mapper.EquipamentoMapper;
import com.frigo.frigotec.service.EquipamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** SDD RN-EQP-04: modelos da marca escolhida (alimenta o select de modelo do cadastro de equipamento). */
@RestController
@RequestMapping("/fabricantes")
public class CatalogoController {

    private final EquipamentoService equipamentoService;

    public CatalogoController(EquipamentoService equipamentoService) {
        this.equipamentoService = equipamentoService;
    }

    @GetMapping("/{id}/equipamentos")
    public ResponseEntity<List<EquipamentoDto>> modelosDaMarca(@PathVariable Long id) {
        return ResponseEntity.ok(equipamentoService.listarPorFabricante(id).stream().map(EquipamentoMapper::toResponse).toList());
    }
}

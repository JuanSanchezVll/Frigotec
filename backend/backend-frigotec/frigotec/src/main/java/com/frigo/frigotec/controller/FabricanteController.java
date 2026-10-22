package com.frigo.frigotec.controller;

import com.frigo.frigotec.dto.FabricanteCadastroDto;
import com.frigo.frigotec.dto.FabricanteDto;
import com.frigo.frigotec.entity.Fabricante;
import com.frigo.frigotec.service.FabricanteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fabricantes")
public class FabricanteController {


    @Autowired
    private final FabricanteService service;


    public FabricanteController(FabricanteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FabricanteDto> salvar(@RequestBody @Valid FabricanteCadastroDto fabricanteDto) {

        FabricanteDto dtoResponse = service.salvar(fabricanteDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);


    }



}

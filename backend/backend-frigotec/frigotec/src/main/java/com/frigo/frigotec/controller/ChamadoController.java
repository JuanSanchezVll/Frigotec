package com.frigo.frigotec.controller;

import com.frigo.frigotec.dto.ChamadoCadastroDto;
import com.frigo.frigotec.dto.ChamadoDto;
import com.frigo.frigotec.exception.BuscaEncontradaException;
import com.frigo.frigotec.service.ChamadoService;
import com.frigo.frigotec.util.StatusChamadoEnum;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoDto> buscarChamado(@PathVariable Long id) {

        ChamadoDto chamadoDto = chamadoService.buscarPorId(id);

        return ResponseEntity.status(200).body(chamadoDto);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity <List <ChamadoDto>> buscarPorUsuario (@PathVariable UUID idUsuario) {
        if(idUsuario == null){
            throw new BuscaEncontradaException("Usuario inexistente ");
        }

        List<ChamadoDto> chamadosDto = chamadoService.buscarPorUsuario(idUsuario);

        if(chamadosDto == null || chamadosDto.isEmpty()){
            return ResponseEntity.status(204).body(null);
        }

        return ResponseEntity.status(200).body(chamadosDto);
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<ChamadoDto>> buscarPorStatus(@PathVariable String status) {
        try {
            List<ChamadoDto> chamados = chamadoService.buscarPorStatus(status);

            if (chamados.isEmpty()) {
                return ResponseEntity.status(204).build();
            }

            return ResponseEntity.status(200).body(chamados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ChamadoDto> atualizarStatus(@PathVariable Long id,
                                                      @RequestParam StatusChamadoEnum novoStatus) {
        ChamadoDto chamadoDto = chamadoService.atualizarStatus(id, novoStatus);

        return ResponseEntity.status(200).body(chamadoDto);
    }
}
package com.frigo.frigotec.controller;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.AtribuicaoDto;
import com.frigo.frigotec.dto.ChamadoCadastroDto;
import com.frigo.frigotec.dto.ChamadoDto;
import com.frigo.frigotec.dto.EncerramentoDto;
import com.frigo.frigotec.dto.VisitaDto;
import com.frigo.frigotec.exception.RegraDeNegocioException;
import com.frigo.frigotec.service.ChamadoService;
import com.frigo.frigotec.util.StatusChamadoEnum;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/** Quem pode chamar cada rota está em SegurancaConfig; as regras finas (dono, responsável) estão no ChamadoService. */
@RestController
@RequestMapping("/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    /** Equipe: lista todos; filtros opcionais por status atual e por chamados sem técnico (fila). */
    @GetMapping
    public ResponseEntity<List<ChamadoDto>> listar(@RequestParam(required = false) StatusChamadoEnum status,
                                                   @RequestParam(defaultValue = "false") boolean semTecnico) {
        return ResponseEntity.ok(chamadoService.listar(status, semTecnico));
    }

    @PostMapping
    public ResponseEntity<ChamadoDto> salvar(@RequestBody @Valid ChamadoCadastroDto dto, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.status(201).body(chamadoService.salvar(dto, AutorAutenticado.de(jwt)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoDto> buscarChamado(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id, AutorAutenticado.de(jwt)));
    }

    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<List<ChamadoDto>> buscarPorUsuario(@PathVariable UUID idUsuario,
                                                             @RequestParam(required = false) String situacao,
                                                             @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(chamadoService.buscarPorUsuario(idUsuario, situacao, AutorAutenticado.de(jwt)));
    }

    @GetMapping("/tecnicos/{idTecnico}")
    public ResponseEntity<List<ChamadoDto>> buscarPorTecnico(@PathVariable UUID idTecnico, @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(chamadoService.buscarPorTecnico(idTecnico, AutorAutenticado.de(jwt)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ChamadoDto>> buscarPorStatus(@PathVariable String status) {
        try {
            return ResponseEntity.ok(chamadoService.listar(StatusChamadoEnum.valueOf(status.toUpperCase()), false));
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Status inválido: " + status + ".");
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ChamadoDto> atualizarStatus(@PathVariable Long id,
                                                      @RequestParam StatusChamadoEnum novoStatus,
                                                      @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(chamadoService.atualizarStatus(id, novoStatus, AutorAutenticado.de(jwt)));
    }

    @PostMapping("/{id}/atribuicao")
    public ResponseEntity<ChamadoDto> atribuir(@PathVariable Long id,
                                               @RequestBody(required = false) AtribuicaoDto dto,
                                               @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(chamadoService.atribuir(id, dto == null ? new AtribuicaoDto() : dto, AutorAutenticado.de(jwt)));
    }

    @PatchMapping("/{id}/visita")
    public ResponseEntity<ChamadoDto> agendarVisita(@PathVariable Long id,
                                                    @RequestBody @Valid VisitaDto dto,
                                                    @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(chamadoService.agendarVisita(id, dto, AutorAutenticado.de(jwt)));
    }

    @PostMapping("/{id}/encerramento")
    public ResponseEntity<ChamadoDto> encerrar(@PathVariable Long id,
                                               @RequestBody @Valid EncerramentoDto dto,
                                               @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(chamadoService.encerrar(id, dto, AutorAutenticado.de(jwt)));
    }
}

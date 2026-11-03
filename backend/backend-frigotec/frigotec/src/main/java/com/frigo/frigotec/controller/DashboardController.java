package com.frigo.frigotec.controller;

import com.frigo.frigotec.config.AutorAutenticado;
import com.frigo.frigotec.dto.DashboardTecnicoDto;
import com.frigo.frigotec.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /** TECNICO/ADMIN (SegurancaConfig): KPIs do painel do técnico, calculados no banco. */
    @GetMapping("/tecnico")
    public ResponseEntity<DashboardTecnicoDto> doTecnico(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(dashboardService.doTecnico(AutorAutenticado.de(jwt)));
    }
}

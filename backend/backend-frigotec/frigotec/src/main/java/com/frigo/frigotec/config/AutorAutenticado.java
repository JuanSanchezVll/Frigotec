package com.frigo.frigotec.config;

import com.frigo.frigotec.entity.Perfil;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

/**
 * Quem está fazendo o pedido, lido do token JWT (SDD RN-USR-06): o id e o perfil nunca vêm do corpo da requisição.
 * Os controllers recebem o Jwt com @AuthenticationPrincipal e chamam AutorAutenticado.de(jwt).
 */
public record AutorAutenticado(UUID id, Perfil perfil) {

    public static AutorAutenticado de(Jwt jwt) {
        return new AutorAutenticado(UUID.fromString(jwt.getSubject()), Perfil.valueOf(jwt.getClaimAsString("perfil")));
    }

    public boolean ehCliente() {
        return perfil == Perfil.CLIENTE;
    }

    public boolean ehAdmin() {
        return perfil == Perfil.ADMIN;
    }

    /** TECNICO ou ADMIN (o ADMIN faz tudo do técnico: SDD seção 3). */
    public boolean daEquipe() {
        return perfil == Perfil.TECNICO || perfil == Perfil.ADMIN;
    }
}

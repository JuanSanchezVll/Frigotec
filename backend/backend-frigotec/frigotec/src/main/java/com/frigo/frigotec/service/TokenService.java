package com.frigo.frigotec.service;

import com.frigo.frigotec.entity.Perfil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/** Gera o token JWT do login: id do usuário (subject), perfil e validade, assinado com o segredo do servidor. */
@Service
public class TokenService {

    public record Token(String valor, long expiraEmSegundos) {}

    private final JwtEncoder encoder;
    private final long minutos;

    public TokenService(JwtEncoder encoder, @Value("${app.jwt.expiracao-minutos:120}") long minutos) {
        this.encoder = encoder;
        this.minutos = minutos;
    }

    public Token gerar(UUID usuarioId, Perfil perfil) {
        Instant agora = Instant.now();
        JwtClaimsSet dados = JwtClaimsSet.builder()
                .issuer("frigotec")
                .subject(usuarioId.toString())
                .issuedAt(agora)
                .expiresAt(agora.plus(minutos, ChronoUnit.MINUTES))
                .claim("perfil", perfil.name())
                .build();
        String valor = encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), dados)).getTokenValue();
        return new Token(valor, minutos * 60);
    }
}

package com.frigo.frigotec.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Segurança da API (SDD RN-USR-05 e RN-USR-06): senha com BCrypt, login que devolve um token JWT
 * e todas as demais rotas exigindo esse token no cabeçalho Authorization: Bearer.
 */
@Configuration
@EnableWebSecurity
public class SegurancaConfig {

    /** Segredo de DESENVOLVIMENTO. Fora do ambiente "dev" a aplicação se recusa a subir com ele. */
    public static final String SEGREDO_DEV = "frigotec-segredo-de-desenvolvimento-nao-usar-em-producao";

    private final SecretKey chave;

    public SegurancaConfig(@Value("${app.jwt.secret}") String segredo,
                           @Value("${app.ambiente:dev}") String ambiente) {
        if (segredo.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("app.jwt.secret precisa ter pelo menos 32 caracteres (defina JWT_SECRET).");
        }
        if (!"dev".equals(ambiente) && SEGREDO_DEV.equals(segredo)) {
            throw new IllegalStateException("Defina a variável de ambiente JWT_SECRET: o segredo de desenvolvimento não vale fora do ambiente dev.");
        }
        this.chave = new SecretKeySpec(segredo.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(chave));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(chave).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /** O claim "perfil" do token vira o papel (ROLE_ADMIN, ROLE_TECNICO, ROLE_CLIENTE). */
    private JwtAuthenticationConverter conversor() {
        JwtAuthenticationConverter conversor = new JwtAuthenticationConverter();
        conversor.setJwtGrantedAuthoritiesConverter(jwt ->
                List.of(new SimpleGrantedAuthority("ROLE_" + jwt.getClaimAsString("perfil"))));
        return conversor;
    }

    @Bean
    public SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // API sem sessão e sem cookie: não há o que proteger com CSRF
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h -> h.frameOptions(f -> f.sameOrigin())) // o console do H2 usa frames
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.POST, "/usuarios", "/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/fabricantes").permitAll()
                        .requestMatchers("/h2-console/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**", "/v3/api-docs.yaml", "/error").permitAll()
                        .requestMatchers("/usuarios/colaboradores/**").hasRole("ADMIN")
                        // Catálogo: só a equipe cria marcas e modelos (SDD seção 3)
                        .requestMatchers(HttpMethod.POST, "/fabricantes", "/equipamentos").hasAnyRole("TECNICO", "ADMIN")
                        // Equipamento do cliente e abertura/resposta: só o cliente
                        .requestMatchers(HttpMethod.POST, "/equipamentos/*", "/chamados", "/chamados/*/anexos", "/equipamentos/*/documentos").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.PATCH, "/equipamentos/*").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/equipamentos/*").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.PATCH, "/orcamentos/*/resposta").hasRole("CLIENTE")
                        // Fila, atendimento, visita, encerramento, orçamento e garantia: equipe Frigotec
                        .requestMatchers(HttpMethod.GET, "/chamados", "/chamados/status/*", "/chamados/tecnicos/*", "/dashboard/tecnico").hasAnyRole("TECNICO", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/chamados/*/atribuicao", "/chamados/*/encerramento", "/orcamentos").hasAnyRole("TECNICO", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/chamados/*/visita", "/garantias/*/validacao").hasAnyRole("TECNICO", "ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(o -> o
                        .jwt(j -> j.jwtAuthenticationConverter(conversor()))
                        .authenticationEntryPoint((req, res, ex) -> erro(res, 401, "Faça login para continuar."))
                        .accessDeniedHandler((req, res, ex) -> erro(res, 403, "Seu perfil não tem permissão para esta ação.")));
        return http.build();
    }

    /** Erro no mesmo formato do GlobalExceptionHandler (SDD seção 9). */
    private static void erro(HttpServletResponse res, int status, String mensagem) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"status\":" + status + ",\"mensagem\":\"" + mensagem + "\",\"campos\":[]}");
    }
}

package com.frigo.frigotec.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Documentação da API: Swagger UI em /swagger-ui.html e especificação OpenAPI em /v3/api-docs (JSON)
 * e /v3/api-docs.yaml (YAML). As três rotas são públicas (SegurancaConfig).
 * O esquema "bearerAuth" liga o botão Authorize do Swagger UI ao token JWT do login.
 */
@Configuration
public class OpenApiConfig {

    private static final String ESQUEMA_JWT = "bearerAuth";

    @Bean
    public OpenAPI frigotecOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Portal Frigotec — API")
                        .version("1.0.0")
                        .description("""
                                API do portal de pós-venda, garantias e assistência técnica da Frigotec.
                                O cliente cadastra equipamentos e abre chamados; o técnico atende, analisa a garantia, \
                                agenda a visita, gera orçamento e encerra; o administrador cadastra a equipe.

                                Para testar as rotas protegidas: faça POST /usuarios/login, copie o valor de "token" \
                                da resposta e informe-o no botão Authorize.""")
                        .contact(new Contact().name("Grupo 5 — Portal Frigotec")))
                .components(new Components().addSecuritySchemes(ESQUEMA_JWT, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_JWT));
    }
}

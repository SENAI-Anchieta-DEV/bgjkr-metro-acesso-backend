package com.senai.bgjkr_metro_acesso_backend.infrastructure.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.*;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI metroAcessoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - MetroAcesso")
                        .description("Integração da comunicação dos agentes de atendimento para suporte ao PcD.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Equipe MetroAcesso")
                                .email("suporte@metroacesso.com")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}

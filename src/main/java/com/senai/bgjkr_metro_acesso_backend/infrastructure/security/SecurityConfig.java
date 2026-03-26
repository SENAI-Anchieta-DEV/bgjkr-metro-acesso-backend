package com.senai.bgjkr_metro_acesso_backend.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/**").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/agente/**").hasAnyRole("ADMINISTRADOR", "AGENTE_ATENDIMENTO")
                        .requestMatchers(HttpMethod.POST, "/api/agente/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/agente/**").hasAnyRole("ADMINISTRADOR", "AGENTE_ATENDIMENTO")
                        .requestMatchers(HttpMethod.DELETE, "/api/agente/**").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/pcd/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR", "AGENTE_ATENDIMENTO")
                        .requestMatchers(HttpMethod.POST, "/api/pcd/**").hasRole( "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/pcd/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/pcd/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/formulario/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/formulario/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/formulario/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/formulario/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/formulario/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/formulario/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/formulario/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/formulario/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/estacao/**").hasAnyRole( "ADMINISTRADOR", "AGENTE_ATENDIMENTO")
                        .requestMatchers(HttpMethod.POST, "/api/estacao/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/estacao/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/estacao/**").hasRole("ADMINISTRADOR")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

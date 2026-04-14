package com.senai.bgjkr_metro_acesso_backend.infrastructure.security;

import com.senai.bgjkr_metro_acesso_backend.infrastructure.config.CorsProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(CorsProperties.class)
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsProperties corsProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/",
                                "/error",
                                "/favicon.ico",
                                "/actuator/health/**",
                                "/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/**").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/agente/**").hasAnyRole("ADMINISTRADOR", "AGENTE_ATENDIMENTO")
                        .requestMatchers(HttpMethod.POST, "/api/agente/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/agente/**").hasAnyRole("ADMINISTRADOR", "AGENTE_ATENDIMENTO")
                        .requestMatchers(HttpMethod.DELETE, "/api/agente/**").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/pcd/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR", "AGENTE_ATENDIMENTO")
                        .requestMatchers(HttpMethod.POST, "/api/pcd/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/pcd/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/pcd/**").hasAnyRole("USUARIO_PCD", "ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/formulario/**").hasAnyRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/formulario/**").hasAnyRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/formulario/**").hasAnyRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/formulario/**").hasAnyRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/estacao/**").hasAnyRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/estacao/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/estacao/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/estacao/**").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/sensor/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/sensor/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/sensor/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/sensor/**").hasRole("ADMINISTRADOR")

                        .requestMatchers(HttpMethod.GET, "/api/tag/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/tag/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/tag/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/tag/**").hasRole("ADMINISTRADOR")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (!response.isCommitted()) {
                                System.out.println("authenticationEntryPoint: " + authException.getMessage() + " | URL: " + request.getRequestURI());
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                            }
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("accessDeniedHandler: " + accessDeniedException.getMessage() + " | URL: " + request.getRequestURI());
                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(corsProperties.getAllowedOrigins()); // Porta do seu React
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

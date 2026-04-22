package com.senai.bgjkr_metro_acesso_backend.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {
    private List<String> allowedOrigins = new ArrayList<>(List.of(
            "http://localhost:5173",
            "https://metro-acesso-frontend.firebaseapp.com",
            "https://metro-acesso-frontend.web.app"
    ));
}
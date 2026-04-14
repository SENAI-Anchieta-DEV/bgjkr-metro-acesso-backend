package com.senai.bgjkr_metro_acesso_backend.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

import static com.google.api.AnnotationsProto.http;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {
    private List<String> allowedOrigins = new ArrayList<>(List.of(
            "http://localhost:5173",
            "https://metro-acesso-frontend.firebaseapp.com",
            "https://metro-acesso-frontend.web.app",
            "http://localhost:8080/h2-console/login.do?jsessionid=66c79f770b3c82c6e232314d8c35818c"
    ));
}
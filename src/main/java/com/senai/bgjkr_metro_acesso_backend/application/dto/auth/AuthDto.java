package com.senai.bgjkr_metro_acesso_backend.application.dto.auth;

public class AuthDto {
    public record LoginRequest(
            String email,
            String senha
    ) {
    }

    public record TokenResponse(
            String token
    ) {
    }
}

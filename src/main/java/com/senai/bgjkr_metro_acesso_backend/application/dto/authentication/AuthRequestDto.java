package com.senai.bgjkr_metro_acesso_backend.application.dto.authentication;

public record AuthRequestDto(
        String email,
        String senha
) {
}

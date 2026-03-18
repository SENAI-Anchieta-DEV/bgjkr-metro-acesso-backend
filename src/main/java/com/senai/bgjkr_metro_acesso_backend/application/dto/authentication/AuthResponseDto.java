package com.senai.bgjkr_metro_acesso_backend.application.dto.authentication;

import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;

public record AuthResponseDto(
        String nome,
        String email,
        Role role,
        String token
) {
}
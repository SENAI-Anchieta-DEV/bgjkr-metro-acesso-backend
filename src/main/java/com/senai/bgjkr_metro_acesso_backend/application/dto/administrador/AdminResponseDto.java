package com.senai.bgjkr_metro_acesso_backend.application.dto.administrador;

import java.util.UUID;

public record AdminResponseDto(
        UUID id,
        String nome,
        String email
) {
}

package com.senai.bgjkr_metro_acesso_backend.application.dto.administrador;

public record AdminRequestDto(
        String nome,
        String email,
        String senha
) {
}

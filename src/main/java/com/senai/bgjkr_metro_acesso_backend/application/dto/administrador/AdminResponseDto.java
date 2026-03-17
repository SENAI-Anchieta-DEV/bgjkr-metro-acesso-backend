package com.senai.bgjkr_metro_acesso_backend.application.dto.administrador;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;

public record AdminResponseDto(
        String id,
        String nome,
        String email
) {
    public static AdminResponseDto fromEntity(
            Administrador administrador
    ) {
        return new AdminResponseDto(
                administrador.getId(),
                administrador.getNome(),
                administrador.getEmail()
        );
    }
}

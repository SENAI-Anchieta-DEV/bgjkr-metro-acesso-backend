package com.senai.bgjkr_metro_acesso_backend.application.dto.administrador;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;

public record AdminRequestDto(
        String nome,
        String email,
        String senha
) {
    public Administrador toEntity() {
        return Administrador.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .ativo(true)
                .role(Role.ADMINISTRADOR)
                .build();
    }
}

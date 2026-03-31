package com.senai.bgjkr_metro_acesso_backend.application.dto.administrador;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminRequestDto(
        @NotBlank
        @Size(max = 200)
        String nome,

        @NotBlank
        @Email
        @Size(max = 200)
        String email,

        @NotBlank
        @Size(min = 8, max = 200)
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

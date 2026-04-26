package com.senai.bgjkr_metro_acesso_backend.application.dto.administrador;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de transferência de dados para requisições de Administrador (Criação e Atualização).")
public record AdminRequestDto(
        @Schema(description = "Nome completo do administrador.", example = "Maria Silva")
        @NotBlank
        @Size(max = 200)
        String nome,

        @Schema(description = "Endereço de e-mail corporativo (deve ser único).", example = "maria.admin@metroacesso.com")
        @NotBlank
        @Email
        @Size(max = 200)
        String email,

        @Schema(description = "Senha de acesso ao sistema (mínimo de 8 caracteres).", example = "SenhaForte123!")
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
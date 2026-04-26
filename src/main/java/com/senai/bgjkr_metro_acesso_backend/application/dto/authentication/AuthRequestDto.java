package com.senai.bgjkr_metro_acesso_backend.application.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Credenciais do utilizador para efetuar o login na plataforma.")
public record AuthRequestDto(
        @Schema(description = "E-mail registado na conta do utilizador.", example = "usuario@metroacesso.com")
        @NotBlank
        @Email
        @Size(max = 200)
        String email,

        @Schema(description = "Senha correspondente à conta.", example = "MinhaSenhaSecreta")
        @NotBlank
        @Size(min = 8, max = 200)
        String senha
) {
}
package com.senai.bgjkr_metro_acesso_backend.application.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequestDto(
        @NotBlank
        @Email
        @Size(max = 200)
        String email,

        @NotBlank
        @Size(min = 8, max = 200)
        String senha
) {
}

package com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IdentificacaoDto(
        @NotBlank
        @Size(max = 200)
        String codigotTag,

        @NotBlank
        @Size(max = 200)
        String codigoEntrada,

        @NotBlank
        @Size(max = 200)
        String codigoEstacao,

        @Min(0)
        @Max(9999999999L) // ano ~2286
        long timestamp
) {
}

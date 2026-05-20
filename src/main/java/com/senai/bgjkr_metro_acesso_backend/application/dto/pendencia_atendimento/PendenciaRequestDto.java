package com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PendenciaRequestDto(
        @NotBlank
        @Size(max = 200)
        String codigoTag,

        @NotBlank
        @Size(max = 200)
        String codigoEstacao,

        @NotBlank
        @Size(max = 200)
        String codigoEntrada,

        boolean tipo,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        String dataHora
) {
}

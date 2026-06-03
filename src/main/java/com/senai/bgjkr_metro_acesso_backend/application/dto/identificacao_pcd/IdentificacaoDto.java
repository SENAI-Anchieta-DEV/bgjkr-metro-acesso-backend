package com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IdentificacaoDto(
        @NotBlank
        @Size(max = 200)
        String codigoTag,

        @NotBlank
        @Size(max = 200)
        String bssid
) {
}

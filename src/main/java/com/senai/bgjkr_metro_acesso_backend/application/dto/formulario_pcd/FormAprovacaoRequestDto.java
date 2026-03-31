package com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FormAprovacaoRequestDto(
        @NotNull
        boolean aprovado,

        @Size(max = 500)
        String motivoReprovacao
) {
}

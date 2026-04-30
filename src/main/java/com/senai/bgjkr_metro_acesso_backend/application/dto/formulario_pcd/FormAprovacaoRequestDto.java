package com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload utilizado por um administrador para determinar o resultado da avaliação de um formulário PCD.")
public record FormAprovacaoRequestDto(
        @Schema(description = "Define se a solicitação foi aprovada (true) ou rejeitada (false).", example = "true")
        @NotNull
        boolean aprovado,

        @Schema(description = "Justificação obrigatória caso o formulário seja rejeitado.", example = "Documento anexado ilegível ou inválido.")
        @Size(max = 500)
        String motivoReprovacao
) {
}
package com.senai.bgjkr_metro_acesso_backend.application.dto.entrada;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EntradaRequestDto(
        @NotBlank
        @Size(max = 200)
        String codigoEstacao,

        @NotBlank
        @Size(max = 200)
        String codigoEntrada
) {
    public Entrada toEntity(
            Estacao estacao

    ) {
        return Entrada.builder()
                .codigoEntrada(codigoEntrada)
                .ativo(true)
                .estacao(estacao)
                .build();
    }
}

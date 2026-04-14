package com.senai.bgjkr_metro_acesso_backend.application.dto.entrada;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;

public record EntradaSummaryDto(
        String id,
        String codigoEntrada
) {
    public static EntradaSummaryDto fromEntity(
            Entrada entrada
    ) {
        return new EntradaSummaryDto(
                entrada.getId(),
                entrada.getCodigoEntrada()
        );
    }
}

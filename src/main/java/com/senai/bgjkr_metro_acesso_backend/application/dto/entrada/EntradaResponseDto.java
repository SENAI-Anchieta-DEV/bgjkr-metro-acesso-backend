package com.senai.bgjkr_metro_acesso_backend.application.dto.entrada;

import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;

public record EntradaResponseDto(
        String id,
        String codigoEntrada,
        EstacaoSummaryDto estacao
) {
    public static EntradaResponseDto fromEntity(
            Entrada entrada
    ) {
        return new EntradaResponseDto(
                entrada.getId(),
                entrada.getCodigoEntrada(),
                entrada.getEstacao() == null ?
                        null :
                        EstacaoSummaryDto.fromEntity(entrada.getEstacao())
        );
    }
}

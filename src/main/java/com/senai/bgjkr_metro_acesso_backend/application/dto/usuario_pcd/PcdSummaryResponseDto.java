package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;

import java.util.UUID;

public record PcdSummaryResponseDto(
        UUID id,
        String nome,
        String email,
        TipoDeficiencia tipoDeficiencia,
        Boolean desejaSuporte
) {
}
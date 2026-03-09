package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagSummaryResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;

import java.util.UUID;

public record PcdResponseDto(
        UUID id,
        String nome,
        String email,
        TipoDeficiencia tipoDeficiencia,
        Boolean desejaSuporte,
        TagSummaryResponseDto tag
) {
}
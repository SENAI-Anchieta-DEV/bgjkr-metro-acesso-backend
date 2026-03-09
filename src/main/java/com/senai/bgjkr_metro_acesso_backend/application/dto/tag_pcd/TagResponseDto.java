package com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd;

import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdSummaryResponseDto;

import java.util.UUID;

public record TagResponseDto(
        UUID id,
        String codigoTag,
        PcdSummaryResponseDto usuarioPcd
) {
}

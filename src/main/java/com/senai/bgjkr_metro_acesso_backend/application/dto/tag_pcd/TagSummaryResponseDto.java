package com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd;

import java.util.UUID;

public record TagSummaryResponseDto(
        UUID id,
        String codigoTag
) {
}

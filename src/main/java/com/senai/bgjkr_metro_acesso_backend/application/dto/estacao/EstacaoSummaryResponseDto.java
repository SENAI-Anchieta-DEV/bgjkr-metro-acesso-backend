package com.senai.bgjkr_metro_acesso_backend.application.dto.estacao;

import java.util.UUID;

public record EstacaoSummaryResponseDto(
        UUID id,
        String nome
) {
}

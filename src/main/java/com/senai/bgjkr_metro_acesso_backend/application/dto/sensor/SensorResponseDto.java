package com.senai.bgjkr_metro_acesso_backend.application.dto.sensor;

import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoSummaryResponseDto;

import java.util.UUID;

public record SensorResponseDto(
        UUID id,
        EstacaoSummaryResponseDto estacao
) {
}

package com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento;

import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoSummaryResponseDto;

import java.time.LocalTime;
import java.util.UUID;

public record AgenteResponseDto(
        UUID id,
        String nome,
        String email,
        LocalTime inicioTurno,
        LocalTime fimTurno,
        EstacaoSummaryResponseDto estacao
) {
}

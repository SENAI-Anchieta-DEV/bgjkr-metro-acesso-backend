package com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento;

import java.time.LocalTime;
import java.util.UUID;

public record AgenteSummaryResponseDto(
        UUID id,
        String nome,
        String email,
        LocalTime inicioTurno,
        LocalTime fimTurno
) {
}

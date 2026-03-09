package com.senai.bgjkr_metro_acesso_backend.application.dto.estacao;

import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteSummaryResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.sensor.SensorSummaryResponseDto;

import java.util.List;
import java.util.UUID;

public record EstacaoResponseDto(
        UUID id,
        String nome,
        List<AgenteSummaryResponseDto> agentes,
        List<SensorSummaryResponseDto> sensores
) {
}

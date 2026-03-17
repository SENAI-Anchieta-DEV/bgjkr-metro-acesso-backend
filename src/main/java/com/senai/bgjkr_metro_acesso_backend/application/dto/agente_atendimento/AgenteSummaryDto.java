package com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;

import java.time.LocalTime;

public record AgenteSummaryDto(
        String id,
        String nome,
        String email,
        LocalTime inicioTurno,
        LocalTime fimTurno
) {
    public static AgenteSummaryDto fromEntity(
            AgenteAtendimento agente
    ) {
        return new AgenteSummaryDto(
                agente.getId(),
                agente.getNome(),
                agente.getEmail(),
                agente.getInicioTurno(),
                agente.getFimTurno()
        );
    }
}

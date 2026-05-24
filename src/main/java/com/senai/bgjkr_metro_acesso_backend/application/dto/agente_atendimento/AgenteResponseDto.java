package com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento;

import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;

import java.time.LocalTime;
import java.util.List;

public record AgenteResponseDto(
        String id,
        String nome,
        String email,
        LocalTime inicioTurno,
        LocalTime fimTurno,
        EstacaoSummaryDto estacao,
        List<PendenciaResponseDto> pendencias
) {
    public static AgenteResponseDto fromEntity(
            AgenteAtendimento agente
    ) {
        return new AgenteResponseDto(
                agente.getId(),
                agente.getNome(),
                agente.getEmail(),
                agente.getInicioTurno(),
                agente.getFimTurno(),
                agente.getEstacao() == null ?
                        null :
                        EstacaoSummaryDto.fromEntity(agente.getEstacao()),
                agente.getPendencias() == null ?
                        null :
                        agente.getPendencias().stream()
                                .map(PendenciaResponseDto::fromEntity)
                                .toList()
        );
    }
}
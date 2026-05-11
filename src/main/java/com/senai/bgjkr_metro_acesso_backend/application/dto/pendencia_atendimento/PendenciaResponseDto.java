package com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento;

import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.entrada.EntradaSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.PendenciaAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusAtendimento;

import java.time.LocalDateTime;

public record PendenciaResponseDto(
        String id,
        PcdSummaryDto pcdAtendido,
        AgenteSummaryDto agente,
        EstacaoSummaryDto estacao,
        EntradaSummaryDto entrada,
        LocalDateTime dataHora,
        StatusAtendimento statusAtendimento
) {
    public static PendenciaResponseDto fromEntity(
            PendenciaAtendimento pendencia
    ) {
        return new PendenciaResponseDto(
                pendencia.getId(),
                PcdSummaryDto.fromEntity(pendencia.getPcdAtendido()),
                AgenteSummaryDto.fromEntity(pendencia.getAgente()),
                EstacaoSummaryDto.fromEntity(pendencia.getEstacao()),
                EntradaSummaryDto.fromEntity(pendencia.getEntrada()),
                pendencia.getDataHora(),
                pendencia.getStatusAtendimento()
        );
    }
}

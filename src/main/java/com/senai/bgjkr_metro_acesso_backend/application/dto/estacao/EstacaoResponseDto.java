package com.senai.bgjkr_metro_acesso_backend.application.dto.estacao;

import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.entrada.EntradaSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Linha;

import java.util.List;
import java.util.Set;

public record EstacaoResponseDto(
        String id,
        String nome,
        String codigoEstacao,
        Set<Linha> linhas,
        List<AgenteSummaryDto> agentes,
        List<EntradaSummaryDto> entradas
) {
    public static EstacaoResponseDto fromEntity(
            Estacao estacao
    ) {
        return new EstacaoResponseDto(
                estacao.getId(),
                estacao.getNome(),
                estacao.getCodigoEstacao(),
                estacao.getLinhas(),
                estacao.getAgentes()
                        .stream()
                        .map(AgenteSummaryDto::fromEntity)
                        .toList(),
                estacao.getEntradas()
                        .stream()
                        .map(EntradaSummaryDto::fromEntity)
                        .toList()
        );
    }
}

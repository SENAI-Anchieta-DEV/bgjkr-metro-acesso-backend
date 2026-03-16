package com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;

import java.time.LocalTime;

public record AgenteRequestDto(
        String nome,
        String email,
        String senha,
        LocalTime inicioTurno,
        LocalTime fimTurno,
        String estacaoId
) {
    public AgenteAtendimento toEntity(
            Estacao estacao
    ) {
        return AgenteAtendimento.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .ativo(true)
                .role(Role.AGENTE_ATENDIMENTO)
                .estacao(estacao)
                .inicioTurno(inicioTurno)
                .fimTurno(fimTurno)
                .build();
    }
}

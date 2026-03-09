package com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento;

import java.time.LocalTime;
import java.util.UUID;

public record AgenteRequestDto(
        String nome,
        String email,
        String senha,
        LocalTime inicioTurno,
        LocalTime fimTurno,
        UUID estacaoId
) {
}

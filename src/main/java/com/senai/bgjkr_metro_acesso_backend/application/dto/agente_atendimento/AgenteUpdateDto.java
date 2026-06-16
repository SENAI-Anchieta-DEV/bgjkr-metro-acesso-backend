package com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;

@Schema(description = "Objeto de transferência de dados para o registo e atualização de Agentes de Atendimento.")
public record AgenteUpdateDto(
        String nome,
        String email,
        String senha,
        LocalTime inicioTurno,
        LocalTime fimTurno,
        String codigoEstacao
) {
}
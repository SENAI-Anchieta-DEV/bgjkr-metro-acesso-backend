package com.senai.bgjkr_metro_acesso_backend.application.dto.entrada;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de registo e localização de Entradas.")
public record EntradaUpdateDto(
        String codigoEstacao,
        String codigoEntrada
) {
}
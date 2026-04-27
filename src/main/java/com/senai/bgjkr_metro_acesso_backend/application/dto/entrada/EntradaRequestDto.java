package com.senai.bgjkr_metro_acesso_backend.application.dto.entrada;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Sensor;
import io.swagger.v3.oas.annotations.media.Schema;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados de registo e localização de Entradas.")
public record EntradaRequestDto(
        @Schema(description = "Código da estação à qual o Entrada está fixado.", example = "EST-SE-01")
        @NotBlank
        @Size(max = 200)
        String codigoEstacao,

        @Schema(description = "Identificação única da entrada.", example = "ENTRADA-100X")
        @NotBlank
        @Size(max = 200)
        String codigoSensor
) {
    public Entrada toEntity(
            Estacao estacao
    ) {
        return Entrada.builder()
                .codigoEntrada(codigoEntrada)
                .ativo(true)
                .estacao(estacao)
                .build();
    }
}

package com.senai.bgjkr_metro_acesso_backend.application.dto.sensor;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Sensor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SensorRequestDto(
        @NotBlank
        @Size(max = 200)
        String codigoEstacao,

        @NotBlank
        @Size(max = 200)
        String porta,

        @NotBlank
        @Size(max = 200)
        String codigoSensor
) {
    public Sensor toEntity(
            Estacao estacao

    ) {
        return Sensor.builder()
                .porta(porta)
                .codigoSensor(codigoSensor)
                .ativo(true)
                .estacao(estacao)
                .build();
    }
}

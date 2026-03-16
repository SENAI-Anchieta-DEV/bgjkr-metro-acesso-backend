package com.senai.bgjkr_metro_acesso_backend.application.dto.sensor;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Sensor;

public record SensorRequestDto(
        String estacaoId,
        String porta,
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

package com.senai.bgjkr_metro_acesso_backend.application.dto.sensor;

import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Sensor;

public record SensorResponseDto(
        String id,
        String porta,
        String codigoSensor,
        EstacaoSummaryDto estacao
) {
    public static SensorResponseDto fromEntity(
            Sensor sensor
    ) {
        return new SensorResponseDto(
                sensor.getId(),
                sensor.getPorta(),
                sensor.getCodigoSensor(),
                sensor.getEstacao() == null ?
                        null :
                        EstacaoSummaryDto.fromEntity(sensor.getEstacao())
        );
    }
}

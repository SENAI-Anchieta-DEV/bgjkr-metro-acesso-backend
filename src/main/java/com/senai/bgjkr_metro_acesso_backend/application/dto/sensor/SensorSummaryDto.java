package com.senai.bgjkr_metro_acesso_backend.application.dto.sensor;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Sensor;

public record SensorSummaryDto(
        String id,
        String porta,
        String codigoSensor
) {
    public static SensorSummaryDto fromEntity(
            Sensor sensor
    ) {
        return new SensorSummaryDto(
                sensor.getId(),
                sensor.getPorta(),
                sensor.getCodigoSensor()
        );
    }
}

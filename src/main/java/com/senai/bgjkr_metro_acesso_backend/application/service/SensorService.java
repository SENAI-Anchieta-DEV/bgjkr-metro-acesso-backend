package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.sensor.SensorRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.sensor.SensorResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Sensor;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {
    private final SensorRepository repository;
    private final EstacaoService estacaoService;

    // CREATE
    @Transactional
    public SensorResponseDto registrarSensor(SensorRequestDto requestDto) {
        Sensor sensorRegistrado = repository.findByCodigoSensor(requestDto.codigoSensor())
                .map(sensor -> sensor.isAtivo() ? sensor : reativarSensor(sensor, requestDto))
                .orElseGet(() -> criarSensor(requestDto));

        return SensorResponseDto.fromEntity(repository.save(sensorRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    public List<SensorResponseDto> listarSensorsAtivos() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(SensorResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public SensorResponseDto buscarSensorAtivo(String codigoSensor) {
        return SensorResponseDto.fromEntity(procurarSensorAtivo(codigoSensor));
    }

    // UPDATE
    @Transactional
    public SensorResponseDto atualizarSensor(String codigoSensor, SensorRequestDto requestDto) {
        Sensor sensorAtualizado = procurarSensorAtivo(codigoSensor);

        atualizarValores(sensorAtualizado, requestDto);

        return SensorResponseDto.fromEntity(repository.save(sensorAtualizado));
    }

    // DELETE
    @Transactional
    public void removerSensor(String codigoSensor) {
        Sensor sensorRemovido = procurarSensorAtivo(codigoSensor);

        sensorRemovido.setAtivo(false);

        repository.save(sensorRemovido);
    }


package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.sensor.SensorRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.sensor.SensorResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sensor")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService service;

    // CREATE
    @PostMapping
    public ResponseEntity<SensorResponseDto> registrarSensor(@RequestBody @Valid SensorRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/sensor"))
                .body(service.registrarSensor(requestDto));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<SensorResponseDto>> listarSensorsAtivos() {
        return ResponseEntity
                .ok(service.listarSensorsAtivos());
    }

    @GetMapping("/{codigoSensor}")
    public ResponseEntity<SensorResponseDto> buscarSensorAtivo(@PathVariable String codigoSensor) {
        return ResponseEntity
                .ok(service.buscarSensorAtivo(codigoSensor));
    }

    // UPDATE
    @PutMapping("/{codigoSensor}")
    public ResponseEntity<SensorResponseDto> atualizarSensor(@PathVariable String codigoSensor, @RequestBody @Valid SensorRequestDto requestDto) {
        return ResponseEntity
                .ok(service.atualizarSensor(codigoSensor, requestDto));
    }

    // DELETE
    @DeleteMapping("/{codigoSensor}")
    public ResponseEntity<Void> removerSensor(@PathVariable String codigoSensor) {
        service.removerSensor(codigoSensor);
        return ResponseEntity
                .noContent()
                .build();
    }
}


package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.sensor.SensorRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.sensor.SensorResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sensor")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService service;

    // CREATE
    @PostMapping
    public SensorResponseDto registrarSensor(@RequestBody SensorRequestDto requestDto) {
        return service.registrarSensor(requestDto);
    }

    // READ
    @GetMapping
    public List<SensorResponseDto> listarSensorsAtivos() {
        return service.listarSensorsAtivos();
    }

    @GetMapping("/{codigoSensor}")
    public SensorResponseDto buscarSensorAtivo(@PathVariable String codigoSensor) {
        return service.buscarSensorAtivo(codigoSensor);
    }

    // UPDATE
    @PutMapping("/{codigoSensor}")
    public SensorResponseDto atualizarSensor(@PathVariable String codigoSensor, @RequestBody SensorRequestDto requestDto) {
        return service.atualizarSensor(codigoSensor, requestDto);
    }


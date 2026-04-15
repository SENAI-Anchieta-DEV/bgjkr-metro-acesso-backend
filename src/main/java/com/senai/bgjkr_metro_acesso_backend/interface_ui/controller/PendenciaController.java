package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.PendenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pendencia-atendimento")
@RequiredArgsConstructor
public class PendenciaController {
    private final PendenciaService service;

    @GetMapping
    public ResponseEntity<List<PendenciaResponseDto>> listarPendenciasAtivas() {
        return ResponseEntity
                .ok(service.listarPendenciasAtivas());
    }

    @GetMapping("/agente/{email}")
    public ResponseEntity<List<PendenciaResponseDto>> listarPendenciasDoAgente(@PathVariable String email) {
        return ResponseEntity
                .ok(service.listarPendenciasDoAgente(email));
    }

    @GetMapping("/estacao/{codigoEstacao}")
    public ResponseEntity<List<PendenciaResponseDto>> listarPendenciasPorEstacao(@PathVariable String codigoEstacao) {
        return ResponseEntity
                .ok(service.listarPendenciasDoAgente(codigoEstacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPendencia(@PathVariable String id) {
        service.removerPendencia(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}


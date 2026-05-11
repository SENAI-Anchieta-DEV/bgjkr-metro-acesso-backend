package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.entrada.EntradaRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.entrada.EntradaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.EntradaService;
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
@RequestMapping("/api/entrada")
@RequiredArgsConstructor
public class EntradaController {
    private final EntradaService service;

    // CREATE
    @PostMapping
    public ResponseEntity<EntradaResponseDto> registrarEntrada(@RequestBody @Valid EntradaRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/entrada"))
                .body(service.registrarEntrada(requestDto));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<EntradaResponseDto>> listarEntradasAtivas() {
        return ResponseEntity
                .ok(service.listarEntradasAtivas());
    }

    @GetMapping("/{codigoEntrada}")
    public ResponseEntity<EntradaResponseDto> buscarEntradaAtiva(@PathVariable String codigoEntrada) {
        return ResponseEntity
                .ok(service.buscarEntradaAtiva(codigoEntrada));
    }

    // UPDATE
    @PutMapping("/{codigoEntrada}")
    public ResponseEntity<EntradaResponseDto> atualizarEntrada(@PathVariable String codigoEntrada, @RequestBody @Valid EntradaRequestDto requestDto) {
        return ResponseEntity
                .ok(service.atualizarEntrada(codigoEntrada, requestDto));
    }

    // DELETE
    @DeleteMapping("/{codigoEntrada}")
    public ResponseEntity<Void> removerEntrada(@PathVariable String codigoEntrada) {
        service.removerEntrada(codigoEntrada);
        return ResponseEntity
                .noContent()
                .build();
    }
}


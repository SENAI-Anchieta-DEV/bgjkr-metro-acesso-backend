package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AgenteService;
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
@RequestMapping("/api/agente")
@RequiredArgsConstructor
public class AgenteController {
    private final AgenteService service;

    // CREATE
    @PostMapping
    public ResponseEntity<AgenteResponseDto> registrarAgente(@RequestBody @Valid AgenteRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/agente"))
                .body(service.registrarAgente(requestDto));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<AgenteResponseDto>> listarAgentesAtivos() {
        return ResponseEntity
                .ok(service.listarAgentesAtivos());
    }

    @GetMapping("/{email}")
    public ResponseEntity<AgenteResponseDto> buscarAgenteAtivo(@PathVariable String email) {
        return ResponseEntity
                .ok(service.buscarAgenteAtivo(email));
    }

    // UPDATE
    @PutMapping("/{email}")
    public ResponseEntity<AgenteResponseDto> atualizarAgente(@PathVariable String email, @RequestBody @Valid AgenteRequestDto requestDto) {
        return ResponseEntity
                .ok(service.atualizarAgente(email, requestDto));
    }

    // DELETE
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> removerAgente(@PathVariable String email) {
        service.removerAgente(email);
        return ResponseEntity
                .noContent()
                .build();
    }
}

package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.EstacaoService;
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
@RequestMapping("/api/estacao")
@RequiredArgsConstructor
public class EstacaoController {
    private final EstacaoService service;

    // CREATE
    @PostMapping
    public ResponseEntity<EstacaoResponseDto> registrarEstacao(@RequestBody EstacaoRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/estacao"))
                .body(service.registrarEstacao(requestDto));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<EstacaoResponseDto>> listarEstacoesAtivas() {
        return ResponseEntity
                .ok(service.listarEstacoesAtivas());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<EstacaoResponseDto> buscarEstacaoAtiva(@PathVariable String codigo) {
        return ResponseEntity
                .ok(service.buscarEstacaoAtiva(codigo));
    }

    // UPDATE
    @PutMapping("/{codigo}")
    public ResponseEntity<EstacaoResponseDto> atualizarEstacao(@PathVariable String codigo, @RequestBody EstacaoRequestDto requestDto) {
        return ResponseEntity
                .ok(service.atualizarEstacao(codigo, requestDto));
    }

    // DELETE
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> removerEstacao(@PathVariable String codigo) {
        service.removerEstacao(codigo);
        return ResponseEntity
                .noContent()
                .build();
    }
}

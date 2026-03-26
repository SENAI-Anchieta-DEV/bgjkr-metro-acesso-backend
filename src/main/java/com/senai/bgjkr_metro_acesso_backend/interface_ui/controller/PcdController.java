package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.PcdService;
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
@RequestMapping("/api/pcd")
@RequiredArgsConstructor
public class PcdController {
    private final PcdService service;

    // CREATE
    @PostMapping
    public ResponseEntity<PcdResponseDto> registrarPcd(@RequestBody PcdRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/pcd"))
                .body(service.registrarPcd(requestDto));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<PcdResponseDto>> listarPcdsAtivos() {
        return ResponseEntity
                .ok(service.listarPcdsAtivos());
    }

    @GetMapping("/{email}")
    public ResponseEntity<PcdResponseDto> buscarPcdAtivo(@PathVariable String email) {
        return ResponseEntity
                .ok(service.buscarPcdAtivo(email));
    }

    // UPDATE
    @PutMapping("/{email}")
    public ResponseEntity<PcdResponseDto> atualizarPcd(@PathVariable String email, @RequestBody PcdRequestDto requestDto) {
        return ResponseEntity
                .ok(service.atualizarPcd(email, requestDto));
    }

    // DELETE
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> removerPcd(@PathVariable String email) {
        service.removerPcd(email);
        return ResponseEntity
                .noContent()
                .build();

    }
}


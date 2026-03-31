package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AdminService;
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
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

    // CREATE
    @PostMapping
    public ResponseEntity<AdminResponseDto> registrarAdmin(@RequestBody @Valid AdminRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/admin"))
                .body(service.registrarAdmin(requestDto));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<AdminResponseDto>> listarAdminsAtivos() {
        return ResponseEntity
                .ok(service.listarAdminsAtivos());
    }

    @GetMapping("/{email}")
    public ResponseEntity<AdminResponseDto> buscarAdminAtivo(@PathVariable String email) {
        return ResponseEntity
        .ok(service.buscarAdminAtivo(email));
    }

    // UPDATE
    @PutMapping("/{email}")
    public ResponseEntity<AdminResponseDto> atualizarAdmin(@PathVariable String email, @RequestBody @Valid AdminRequestDto requestDto) {
        return ResponseEntity
        .ok(service.atualizarAdmin(email, requestDto));
    }

    // DELETE
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> removerAdmin(@PathVariable String email) {
        service.removerAdmin(email);
        return ResponseEntity
                .noContent()
                .build();

    }
}
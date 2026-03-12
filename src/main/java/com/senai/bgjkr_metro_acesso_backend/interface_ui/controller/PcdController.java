package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.PcdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pcd")
@RequiredArgsConstructor
public class PcdController {
    private final PcdService service;

    // CREATE
    @PostMapping
    public PcdResponseDto registrarPcd(@RequestBody PcdRequestDto requestDto) {
        return service.registrarPcd(requestDto);
    }

    // READ
    @GetMapping
    public List<PcdResponseDto> listarPcdsAtivos() {
        return service.listarPcdsAtivos();
    }

    @GetMapping("/{email}")
    public PcdResponseDto buscarPcdAtivo(@PathVariable String email) {
        return service.buscarPcdAtivo(email);
    }

    // UPDATE
    @PutMapping("/{email}")
    public PcdResponseDto atualizarPcd(@PathVariable String email, @RequestBody PcdRequestDto requestDto) {
        return service.atualizarPcd(email, requestDto);
    }



}


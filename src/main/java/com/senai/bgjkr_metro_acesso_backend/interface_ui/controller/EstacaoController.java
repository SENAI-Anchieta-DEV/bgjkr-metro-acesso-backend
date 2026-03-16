package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.EstacaoService;
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
@RequestMapping("/api/estacao")
@RequiredArgsConstructor
public class EstacaoController {
    private final EstacaoService service;

    // CREATE
    @PostMapping
    public EstacaoResponseDto registrarEstacao(@RequestBody EstacaoRequestDto requestDto) {
        return service.registrarEstacao(requestDto);
    }

    // READ
    @GetMapping
    public List<EstacaoResponseDto> listarEstacoesAtivas() {
        return service.listarEstacoesAtivas();
    }

    @GetMapping("/{id}")
    public EstacaoResponseDto buscarEstacaoAtiva(@PathVariable String id) {
        return service.buscarEstacaoAtiva(id);
    }
}

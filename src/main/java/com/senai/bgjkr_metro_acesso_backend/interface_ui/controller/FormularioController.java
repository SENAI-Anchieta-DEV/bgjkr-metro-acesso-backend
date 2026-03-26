package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.FormularioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/formulario")
@RequiredArgsConstructor
public class FormularioController {
    private final FormularioService service;

    @PostMapping
    public FormSolicitacaoResponseDto enviarFormulario(@RequestBody FormSolicitacaoRequestDto requestDto) {
        return service.enviarFormulario(requestDto);
    }

    @PutMapping("/{email}")
    public FormSolicitacaoResponseDto atualizarFormulario(@PathVariable String email, @RequestBody FormSolicitacaoRequestDto requestDto) {
        return service.atualizarFormulario(email, requestDto);
    }

    @GetMapping("/{email}")
    public FormSolicitacaoResponseDto buscarFormularioAtivo(@PathVariable String email) {
        return service.buscarFormularioAtivo(email);
    }

    @DeleteMapping("/{email}")
    public void removerFormulario(@PathVariable String email) {
        service.removerFormulario(email);
    }

    @PutMapping("/{email}/validar")
    public FormAprovacaoResponseDto aprovarFormulario(@PathVariable String email, @RequestBody FormAprovacaoRequestDto requestDto) {
        return service.validarFormulario(email, requestDto);
    }

}


package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.FormularioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/formulario")
@RequiredArgsConstructor
public class FormularioController {
    private final FormularioService service;

    // CREATE
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FormSolicitacaoResponseDto> registrarFormulario(@ModelAttribute FormSolicitacaoRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/formulario"))
                .body(service.registrarFormulario(requestDto));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<FormAprovacaoResponseDto>> listarFormulariosAtivos() {
        return ResponseEntity
                .ok(service.listarFormulariosAtivos());
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<FormSolicitacaoResponseDto>> listarFormulariosPendentes() {
        return ResponseEntity
                .ok(service.listarFormulariosPendentes());
    }

    @GetMapping("/{email}")
    public ResponseEntity<FormAprovacaoResponseDto> buscarFormularioAtivo(@PathVariable String email) {
        return ResponseEntity
                .ok(service.buscarFormularioAtivo(email));
    }

    // UPDATE
    @PutMapping(path = "/pendentes/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FormSolicitacaoResponseDto> atualizarFormularioPendente(@PathVariable String email, @ModelAttribute FormSolicitacaoRequestDto requestDto) {
        return ResponseEntity
                .ok(service.atualizarFormularioPendente(email, requestDto));
    }

    // DELETE
    @DeleteMapping("/pendentes/{email}")
    public ResponseEntity<Void> removerFormularioPendente(@PathVariable String email) {
        service.removerFormularioPendente(email);
        return ResponseEntity
                .noContent()
                .build();
    }

    // REQUISITOS FUNCIONAIS
    @PostMapping("/validar/{email}")
    public ResponseEntity<FormAprovacaoResponseDto> validarFormulario(@PathVariable String email, @RequestBody @Valid FormAprovacaoRequestDto requestDto) {
        return ResponseEntity
                .ok(service.validarFormulario(email, requestDto));
    }
}


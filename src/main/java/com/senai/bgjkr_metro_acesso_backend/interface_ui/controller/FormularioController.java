package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.FormularioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/formulario")
@RequiredArgsConstructor
public class FormularioController {
    private final FormularioService service;

    // CREATE
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FormSolicitacaoResponseDto registrarFormulario(@ModelAttribute FormSolicitacaoRequestDto requestDto) {
        return service.registrarFormulario(requestDto);
    }

    // READ
    @GetMapping
    public List<FormAprovacaoResponseDto> listarFormulariosAtivos() {
        return service.listarFormulariosAtivos();
    }

    @GetMapping("/pendentes")
    public List<FormSolicitacaoResponseDto> listarFormulariosPendentes() {
        return service.listarFormulariosPendentes();
    }

    @GetMapping("/{email}")
    public FormAprovacaoResponseDto buscarFormularioAtivo(@PathVariable String email) {
        return service.buscarFormularioAtivo(email);
    }

    // UPDATE
    @PutMapping(path = "/pendentes/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FormSolicitacaoResponseDto atualizarFormularioPendente(@PathVariable String email, @ModelAttribute FormSolicitacaoRequestDto requestDto) {
        return service.atualizarFormularioPendente(email, requestDto);
    }

    // DELETE
    @DeleteMapping("/pendentes/{email}")
    public void removerFormularioPendente(@PathVariable String email) {
        service.removerFormularioPendente(email);
    }

    // REQUISITOS FUNCIONAIS
    @PostMapping("/validar/{email}")
    public FormAprovacaoResponseDto validarFormulario(@PathVariable String email, @RequestBody FormAprovacaoRequestDto requestDto) {
        return service.validarFormulario(email, requestDto);
    }
}


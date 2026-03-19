package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.FormularioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/formulario")
@RequiredArgsConstructor
public class FormularioController {
    private final FormularioService service;

    @PostMapping
    public FormSolicitacaoDto enviarFormulario(@RequestBody FormSolicitacaoDto requestDto) {
        return service.enviarFormulario(requestDto);
    }
}


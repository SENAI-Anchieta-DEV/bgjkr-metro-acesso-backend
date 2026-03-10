package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

    // CREATE
    @PostMapping
    public AdminResponseDto registrarAdmin(@RequestBody AdminRequestDto requestDto) {
        return service.registrarAdmin(requestDto);
    }
}
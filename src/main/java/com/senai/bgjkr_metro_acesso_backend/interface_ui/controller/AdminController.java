package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    // READ
    @GetMapping
    public List<AdminResponseDto> listarAdminsAtivos() {
        return service.listarAdminsAtivos();
    }

    @GetMapping("/{email}")
    public AdminResponseDto buscarAdminAtivo(@PathVariable String email) {
        return service.buscarAdminAtivo(email);
    }
}
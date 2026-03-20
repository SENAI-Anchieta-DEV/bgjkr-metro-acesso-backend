package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto requestDto) {
        return ResponseEntity
                .ok(service.login(requestDto));
    }
}
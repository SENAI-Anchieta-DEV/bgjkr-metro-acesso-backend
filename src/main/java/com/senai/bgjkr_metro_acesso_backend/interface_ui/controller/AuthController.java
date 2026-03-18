package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.auth.AuthDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;

    @PostMapping("/login")
    public AuthDto.TokenResponse login(@RequestBody AuthDto.LoginRequest requestDto) {
        String token = auth.login(requestDto);
        return new AuthDto.TokenResponse(token);
    }
}
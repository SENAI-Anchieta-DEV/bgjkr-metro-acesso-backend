package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.auth.AuthDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Usuario;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.UsuarioRepository;
import com.senai.bgjkr_metro_acesso_backend.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthService {
    private final UsuarioRepository usuarios;
    private final JwtService jwt;

    public String login(AuthDto.LoginRequest requesDto) {
        Usuario usuario = usuarios.findByEmail(requesDto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado.")); //Exception Especifica

        if (!requesDto.senha().equals(usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas."); //Exception Especifica
        }

        return jwt.generateToken(usuario.getEmail(), usuario.getRole().name());
    }
}

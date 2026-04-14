package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Usuario;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.auth.CredenciaisInvalidasException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.UsuarioRepository;
import com.senai.bgjkr_metro_acesso_backend.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; // IMPORTANTE: Adicionado o import
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder; // Lombok vai injetar isso automaticamente

    public AuthResponseDto login(AuthRequestDto requestDto) {
        Usuario usuario = usuarioRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", "email", requestDto.email()));

        // CORREÇÃO: Compara a senha digitada em texto puro com o hash Bcrypt salvo no banco
        if (!passwordEncoder.matches(requestDto.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        return new AuthResponseDto(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                jwtService.generateToken(usuario.getEmail(), usuario.getRole().name())
        );
    }
}
package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Usuario;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.UsuarioRepository;
import com.senai.bgjkr_metro_acesso_backend.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthResponseDto login(AuthRequestDto requestDto) {
        Usuario usuario = usuarioRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado.")); //Exception Especifica
        if (!requestDto.senha().equals(usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas."); //Exception Especifica
        }

        return new AuthResponseDto(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                jwtService.generateToken(usuario.getEmail(), usuario.getRole().name())
        );
    }
}

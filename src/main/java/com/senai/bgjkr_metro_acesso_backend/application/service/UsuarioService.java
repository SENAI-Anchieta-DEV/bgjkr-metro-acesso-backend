package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;

    protected void impedirEmailAtivo(String email) {
        if (repository.findByEmailAndAtivoTrue(email).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário ativo para este email."); // Exception específica em futura feature
        }
    }
}

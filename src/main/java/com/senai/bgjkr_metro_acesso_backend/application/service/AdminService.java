package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository repository;

    // CREATE
    @Transactional
    public AdminResponseDto registrarAdmin(AdminRequestDto requestDto) {
        Administrador adminRegistrado = repository.findByEmail(requestDto.email())
                .map(admin ->
                        admin.isAtivo() ? admin : reativarAdmin(admin, requestDto)
                )
                .orElseGet(() ->
                        {
                            Administrador admin = requestDto.toEntity();
                            admin.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
                            return admin;
                        }
                );

        return AdminResponseDto.fromEntity(repository.save(adminRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    public List<AdminResponseDto> listarAdminsAtivos() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(AdminResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminResponseDto buscarAdminAtivo(String email) {
        return AdminResponseDto.fromEntity(procurarAdminAtivo(email));
    }

    // Funções auxiliares
    private Administrador reativarAdmin(Administrador admin, AdminRequestDto dto) {
        admin.setAtivo(true);
        admin.setNome(dto.nome());
        admin.setEmail(dto.email());
        admin.setSenha(dto.senha()); // Criptografia de senha em futura feature

        return admin;
    }

    private Administrador procurarAdminAtivo(String email) {
        return repository
                .findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new RuntimeException("Entidade não encontrada.")); // Exception específica em futura feature
    }
}

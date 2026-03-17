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
                .map(admin -> admin.isAtivo() ? admin : reativarAdmin(admin, requestDto))
                .orElseGet(() -> criarAdmin(requestDto));

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

    // UPDATE
    @Transactional
    public AdminResponseDto atualizarAdmin(String email, AdminRequestDto requestDto) {
        Administrador adminAtualizado = procurarAdminAtivo(email);
        atualizarValores(adminAtualizado, requestDto);
        return AdminResponseDto.fromEntity(repository.save(adminAtualizado));
    }

    // DELETE
    @Transactional
    public void removerAdmin(String email) {
        Administrador adminRemovido = procurarAdminAtivo(email);
        adminRemovido.setAtivo(false);
        repository.save(adminRemovido);
    }

    // Funções auxiliares
    private Administrador procurarAdminAtivo(String email) {
        return repository
                .findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new RuntimeException("Entidade não encontrada.")); // Exception específica em futura feature
    }

    private Administrador reativarAdmin(Administrador admin, AdminRequestDto requestDto) {
        admin.setAtivo(true);
        atualizarValores(admin, requestDto);
        return admin;
    }

    private Administrador criarAdmin(AdminRequestDto requestDto) {
        Administrador admin = requestDto.toEntity();
        admin.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        return admin;
    }

    private void atualizarValores(Administrador admin, AdminRequestDto requestDto) {
        admin.setNome(requestDto.nome());
        admin.setEmail(requestDto.email());
        admin.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
    }
}

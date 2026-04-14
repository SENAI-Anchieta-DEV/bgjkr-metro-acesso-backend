package com.senai.bgjkr_metro_acesso_backend.infrastructure.config;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.AdminRepository;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        String emailAdmin = "admin@metroacesso.com";

        // 1. Verifica se o administrador já existe para não duplicar
        if (usuarioRepository.findByEmail(emailAdmin).isEmpty()) {

            // 2. Cria a entidade Administrador
            Administrador admin = new Administrador();
            admin.setNome("Administrador do Sistema");
            admin.setEmail(emailAdmin);

            // 3. Criptografa a senha usando BCrypt (importante para o Spring Security)
            admin.setSenha(passwordEncoder.encode("admin123"));

            admin.setAtivo(true);
            admin.setRole(Role.ADMINISTRADOR);

            // 4. Salva no banco de dados
            adminRepository.save(admin);

            System.out.println("===========================================");
            System.out.println("ADMIN INICIAL CRIADO: " + emailAdmin);
            System.out.println("===========================================");
        } else {
            System.out.println("ℹAdministrador inicial já existe no banco.");
        }
    }
}
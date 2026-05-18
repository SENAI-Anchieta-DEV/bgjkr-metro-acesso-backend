package com.senai.bgjkr_metro_acesso_backend.infrastructure.config;


import com.senai.bgjkr_metro_acesso_backend.domain.repository.AdminRepository;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements CommandLineRunner {

    private final AdminRepository adminRepository;

    @Value("${sistema.admin.email}")
    private String adminEmail;

    @Value("${sistema.admin.senha}")
    private String adminSenha;

    @Override
    public void run(String... args) {
        adminRepository.findByEmail(adminEmail).ifPresentOrElse(
                administrador -> {
                    if (!administrador.isAtivo()) {
                        administrador.setAtivo(true);
                        adminRepository.save(administrador);
                    }
                },
                () -> {
                    Administrador admin = Administrador.builder()
                            .nome("Administrador Provisório")
                            .email(adminEmail)
                            .senha(adminSenha)
                            .role(Role.ADMINISTRADOR)
                            .ativo(true)
                            .build();
                    adminRepository.save(admin);
                    System.out.println("⚡ Usuário admin provisório criado: " + adminEmail);
                }
        );
    }
}

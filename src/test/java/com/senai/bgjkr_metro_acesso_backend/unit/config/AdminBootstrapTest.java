package com.senai.bgjkr_metro_acesso_backend.unit.config;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.AdminRepository;
import com.senai.bgjkr_metro_acesso_backend.infrastructure.config.AdminBootstrap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminBootstrapTest {

    @Mock
    private AdminRepository adminRepository;

    private AdminBootstrap adminBootstrap;

    private final String EMAIL_TESTE = "admin@metroacesso.com";
    private final String SENHA_TESTE = "admin123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminBootstrap = new AdminBootstrap(adminRepository);

        // Injeta manualmente os valores que viriam do application.properties nas variáveis privadas da classe
        ReflectionTestUtils.setField(adminBootstrap, "adminEmail", EMAIL_TESTE);
        ReflectionTestUtils.setField(adminBootstrap, "adminSenha", SENHA_TESTE);
    }

    @Test
    @DisplayName("CT-01 - Deve criar administrador provisório com dados corretos quando ele não existir no banco")
    void deveCriarAdminProvisorioQuandoNaoExistir() {
        // Arrange
        when(adminRepository.findByEmail(EMAIL_TESTE)).thenReturn(Optional.empty());

        // Act
        adminBootstrap.run();

        // Assert
        ArgumentCaptor<Administrador> captor = ArgumentCaptor.forClass(Administrador.class);
        verify(adminRepository, times(1)).save(captor.capture());

        Administrador adminSalvo = captor.getValue();
        assertEquals("Administrador Provisório", adminSalvo.getNome());
        assertEquals(EMAIL_TESTE, adminSalvo.getEmail());
        assertEquals(SENHA_TESTE, adminSalvo.getSenha());
        assertEquals(Role.ADMINISTRADOR, adminSalvo.getRole());
        assertTrue(adminSalvo.isAtivo());
    }

    @Test
    @DisplayName("CT-02 - Deve apagar administrador provisório quando outro admin for cadastrado no banco")
    void deveApagarAdminProvisorioQuandoExistirAdminCadastrado() {
        // Arrange
        Administrador adminProvisorio = Administrador.builder()
                .nome("Administrador Provisório")
                .email(EMAIL_TESTE)
                .senha(SENHA_TESTE)
                .role(Role.ADMINISTRADOR)
                .ativo(true)
                .build();

        Administrador adminDefinitivo = Administrador.builder()
                .nome("Admin Definitivo")
                .email("diretor@metroacesso.com")
                .senha("senhaForte123")
                .role(Role.ADMINISTRADOR)
                .ativo(true)
                .build();

        // Simula que o banco retornará uma lista contendo um admin real e o provisório
        when(adminRepository.findAllByAtivoTrue()).thenReturn(List.of(adminDefinitivo, adminProvisorio));

        // Simula a busca específica pelo admin provisório
        when(adminRepository.findByEmail(EMAIL_TESTE)).thenReturn(Optional.of(adminProvisorio));

        // Act
        adminBootstrap.run();

        // Assert
        // Verifica se o método de exclusão do repositório foi chamado passando exatamente o admin provisório
        verify(adminRepository, times(1)).delete(adminProvisorio);

        // Garante que o administrador definitivo NUNCA foi apagado acidentalmente
        verify(adminRepository, never()).delete(adminDefinitivo);
    }

}
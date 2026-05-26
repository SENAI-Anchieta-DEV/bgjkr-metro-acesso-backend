package com.senai.bgjkr_metro_acesso_backend.integration.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthRequestDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.AdminRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@DisplayName("CT-01 | RF01 - Criação automática do administrador provisório no bootstrap")
class AdminProvisorioBootstrapIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AdminRepository adminRepository;

    @Value("${sistema.admin.email}")
    private String adminEmail;

    @Value("${sistema.admin.senha}")
    private String adminSenha;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Deve criar o administrador provisório no banco de dados ao inicializar o sistema com banco vazio")
    void deveCriarAdminProvisorioNoBancoAoInicializar() {
        // Assert — o AdminBootstrap já rodou durante a inicialização do contexto Spring
        Optional<Administrador> adminProvisorio = adminRepository.findByEmail(adminEmail);

        assertTrue(adminProvisorio.isPresent(),
                "O administrador provisório deve existir no banco após a inicialização");
        assertTrue(adminProvisorio.get().isAtivo(),
                "O administrador provisório deve estar ativo");
        assertEquals("Administrador Provisório", adminProvisorio.get().getNome(),
                "O nome do administrador provisório deve ser 'Administrador Provisório'");
        assertEquals(adminEmail, adminProvisorio.get().getEmail(),
                "O e-mail do administrador provisório deve corresponder ao configurado no properties");
    }

    @Test
    @DisplayName("Deve permitir autenticação com as credenciais do administrador provisório recém-criado")
    void devePermitirAutenticacaoComAdminProvisorio() {
        // Arrange
        AuthRequestDto payload = new AuthRequestDto(adminEmail, adminSenha);

        // Act & Assert
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 401 ao tentar autenticar o administrador provisório com senha incorreta")
    void deveRetornar401ComSenhaIncorreta() {
        // Arrange
        AuthRequestDto payload = new AuthRequestDto(adminEmail, "senhaErradaQualquer99!");

        // Act & Assert
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }
}


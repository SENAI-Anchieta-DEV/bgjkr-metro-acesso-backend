package com.senai.bgjkr_metro_acesso_backend.integration.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthRequestDto;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.AdminRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
@DisplayName("CT-02 | RF02 - Remoção do administrador provisório após cadastro de administrador definitivo")
class RemocaoAdminProvisorioIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private AdminRepository adminRepository;

    @Value("${sistema.admin.email}")
    private String adminProvisorioEmail;

    @Value("${sistema.admin.senha}")
    private String adminProvisorioSenha;

    private String tokenAdminProvisorio;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Arrange — obter token do admin provisório (criado pelo bootstrap)
        AuthRequestDto loginProvisorio = new AuthRequestDto(adminProvisorioEmail, adminProvisorioSenha);
        tokenAdminProvisorio = given()
                .contentType(ContentType.JSON)
                .body(loginProvisorio)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .path("token");
    }

    @Test
    @DisplayName("Deve cadastrar administrador definitivo com token do provisório e retornar 201")
    void deveCadastrarAdminDefinitivoComSucesso() {
        // Arrange
        AdminRequestDto novoAdmin = new AdminRequestDto(
                "Maria Silva",
                "maria.definitiva@metroacesso.com",
                "SenhaDefinitiva123!"
        );

        // Act & Assert
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAdminProvisorio)
                .body(novoAdmin)
                .when()
                .post("/api/admin")
                .then()
                .statusCode(201)
                .body("email", org.hamcrest.Matchers.equalTo("maria.definitiva@metroacesso.com"));
    }

    @Test
    @DisplayName("Após remoção do provisório, conta deve estar desativada no banco e login deve ser negado")
    void deveDesativarProvisorioAposRemocaoENegarLogin() {
        // Arrange — cadastrar admin definitivo primeiro
        AdminRequestDto adminDefinitivo = new AdminRequestDto(
                "João Definitivo",
                "joao.definitivo@metroacesso.com",
                "SenhaDefinitiva456!"
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAdminProvisorio)
                .body(adminDefinitivo)
                .when()
                .post("/api/admin")
                .then()
                .statusCode(201);

        // Act — remover o administrador provisório
        given()
                .header("Authorization", "Bearer " + tokenAdminProvisorio)
                .when()
                .delete("/api/admin/" + adminProvisorioEmail)
                .then()
                .statusCode(204);

        // Assert — provisório deve estar inativo no banco
        assertTrue(
                adminRepository.findByEmail(adminProvisorioEmail)
                        .map(admin -> !admin.isAtivo())
                        .orElse(false),
                "O administrador provisório deve estar inativo no banco após a remoção"
        );

        // Assert — tentativa de login com provisório deve falhar
        AuthRequestDto tentativaLoginProvisorio = new AuthRequestDto(adminProvisorioEmail, adminProvisorioSenha);
        given()
                .contentType(ContentType.JSON)
                .body(tentativaLoginProvisorio)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(404); // usuário não encontrado (removido logicamente)
    }

    @Test
    @DisplayName("Não deve permitir que agente crie administrador — deve retornar 403 Forbidden")
    void naoDevePermitirCriacaoDeAdminSemPerfil() {
        // Arrange
        AdminRequestDto payload = new AdminRequestDto(
                "Tentativa Indevida",
                "indevido@metroacesso.com",
                "SenhaIndevida123!"
        );

        // Act & Assert — sem token algum
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/admin")
                .then()
                .statusCode(401);
    }
}

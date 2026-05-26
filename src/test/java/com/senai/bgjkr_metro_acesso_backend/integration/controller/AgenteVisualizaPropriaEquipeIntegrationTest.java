package com.senai.bgjkr_metro_acesso_backend.integration.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoRequestDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@DisplayName("CT-03 | RF10 - Agente visualiza apenas agentes da sua própria equipe")
class AgenteVisualizaPropriaEquipeIntegrationTest {

    @LocalServerPort
    private int port;

    @Value("${sistema.admin.email}")
    private String adminEmail;

    @Value("${sistema.admin.senha}")
    private String adminSenha;

    private String tokenAdmin;
    private String tokenAgenteA;
    private static final String EMAIL_AGENTE_A = "agente.equipe.a@metroacesso.com";
    private static final String EMAIL_AGENTE_B = "agente.equipe.b@metroacesso.com";
    private static final String SENHA_AGENTES = "SenhaAgente123!";

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Obter token do admin provisório
        tokenAdmin = given()
                .contentType(ContentType.JSON)
                .body(new AuthRequestDto(adminEmail, adminSenha))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        // Criar estação A
        EstacaoRequestDto estacaoA = new EstacaoRequestDto("Estação Sé", "EST-SE-01", Set.of(1, 3));
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAdmin)
                .body(estacaoA)
                .when()
                .post("/api/estacao")
                .then()
                .statusCode(anyOf(equalTo(201), equalTo(200)));

        // Criar estação B
        EstacaoRequestDto estacaoB = new EstacaoRequestDto("Estação Paulista", "EST-PA-01", Set.of(2));
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAdmin)
                .body(estacaoB)
                .when()
                .post("/api/estacao")
                .then()
                .statusCode(anyOf(equalTo(201), equalTo(200)));

        // Criar agente da Equipe A (vinculado à estação A)
        AgenteRequestDto agenteA = new AgenteRequestDto(
                "Agente Equipe A",
                EMAIL_AGENTE_A,
                SENHA_AGENTES,
                LocalTime.of(6, 0),
                LocalTime.of(14, 0),
                "EST-SE-01"
        );
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAdmin)
                .body(agenteA)
                .when()
                .post("/api/agente")
                .then()
                .statusCode(anyOf(equalTo(201), equalTo(200)));

        // Criar agente da Equipe B (vinculado à estação B)
        AgenteRequestDto agenteB = new AgenteRequestDto(
                "Agente Equipe B",
                EMAIL_AGENTE_B,
                SENHA_AGENTES,
                LocalTime.of(6, 0),
                LocalTime.of(14, 0),
                "EST-PA-01"
        );
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + tokenAdmin)
                .body(agenteB)
                .when()
                .post("/api/agente")
                .then()
                .statusCode(anyOf(equalTo(201), equalTo(200)));

        // Autenticar como Agente A
        tokenAgenteA = given()
                .contentType(ContentType.JSON)
                .body(new AuthRequestDto(EMAIL_AGENTE_A, SENHA_AGENTES))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    @DisplayName("Agente deve conseguir visualizar seus próprios dados via GET /api/agente/{email}")
    void agenteDeveVisualizarPropriosDados() {
        // Act & Assert
        given()
                .header("Authorization", "Bearer " + tokenAgenteA)
                .when()
                .get("/api/agente/" + EMAIL_AGENTE_A)
                .then()
                .statusCode(200)
                .body("email", equalTo(EMAIL_AGENTE_A))
                .body("estacao.codigoEstacao", equalTo("EST-SE-01"));
    }

    @Test
    @DisplayName("Agente não deve conseguir visualizar dados de agente de outra equipe — deve retornar 403")
    void agenteNaoDeveVisualizarDadosDeOutraEquipe() {
        // Act & Assert — Agente A tentando acessar Agente B (outra estação/equipe)
        given()
                .header("Authorization", "Bearer " + tokenAgenteA)
                .when()
                .get("/api/agente/" + EMAIL_AGENTE_B)
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Agente não deve acessar a listagem completa de agentes — deve retornar 403")
    void agenteNaoDeveAcessarListagemCompletaDeAgentes() {
        // Act & Assert — listagem completa é exclusiva do ADMINISTRADOR
        given()
                .header("Authorization", "Bearer " + tokenAgenteA)
                .when()
                .get("/api/agente")
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Administrador deve conseguir visualizar todos os agentes independentemente da equipe")
    void adminDeveVisualizarTodosOsAgentes() {
        // Act
        List<String> emails = given()
                .header("Authorization", "Bearer " + tokenAdmin)
                .when()
                .get("/api/agente")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("email", String.class);

        // Assert — ambos os agentes devem aparecer para o admin
        assertTrue(emails.contains(EMAIL_AGENTE_A),
                "Administrador deve visualizar o Agente A");
        assertTrue(emails.contains(EMAIL_AGENTE_B),
                "Administrador deve visualizar o Agente B");
    }
}


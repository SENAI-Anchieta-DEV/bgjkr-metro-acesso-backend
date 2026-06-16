package com.senai.bgjkr_metro_acesso_backend.integration.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthRequestDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.*;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.*;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.*;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("local")
@DisplayName("CT-04 | RF11 - Confirmação e Exclusão de Pendência de Atendimento")
class PendenciaAtendimentoIntegrationTest {

    @LocalServerPort
    private int port;

    @Value("${sistema.admin.email}")
    private String adminEmail;

    @Value("${sistema.admin.senha}")
    private String adminSenha;

    @Autowired
    private PendenciaRepository pendenciaRepository;

    @Autowired
    private AgenteRepository agenteRepository;

    @Autowired
    private EstacaoRepository estacaoRepository;

    @Autowired
    private EntradaRepository entradaRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PcdRepository pcdRepository;

    private String tokenAdmin;
    private String tokenAgente;

    private final String emailAgente = "agente.atendimento@metroacesso.com";
    private String pendenciaId;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        // Limpeza dos repositórios para garantir isolamento total e estado limpo entre execuções
        pendenciaRepository.deleteAll();
        pcdRepository.deleteAll();
        tagRepository.deleteAll();
        agenteRepository.deleteAll();
        entradaRepository.deleteAll();
        estacaoRepository.deleteAll();

        // ARRANGE - Configuração das Pré-condições do Cenário de Teste
        Estacao estacao = Estacao.builder()
                .nome("Estação Sé")
                .codigoEstacao("EST-SE-01")
                .linhas(Set.of(Linha.AZUL, Linha.VERMELHA))
                .ativo(true)
                .build();
        estacao = estacaoRepository.save(estacao);

        Entrada entrada = Entrada.builder()
                .codigoEntrada("ENT-SE-01")
                .estacao(estacao)
                .ativo(true)
                .build();
        entrada = entradaRepository.save(entrada);

        String senhaAgente = "SenhaAgente123!";
        AgenteAtendimento agente = AgenteAtendimento.builder()
                .nome("Agente Operacional Sé")
                .email(emailAgente)
                .senha(senhaAgente)
                .role(Role.AGENTE_ATENDIMENTO)
                .inicioTurno(LocalTime.of(6, 0))
                .fimTurno(LocalTime.of(14, 0))
                .estacao(estacao)
                .ativo(true)
                .build();
        agente = agenteRepository.save(agente);

        TagPcd tag = TagPcd.builder()
                .codigoTag("TAG-XYZ-789")
                .ativo(true)
                .build();
        tag = tagRepository.save(tag);

        UsuarioPcd pcd = UsuarioPcd.builder()
                .nome("Usuário PcD Teste")
                .email("pcd.teste@gmail.com")
                .senha("SenhaPcd123!")
                .role(Role.USUARIO_PCD)
                .tiposDeficiencia(Set.of(TipoDeficiencia.MOTORA))
                .desejaSuporte(true)
                .tag(tag)
                .ativo(true)
                .build();
        pcd = pcdRepository.save(pcd);

        tag.setUsuarioPcd(pcd);
        tagRepository.save(tag);

        // Criação da Pendência Ativa (Simulando a identificação do PcD na estação por IoT/MQTT)
        PendenciaAtendimento pendencia = PendenciaAtendimento.builder()
                .pcdAtendido(pcd)
                .agente(agente)
                .estacao(estacao)
                .entrada(entrada)
                .dataHora(LocalDateTime.now())
                .ativo(true)
                .build();
        pendencia = pendenciaRepository.save(pendencia);
        pendenciaId = pendencia.getId();

        // Geração dos Tokens JWT de Autenticação necessários para os fluxos
        tokenAdmin = given()
                .contentType(ContentType.JSON)
                .body(new AuthRequestDto(adminEmail, adminSenha))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        tokenAgente = given()
                .contentType(ContentType.JSON)
                .body(new AuthRequestDto(emailAgente, senhaAgente))
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .path("token");
    }

    @Test
    @DisplayName("Deve permitir que o agente confirme o atendimento e a pendência seja removida e excluída corretamente")
    void deveConfirmarAtendimentoEExcluirPendenciaComSucesso() {

        // GIVEN: O agente acessa o painel de atendimentos e localiza a pendência ativa atribuída a ele
        given()
                .header("Authorization", "Bearer " + tokenAgente)
                .when()
                .get("/api/pendencia-atendimento/agente/" + emailAgente)
                .then()
                .statusCode(200)
                .body("id", hasItem(pendenciaId))
                .body("statusAtendimento", hasItem("PENDENTE"));

        // WHEN: O agente pressiona o botão de confirmação de atendimento enviando a requisição HTTP
        // Nota: Certifique-se de expor o endpoint correspondente ao método confirmarAtendimento no PendenciaController!
        given()
                .header("Authorization", "Bearer " + tokenAgente)
                .when()
                .delete("/api/pendencia-atendimento/" + pendenciaId)
                .then()
                .statusCode(204);

        // THEN: O administrador realiza a limpeza técnica/exclusão da pendência finalizada
        given()
                .header("Authorization", "Bearer " + tokenAdmin)
                .when()
                .delete("/api/pendencia-atendimento/" + pendenciaId)
                .then()
                .statusCode(204);

        // E: Confirma diretamente no Banco de Dados (H2 em memória) se os vínculos e o registro foram removidos com sucesso
        Optional<PendenciaAtendimento> pendenciaNoBanco = pendenciaRepository.findById(pendenciaId);
        assertTrue(pendenciaNoBanco.isEmpty(), "A notificação/pendência de atendimento deve ter sido excluída completamente do banco de dados.");
    }
}

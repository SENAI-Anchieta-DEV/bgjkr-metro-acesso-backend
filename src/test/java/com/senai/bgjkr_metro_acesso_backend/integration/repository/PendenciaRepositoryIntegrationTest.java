package com.senai.bgjkr_metro_acesso_backend.integration.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.PendenciaAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Linha;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.AgenteRepository;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.EntradaRepository;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.EstacaoRepository;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.PendenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class PendenciaRepositoryIntegrationTest {

    @Autowired
    private PendenciaRepository pendenciaRepository;

    @Autowired
    private AgenteRepository agenteRepository;

    @Autowired
    private EstacaoRepository estacaoRepository;

    @Autowired
    private EntradaRepository entradaRepository;

    private Estacao estacao;
    private AgenteAtendimento agente;
    private Entrada entrada;
    private UsuarioPcd pcd;

    @BeforeEach
    void setUp() {
        pendenciaRepository.deleteAll();

        estacao = estacaoRepository.save(
                Estacao.builder()
                        .nome("Estação Sé")
                        .codigoEstacao("EST001")
                        .linhas(Set.of(Linha.AZUL, Linha.VERMELHA))
                        .ativo(true)
                        .build()
        );

        agente = agenteRepository.save(
                AgenteAtendimento.builder()
                        .nome("Agente Teste")
                        .email("agente@email.com")
                        .senha("senha123")
                        .role(Role.AGENTE_ATENDIMENTO)
                        .ativo(true)
                        .estacao(estacao)
                        .inicioTurno(LocalTime.of(8, 0))
                        .fimTurno(LocalTime.of(17, 0))
                        .build()
        );

        entrada = entradaRepository.save(
                Entrada.builder()
                        .codigoEntrada("ENT001")
                        .estacao(estacao)
                        .ativo(true)
                        .build()
        );
    }

    private PendenciaAtendimento criarPendencia(boolean ativo) {
        return PendenciaAtendimento.builder()
                .agente(agente)
                .estacao(estacao)
                .entrada(entrada)
                .dataHora(LocalDateTime.now())
                .statusAtendimento(StatusAtendimento.PENDENTE)
                .ativo(ativo)
                .build();
    }

    @Test
    @DisplayName("Deve retornar somente pendências ativas")
    void deveRetornarSomentePendenciasAtivas() {
        // Arrange
        pendenciaRepository.save(criarPendencia(true));
        pendenciaRepository.save(criarPendencia(true));
        pendenciaRepository.save(criarPendencia(false));

        // Act
        List<PendenciaAtendimento> resultado = pendenciaRepository.findAllByAtivoTrue();

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(PendenciaAtendimento::isAtivo));
    }

    @Test
    @DisplayName("Deve retornar pendências ativas do agente")
    void deveRetornarPendenciasAtivasDoAgente() {
        // Arrange
        pendenciaRepository.save(criarPendencia(true));
        pendenciaRepository.save(criarPendencia(false));

        // Act
        List<PendenciaAtendimento> resultado =
                pendenciaRepository.findAllByAgenteAndAtivoTrue(agente);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(agente.getId(), resultado.get(0).getAgente().getId());
        assertTrue(resultado.get(0).isAtivo());
    }

    @Test
    @DisplayName("Deve retornar pendências ativas da estação")
    void deveRetornarPendenciasAtivasDaEstacao() {
        // Arrange
        pendenciaRepository.save(criarPendencia(true));
        pendenciaRepository.save(criarPendencia(true));
        pendenciaRepository.save(criarPendencia(false));

        // Act
        List<PendenciaAtendimento> resultado =
                pendenciaRepository.findAllByEstacaoAndAtivoTrue(estacao);

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getEstacao().getId().equals(estacao.getId())));
        assertTrue(resultado.stream().allMatch(PendenciaAtendimento::isAtivo));
    }
}
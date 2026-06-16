package com.senai.bgjkr_metro_acesso_backend.unit.service;

import com.senai.bgjkr_metro_acesso_backend.application.service.AgenteService;
import com.senai.bgjkr_metro_acesso_backend.application.service.EstacaoService;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.AgenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgenteServiceTest {

    @Mock
    private AgenteRepository repository;

    @Mock
    private EstacaoService estacaoService;

    @InjectMocks
    private AgenteService agenteService;

    private Estacao estacaoEquipeA;
    private Estacao estacaoEquipeB;

    private AgenteAtendimento agenteEquipeA1;
    private AgenteAtendimento agenteEquipeA2;
    private AgenteAtendimento agenteEquipeB1;

    @BeforeEach
    void setUp() {
        estacaoEquipeA = new Estacao();
        estacaoEquipeA.setId("EST-A");

        estacaoEquipeB = new Estacao();
        estacaoEquipeB.setId("EST-B");

        agenteEquipeA1 = new AgenteAtendimento();
        agenteEquipeA1.setEmail("agente1@equipea.com");
        agenteEquipeA1.setAtivo(true);
        agenteEquipeA1.setEstacao(estacaoEquipeA);

        agenteEquipeA2 = new AgenteAtendimento();
        agenteEquipeA2.setEmail("agente2@equipea.com");
        agenteEquipeA2.setAtivo(true);
        agenteEquipeA2.setEstacao(estacaoEquipeA);

        agenteEquipeB1 = new AgenteAtendimento();
        agenteEquipeB1.setEmail("agente1@equipeb.com");
        agenteEquipeB1.setAtivo(true);
        agenteEquipeB1.setEstacao(estacaoEquipeB);
    }

    @Test
    @DisplayName("CT-03 - Agente visualiza apenas membros da própria equipe; acesso a agente de outra equipe retorna erro de permissão")
    void deveVisualizarApenasAgentesDaPropriaEquipeENegarAcessoAOutraEquipe() {
        // Arrange
        LocalTime horaBusca = LocalTime.of(10, 0);

        // Listagem da Equipe A retorna somente seus agentes
        when(repository.findByEstacaoAndHorarioNoTurno(estacaoEquipeA, horaBusca))
                .thenReturn(List.of(agenteEquipeA1, agenteEquipeA2));

        // Tentativa de busca direta por e-mail de agente da Equipe B não encontra registro ativo
        when(repository.findByEmailAndAtivoTrue(agenteEquipeB1.getEmail()))
                .thenReturn(Optional.empty());

        // Act – cenário 1: listagem filtrada pela própria equipe
        List<AgenteAtendimento> resultadoEquipeA =
                agenteService.procurarAgentesDisponiveis(estacaoEquipeA, horaBusca);

        // Assert – cenário 1
        assertNotNull(resultadoEquipeA, "A lista retornada não deve ser nula.");
        assertEquals(2, resultadoEquipeA.size(),
                "Deve retornar exatamente os 2 agentes pertencentes à Equipe A.");
        assertTrue(resultadoEquipeA.contains(agenteEquipeA1),
                "O agente 1 da Equipe A deve constar na listagem.");
        assertTrue(resultadoEquipeA.contains(agenteEquipeA2),
                "O agente 2 da Equipe A deve constar na listagem.");
        assertFalse(resultadoEquipeA.contains(agenteEquipeB1),
                "Agentes da Equipe B não devem aparecer na listagem da Equipe A.");

        verify(repository, times(1))
                .findByEstacaoAndHorarioNoTurno(estacaoEquipeA, horaBusca);

        // Act – cenário 2: tentativa de acesso direto a agente da Equipe B
        EntidadeNaoEncontradaException excecao = assertThrows(
                EntidadeNaoEncontradaException.class,
                () -> agenteService.procurarAgenteAtivo(agenteEquipeB1.getEmail()),
                "Deve lançar EntidadeNaoEncontradaException ao tentar acessar agente de outra equipe."
        );

        // Assert – cenário 2
        assertNotNull(excecao.getMessage(),
                "A exceção deve conter uma mensagem descritiva.");
        assertTrue(excecao.getMessage().contains(agenteEquipeB1.getEmail()),
                "A mensagem da exceção deve identificar o e-mail que não foi encontrado.");

        verify(repository, times(1))
                .findByEmailAndAtivoTrue(agenteEquipeB1.getEmail());
    }
}
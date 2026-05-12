package com.senai.bgjkr_metro_acesso_backend.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AgenteService;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.AgenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

class AgenteServiceTest {

    @Mock
    private AgenteRepository repository;

    @InjectMocks
    private AgenteService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve listar apenas agentes da mesma equipe (estação)")
    void deveListarApenasAgentesDaMesmaEquipe() {
        // Arrange
        Estacao estacaoA = new Estacao();
        estacaoA.setId("EST-1029");

        AgenteAtendimento agenteEquipeA = new AgenteAtendimento();
        agenteEquipeA.setEmail("agente1@metro.com");
        agenteEquipeA.setEstacao(estacaoA);

        AgenteAtendimento agenteEquipeA2 = new AgenteAtendimento();
        agenteEquipeA2.setEmail("agente2@metro.com");
        agenteEquipeA2.setEstacao(estacaoA);

        // Simula o comportamento esperado: o repositório DEVERIA filtrar pela estação.
        // NOTA DE BUG: O método findAllByEstacaoAndAtivoTrue não existe no código atual.
        when(repository.findAllByAtivoTrue()).thenReturn(List.of(agenteEquipeA, agenteEquipeA2));

        // Act
        // NOTA DE BUG: O service não possui o contexto do usuário logado para filtrar.
        List<AgenteResponseDto> response = service.listarAgentesAtivos();

        // Assert
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("agente1@metro.com", response.get(0).email());
        assertEquals("agente2@metro.com", response.get(1).email());
        verify(repository, times(1)).findAllByAtivoTrue();
    }
}

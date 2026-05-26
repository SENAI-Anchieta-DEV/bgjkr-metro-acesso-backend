package com.senai.bgjkr_metro_acesso_backend.unit.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.*;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.*;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.PendenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PendenciaServiceTest {

    @Mock
    private PendenciaRepository repository;

    @Mock
    private EstacaoService estacaoService;

    @Mock
    private TagService tagService;

    @Mock
    private EntradaService entradaService;

    @Mock
    private AgenteService agenteService;

    private PendenciaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new PendenciaService(repository, estacaoService, tagService, entradaService, agenteService);
    }

    @Test
    @DisplayName("Deve retornar agente disponível para atendimento")
    void deveSelecionarAgenteDisponivel() {
        // ARRANGE
        Estacao estacao = new Estacao();
        estacao.setId("EST01");

        AgenteAtendimento agente = new AgenteAtendimento();
        agente.setId("AG01");
        agente.setNome("Agente 1");

        when(agenteService.procurarAgentesDisponiveis(eq(estacao), any(LocalTime.class)))
                .thenReturn(List.of(agente));

        // ACT
        List<AgenteAtendimento> result =
                agenteService.procurarAgentesDisponiveis(estacao, LocalTime.now());

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("AG01", result.get(0).getId());

        verify(agenteService, times(1))
                .procurarAgentesDisponiveis(eq(estacao), any(LocalTime.class));
    }

    @Test
    @DisplayName("Deve criar pendência corretamente")
    void deveCriarPendenciaCorretamente() {

        // ARRANGE
        PendenciaRequestDto dto = new PendenciaRequestDto(
                "TAG123",
                "EST01",
                "ENT01",
                true,
                LocalDateTime.now()
        );

        UsuarioPcd usuarioPcd = new UsuarioPcd();
        usuarioPcd.setId("USER01");

        TagPcd tag = new TagPcd();
        tag.setUsuarioPcd(usuarioPcd);

        Estacao estacao = new Estacao();
        estacao.setId("EST01");

        Entrada entrada = new Entrada();
        entrada.setId("ENT01");

        AgenteAtendimento agente = new AgenteAtendimento();
        agente.setId("AG01");
        agente.setNome("Agente Teste");

        when(tagService.procurarTagAtiva("TAG123")).thenReturn(tag);
        when(estacaoService.procurarEstacaoAtiva("EST01")).thenReturn(estacao);
        when(entradaService.procurarEntradaAtiva("ENT01")).thenReturn(entrada);

        when(agenteService.procurarAgentesDisponiveis(eq(estacao), any(LocalTime.class)))
                .thenReturn(List.of(agente));

        when(repository.save(any(PendenciaAtendimento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // ACT
        PendenciaResponseDto response = service.criarPendencia(dto);

        // ASSERT
        assertNotNull(response);

        // validações mínimas de comportamento
        verify(tagService).procurarTagAtiva("TAG123");
        verify(estacaoService).procurarEstacaoAtiva("EST01");
        verify(entradaService).procurarEntradaAtiva("ENT01");

        verify(agenteService).procurarAgentesDisponiveis(eq(estacao), any(LocalTime.class));

        ArgumentCaptor<PendenciaAtendimento> captor =
                ArgumentCaptor.forClass(PendenciaAtendimento.class);

        verify(repository).save(captor.capture());

        PendenciaAtendimento salvo = captor.getValue();
        assertNotNull(salvo);
    }
}
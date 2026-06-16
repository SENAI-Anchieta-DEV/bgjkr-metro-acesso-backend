package com.senai.bgjkr_metro_acesso_backend.unit.service;

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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("Deve encontrar código serial da tag no banco de dados e criar a pendência com sucesso")
    void deveEncontrarCodigoTagNoBanco() {
        // ARRANGE
        PendenciaRequestDto pendenciaRequestDto = new PendenciaRequestDto(
                "1234",
                "ESTACAO_01",
                "ENTRADA_01",
                LocalDateTime.now()
        );

        TagPcd tagPcdMock = mock(TagPcd.class);

        when(tagService.procurarTagAtiva(pendenciaRequestDto.codigoTag()))
                .thenReturn(tagPcdMock);

        when(tagPcdMock.getUsuarioPcd())
                .thenReturn(mock(UsuarioPcd.class));

        when(estacaoService.procurarEstacaoAtiva(pendenciaRequestDto.codigoEstacao()))
                .thenReturn(mock(Estacao.class));

        when(entradaService.procurarEntradaAtiva(pendenciaRequestDto.codigoEntrada()))
                .thenReturn(mock(Entrada.class));

        when(agenteService.procurarAgentesDisponiveis(any(Estacao.class), any(LocalTime.class)))
                .thenReturn(new ArrayList<>(List.of(mock(AgenteAtendimento.class))));

        when(repository.save(any(PendenciaAtendimento.class)))
                .thenAnswer(invocation -> invocation.<PendenciaAtendimento>getArgument(0));

        // ACT
        PendenciaResponseDto response = service.criarPendencia(pendenciaRequestDto);

        // ASSERT
        assertNotNull(response);
        verify(tagService, times(1)).procurarTagAtiva("1234");
        verify(repository, times(1)).save(any(PendenciaAtendimento.class));
    }

    @Test
    @DisplayName("Deve associar a tag identificada a um PcD e salvar a pendência corretamente")
    void deveAssociarTagAPcd() {
        // ARRANGE
        TagPcd tagPcd = new TagPcd();
        tagPcd.setCodigoTag("1234");
        AgenteAtendimento agente = new AgenteAtendimento();

        UsuarioPcd usuarioPcd = new UsuarioPcd();
        usuarioPcd.setDesejaSuporte(true);
        usuarioPcd.setTag(tagPcd);
        tagPcd.setUsuarioPcd(usuarioPcd);

        PendenciaRequestDto dto = new PendenciaRequestDto(
                "1234",
                "EST01",
                "ENT01",
                LocalDateTime.parse("2026-05-29T12:00:00")
        );

        when(tagService.procurarTagAtiva(dto.codigoTag()))
                .thenReturn(tagPcd);

        when(estacaoService.procurarEstacaoAtiva(dto.codigoEstacao()))
                .thenReturn(mock(Estacao.class));

        when(entradaService.procurarEntradaAtiva(dto.codigoEntrada()))
                .thenReturn(mock(Entrada.class));

        when(agenteService.procurarAgentesDisponiveis(any(Estacao.class), any(LocalTime.class)))
                .thenReturn(new ArrayList<>(List.of(agente)));

        when(repository.save(any(PendenciaAtendimento.class)))
                .thenAnswer(invocation -> invocation.<PendenciaAtendimento>getArgument(0));
        ArgumentCaptor<PendenciaAtendimento> captor = ArgumentCaptor.forClass(PendenciaAtendimento.class);

        // ACT
        service.criarPendencia(dto);

        // ASSERT
        verify(repository).save(captor.capture());
        PendenciaAtendimento pendenciaCapturada = captor.getValue();
        assertEquals(usuarioPcd, pendenciaCapturada.getPcdAtendido());
    }

    @Test
    @DisplayName("Deve solicitar agente disponível para atendimento")
    void deveSolicitarAgenteDisponivel() {
        // ARRANGE
        TagPcd tagPcd = new TagPcd();
        tagPcd.setUsuarioPcd(new UsuarioPcd());

        AgenteAtendimento agente = new AgenteAtendimento();
        agente.setInicioTurno(LocalTime.parse("11:00:00"));
        agente.setFimTurno(LocalTime.parse("18:00:00"));

        PendenciaRequestDto pendenciaRequestDto = new PendenciaRequestDto(
                "1234",
                "EST01",
                "ENT01",
                LocalDateTime.parse("2026-05-29T12:00:00")
        );

        when(tagService.procurarTagAtiva(pendenciaRequestDto.codigoTag())).
                thenReturn(tagPcd);

        when(estacaoService.procurarEstacaoAtiva(pendenciaRequestDto.codigoEstacao()))
                .thenReturn(mock(Estacao.class));

        when(entradaService.procurarEntradaAtiva(pendenciaRequestDto.codigoEntrada()))
                .thenReturn(mock(Entrada.class));

        when(agenteService.procurarAgentesDisponiveis(any(Estacao.class), any(LocalTime.class)))
                .thenReturn(new ArrayList<>(List.of(agente)));

        when(repository.save(any(PendenciaAtendimento.class)))
                .thenAnswer(invocation -> invocation.<PendenciaAtendimento>getArgument(0));
        ArgumentCaptor<PendenciaAtendimento> captor = ArgumentCaptor.forClass(PendenciaAtendimento.class);

        // ACT
        service.criarPendencia(pendenciaRequestDto);

        // ASSERT
        verify(repository).save(captor.capture());
        PendenciaAtendimento pendenciaCapturada = captor.getValue();
        assertEquals(agente, pendenciaCapturada.getAgente());
    }
}
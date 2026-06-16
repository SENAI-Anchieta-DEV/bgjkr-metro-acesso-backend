package com.senai.bgjkr_metro_acesso_backend.unit.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.*;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.*;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    // Instância criada manualmente para suportar injeção via construtor (testes de criação)
    private PendenciaService service;

    // Entidades compartilhadas pelos testes de confirmação/remoção
    private AgenteAtendimento agente;
    private Estacao estacao;
    private Entrada entrada;
    private UsuarioPcd pcd;
    private PendenciaAtendimento pendenciaAtiva;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new PendenciaService(repository, estacaoService, tagService, entradaService, agenteService);

        // Setup compartilhado para testes de confirmação/remoção
        estacao = new Estacao();
        estacao.setId("EST-A");

        agente = new AgenteAtendimento();
        agente.setId("AGT-01");
        agente.setEmail("agente@equipea.com");
        agente.setPendencias(new ArrayList<>());

        entrada = new Entrada();
        entrada.setId("ENT-01");
        entrada.setPendencias(new ArrayList<>());

        pcd = new UsuarioPcd();
        pcd.setId("PCD-01");
        pcd.setPendencias(new ArrayList<>());

        pendenciaAtiva = PendenciaAtendimento.builder()
                .id("PEND-001")
                .agente(agente)
                .estacao(estacao)
                .entrada(entrada)
                .pcdAtendido(pcd)
                .dataHora(LocalDateTime.now())
                .statusAtendimento(StatusAtendimento.PENDENTE)
                .ativo(true)
                .build();

        agente.getPendencias().add(pendenciaAtiva);
        pcd.getPendencias().add(pendenciaAtiva);
        estacao.setPendencias(new ArrayList<>());
        estacao.getPendencias().add(pendenciaAtiva);
        entrada.getPendencias().add(pendenciaAtiva);
    }

    // -------------------------------------------------------------------------
    // Testes de criação de pendência
    // -------------------------------------------------------------------------

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
        AgenteAtendimento agenteMock = new AgenteAtendimento();

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
                .thenReturn(new ArrayList<>(List.of(agenteMock)));

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

        AgenteAtendimento agenteMock = new AgenteAtendimento();
        agenteMock.setInicioTurno(LocalTime.parse("11:00:00"));
        agenteMock.setFimTurno(LocalTime.parse("18:00:00"));

        PendenciaRequestDto pendenciaRequestDto = new PendenciaRequestDto(
                "1234",
                "EST01",
                "ENT01",
                LocalDateTime.parse("2026-05-29T12:00:00")
        );

        when(tagService.procurarTagAtiva(pendenciaRequestDto.codigoTag()))
                .thenReturn(tagPcd);

        when(estacaoService.procurarEstacaoAtiva(pendenciaRequestDto.codigoEstacao()))
                .thenReturn(mock(Estacao.class));

        when(entradaService.procurarEntradaAtiva(pendenciaRequestDto.codigoEntrada()))
                .thenReturn(mock(Entrada.class));

        when(agenteService.procurarAgentesDisponiveis(any(Estacao.class), any(LocalTime.class)))
                .thenReturn(new ArrayList<>(List.of(agenteMock)));

        when(repository.save(any(PendenciaAtendimento.class)))
                .thenAnswer(invocation -> invocation.<PendenciaAtendimento>getArgument(0));

        ArgumentCaptor<PendenciaAtendimento> captor = ArgumentCaptor.forClass(PendenciaAtendimento.class);

        // ACT
        service.criarPendencia(pendenciaRequestDto);

        // ASSERT
        verify(repository).save(captor.capture());
        PendenciaAtendimento pendenciaCapturada = captor.getValue();
        assertEquals(agenteMock, pendenciaCapturada.getAgente());
    }

    // -------------------------------------------------------------------------
    // Testes de confirmação e remoção de pendência
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("CT-04 - Agente confirma atendimento: pendência é marcada como CONCLUIDO e excluída do banco após confirmação")
    void deveConfirmarAtendimento() {
        // Arrange
        when(repository.findById("PEND-001"))
                .thenReturn(Optional.of(pendenciaAtiva));

        // Act
        service.confirmarAtendimento("PEND-001");

        // Assert
        assertEquals(StatusAtendimento.CONCLUIDO, pendenciaAtiva.getStatusAtendimento(),
                "Após confirmação, o status da pendência deve ser CONCLUIDO.");

        verify(repository, times(1)).findById("PEND-001");
    }

    @Test
    @DisplayName("CT-04 - Excluir pendência após confirmação")
    void excluirPendenciaAposConfirmacao() {
        // Arrange
        when(repository.findById("PEND-001"))
                .thenReturn(Optional.of(pendenciaAtiva));

        // Act
        service.removerPendencia("PEND-001");

        // Assert
        verify(repository, times(1)).delete(pendenciaAtiva);

        assertNull(pendenciaAtiva.getAgente(),
                "O vínculo com o agente deve ser removido antes da exclusão.");
        assertNull(pendenciaAtiva.getPcdAtendido(),
                "O vínculo com o PcD deve ser removido antes da exclusão.");
        assertNull(pendenciaAtiva.getEstacao(),
                "O vínculo com a estação deve ser removido antes da exclusão.");
        assertNull(pendenciaAtiva.getEntrada(),
                "O vínculo com a entrada deve ser removido antes da exclusão.");

        assertFalse(agente.getPendencias().contains(pendenciaAtiva),
                "A pendência deve ser removida da lista do agente.");
        assertFalse(pcd.getPendencias().contains(pendenciaAtiva),
                "A pendência deve ser removida da lista do PcD.");
    }

    @Test
    @DisplayName("CT-04 - Tentativa de confirmar pendência inexistente lança EntidadeNaoEncontradaException")
    void deveLancarExcecaoAoConfirmarPendenciaInexistente() {
        // Arrange
        when(repository.findById("ID-INVALIDO"))
                .thenReturn(Optional.empty());

        // Act
        EntidadeNaoEncontradaException excecao = assertThrows(
                EntidadeNaoEncontradaException.class,
                () -> service.confirmarAtendimento("ID-INVALIDO"),
                "Deve lançar EntidadeNaoEncontradaException para pendência não encontrada."
        );

        // Assert
        assertNotNull(excecao.getMessage(),
                "A exceção deve conter mensagem descritiva.");
        assertTrue(excecao.getMessage().contains("ID-INVALIDO"),
                "A mensagem deve identificar o id que originou a falha.");

        verify(repository, times(1)).findById("ID-INVALIDO");
        verify(repository, never()).delete(any(PendenciaAtendimento.class));
    }
}
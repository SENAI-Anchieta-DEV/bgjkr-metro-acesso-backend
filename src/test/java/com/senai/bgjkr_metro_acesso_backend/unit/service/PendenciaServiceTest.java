package com.senai.bgjkr_metro_acesso_backend.unit.service;

import com.senai.bgjkr_metro_acesso_backend.application.service.AgenteService;
import com.senai.bgjkr_metro_acesso_backend.application.service.EntradaService;
import com.senai.bgjkr_metro_acesso_backend.application.service.EstacaoService;
import com.senai.bgjkr_metro_acesso_backend.application.service.PendenciaService;
import com.senai.bgjkr_metro_acesso_backend.application.service.TagService;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.PendenciaAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.PendenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @InjectMocks
    private PendenciaService pendenciaService;

    private AgenteAtendimento agente;
    private Estacao estacao;
    private Entrada entrada;
    private UsuarioPcd pcd;
    private PendenciaAtendimento pendenciaAtiva;

    @BeforeEach
    void setUp() {
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

    @Test
    @DisplayName("CT-04 - Agente confirma atendimento: pendência é marcada como CONCLUIDO e excluída do banco após confirmação")
    void deveConfirmarAtendimentoEExcluirPendenciaAposConfirmacao() {
        // Arrange – confirmar atendimento
        when(repository.findById("PEND-001"))
                .thenReturn(Optional.of(pendenciaAtiva));

        // Act – agente pressiona botão de confirmação
        pendenciaService.confirmarAtendimento("PEND-001");

        // Assert – pendência marcada como CONCLUIDO (removida do painel)
        assertEquals(StatusAtendimento.CONCLUIDO, pendenciaAtiva.getStatusAtendimento(),
                "Após confirmação, o status da pendência deve ser CONCLUIDO.");

        verify(repository, times(1)).findById("PEND-001");

        // Arrange – exclusão do banco após confirmação
        // Reutiliza o mesmo findById para o fluxo de remoção
        when(repository.findById("PEND-001"))
                .thenReturn(Optional.of(pendenciaAtiva));

        //Act – exclusão da pendência confirmada
        pendenciaService.removerPendencia("PEND-001");

        // Assert – pendência excluída do banco e vínculos desfeitos
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

        // Act & Assert
        EntidadeNaoEncontradaException excecao = assertThrows(
                EntidadeNaoEncontradaException.class,
                () -> pendenciaService.confirmarAtendimento("ID-INVALIDO"),
                "Deve lançar EntidadeNaoEncontradaException para pendência não encontrada."
        );

        assertNotNull(excecao.getMessage(),
                "A exceção deve conter mensagem descritiva.");
        assertTrue(excecao.getMessage().contains("ID-INVALIDO"),
                "A mensagem deve identificar o id que originou a falha.");

        verify(repository, times(1)).findById("ID-INVALIDO");
        verify(repository, never()).delete(any(PendenciaAtendimento.class));
    }
}
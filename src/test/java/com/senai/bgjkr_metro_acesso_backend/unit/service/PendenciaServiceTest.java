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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private PendenciaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================
    // RF07 - Exclusão de Pendência
    // =========================

    @Test
    @DisplayName("Deve remover pendência quando o ID existir")
    void deveRemoverPendenciaQuandoIdExistir() {

        // Arrange
        UsuarioPcd pcd = UsuarioPcd.builder()
                .pendencias(new ArrayList<>())
                .build();

        AgenteAtendimento agente = AgenteAtendimento.builder()
                .pendencias(new ArrayList<>())
                .build();

        Estacao estacao = Estacao.builder()
                .pendencias(new ArrayList<>())
                .build();

        Entrada entrada = Entrada.builder()
                .pendencias(new ArrayList<>())
                .build();

        PendenciaAtendimento pendencia = PendenciaAtendimento.builder()
                .id("1")
                .pcdAtendido(pcd)
                .agente(agente)
                .estacao(estacao)
                .entrada(entrada)
                .build();

        pcd.getPendencias().add(pendencia);
        agente.getPendencias().add(pendencia);
        estacao.getPendencias().add(pendencia);
        entrada.getPendencias().add(pendencia);

        when(repository.findById("1"))
                .thenReturn(Optional.of(pendencia));

        // Act
        service.removerPendencia("1");

        // Assert
        verify(repository, times(1)).delete(pendencia);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a pendência não existir")
    void deveLancarExcecaoQuandoPendenciaNaoExistir() {

        // Arrange
        when(repository.findById("999"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                EntidadeNaoEncontradaException.class,
                () -> service.removerPendencia("999")
        );

        verify(repository, never()).delete(any());
    }

    // =========================
    // RF20 / RF21 - Confirmação de Atendimento
    // =========================

    @Test
    @DisplayName("Deve confirmar atendimento da pendência")
    void deveConfirmarAtendimento() {

        // Arrange
        PendenciaAtendimento pendencia = PendenciaAtendimento.builder()
                .id("1")
                .statusAtendimento(StatusAtendimento.PENDENTE)
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(pendencia));

        // Act
        service.confirmarAtendimento("1");

        // Assert
        assertEquals(
                StatusAtendimento.CONCLUIDO,
                pendencia.getStatusAtendimento()
        );
    }
}
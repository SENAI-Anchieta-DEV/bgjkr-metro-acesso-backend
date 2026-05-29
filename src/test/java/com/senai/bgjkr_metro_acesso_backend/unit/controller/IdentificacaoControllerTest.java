package com.senai.bgjkr_metro_acesso_backend.unit.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.IdentificacaoService;
import com.senai.bgjkr_metro_acesso_backend.interface_ui.controller.IdentificacaoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class IdentificacaoControllerTest {
    @Mock
    private IdentificacaoService service;

    private IdentificacaoController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new IdentificacaoController(service);
    }

    @Test
    @DisplayName("Deve delegar identificação ao service")
    void deveDelegarIdentificacaoAoService() {
        // ARRANGE
        IdentificacaoDto dto = new IdentificacaoDto(
                "1234",
                "AA:BB:CC:DD:EE:FF",
                true
        );
        PendenciaResponseDto responseEsperada = mock(PendenciaResponseDto.class);

        when(service.solicitarPendencia(dto)).thenReturn(responseEsperada);

        // Act
        ResponseEntity<PendenciaResponseDto> response = controller.simularIdentificacao(dto);

        // Assert
        assertEquals(responseEsperada, response.getBody());

        verify(service, times(1)).solicitarPendencia(dto);
    }
}
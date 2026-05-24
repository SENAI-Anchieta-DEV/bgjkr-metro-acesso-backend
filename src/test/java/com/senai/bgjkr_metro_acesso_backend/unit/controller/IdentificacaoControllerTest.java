package com.senai.bgjkr_metro_acesso_backend.unit.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.IdentificacaoService;
import com.senai.bgjkr_metro_acesso_backend.interface_ui.controller.IdentificacaoController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IdentificacaoControllerTest {

    @Test
    @DisplayName("Deve delegar identificação ao service")
    void deveDelegarIdentificacaoAoService() {

        // Arrange
        IdentificacaoService service =
                mock(IdentificacaoService.class);

        IdentificacaoController controller =
                new IdentificacaoController(service);

        IdentificacaoDto dto =
                new IdentificacaoDto(
                        "TAG123",
                        "BSSID123",
                        true
                );

        PendenciaResponseDto responseEsperada =
                mock(PendenciaResponseDto.class);

        when(service.solicitarPendencia(dto))
                .thenReturn(responseEsperada);

        // Act
        ResponseEntity<PendenciaResponseDto> response =
                controller.simularIdentificacao(dto);

        // Assert
        assertEquals(responseEsperada, response.getBody());

        verify(service, times(1))
                .solicitarPendencia(dto);
    }
}
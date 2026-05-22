package com.senai.bgjkr_metro_acesso_backend.unit.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.PendenciaService;
import com.senai.bgjkr_metro_acesso_backend.interface_ui.controller.PendenciaController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PendenciaControllerTest {

    @Test
    @DisplayName("Deve delegar listagem de pendências ativas ao serviço")
    void deveDelegarListagemDePendenciasAtivasAoServico() {
        // Arrange
        PendenciaService service = mock(PendenciaService.class);
        PendenciaController controller = new PendenciaController(service);
        List<PendenciaResponseDto> listaEsperada = List.of();
        when(service.listarPendenciasAtivas()).thenReturn(listaEsperada);

        // Act
        ResponseEntity<List<PendenciaResponseDto>> response = controller.listarPendenciasAtivas();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(listaEsperada, response.getBody());
        verify(service, times(1)).listarPendenciasAtivas();
    }

    @Test
    @DisplayName("Deve delegar listagem de pendências do agente ao serviço")
    void deveDelegarListagemDePendenciasDoAgenteAoServico() {
        // Arrange
        PendenciaService service = mock(PendenciaService.class);
        PendenciaController controller = new PendenciaController(service);
        String email = "agente@email.com";
        List<PendenciaResponseDto> listaEsperada = List.of();
        when(service.listarPendenciasDoAgente(email)).thenReturn(listaEsperada);

        // Act
        ResponseEntity<List<PendenciaResponseDto>> response = controller.listarPendenciasDoAgente(email);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(listaEsperada, response.getBody());
        verify(service, times(1)).listarPendenciasDoAgente(email);
    }

    @Test
    @DisplayName("Deve delegar listagem de pendências por estação ao serviço")
    void deveDelegarListagemDePendenciasPorEstacaoAoServico() {
        // Arrange
        PendenciaService service = mock(PendenciaService.class);
        PendenciaController controller = new PendenciaController(service);
        String codigoEstacao = "EST001";
        List<PendenciaResponseDto> listaEsperada = List.of();
        when(service.listarPendenciasPorEstacao(codigoEstacao)).thenReturn(listaEsperada);

        // Act
        ResponseEntity<List<PendenciaResponseDto>> response = controller.listarPendenciasPorEstacao(codigoEstacao);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(listaEsperada, response.getBody());
        verify(service, times(1)).listarPendenciasPorEstacao(codigoEstacao);
    }

    @Test
    @DisplayName("Deve delegar remoção de pendência ao serviço")
    void deveDelegarRemocaoDePendenciaAoServico() {
        // Arrange
        PendenciaService service = mock(PendenciaService.class);
        PendenciaController controller = new PendenciaController(service);

        // Act
        ResponseEntity<Void> response = controller.removerPendencia("1");

        // Assert
        assertEquals(204, response.getStatusCode().value());
        verify(service, times(1)).removerPendencia("1");
    }
}
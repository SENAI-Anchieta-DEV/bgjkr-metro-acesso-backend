package com.senai.bgjkr_metro_acesso_backend.integration.unit.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.FormularioService;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import com.senai.bgjkr_metro_acesso_backend.interface_ui.controller.FormularioController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MqttSubscriberTest {
    @Test
    @DisplayName("Deve visualizar a leitura da tag na entrada do PcD")
    void deveVisualizarLeituraDaTag() {
        // Arrange
        FormularioService service = mock(FormularioService.class);
        FormularioController controller = new FormularioController(service);
        MultipartFile comprovacao = mock(MultipartFile.class);
        FormSolicitacaoRequestDto dto = new FormSolicitacaoRequestDto(
                "Carlos Ribeiro Fonseca",
                "carlos.fonseca@gmail.com",
                "carlos123",
                Set.of(TipoDeficiencia.VISUAL),
                true,
                comprovacao
        );
        FormSolicitacaoResponseDto responseEsperada = new FormSolicitacaoResponseDto(
                UUID.randomUUID().toString(),
                "Carlos Ribeiro Fonseca",
                "carlos.fonseca@gmail.com",
                "carlos123",
                Set.of(TipoDeficiencia.VISUAL),
                true,
                UUID.randomUUID().toString()
        );
        when(service.registrarFormulario(dto)).thenReturn(responseEsperada);

        // Act
        FormSolicitacaoResponseDto response = controller.registrarFormulario(dto).getBody();

        // Assert
        assertEquals(responseEsperada, response);
        verify(service, times(1)).registrarFormulario(dto);
    }

    @Test
    @DisplayName("Deve delegar busca por id ao serviço")
    void deveDelegarBuscaPorIdAoServico() {
        // Arrange
        FormularioService service = mock(FormularioService.class);
        FormularioController controller = new FormularioController(service);
        FormularioResponseDTO responseEsperada = new FormularioResponseDTO(1L, "Teclado", new BigDecimal("250.00"), 7);
        when(service.buscarPorId(1L)).thenReturn(responseEsperada);

        // Act
        FormularioResponseDTO response = controller.buscarPorId(1L);

        // Assert
        assertEquals(responseEsperada, response);
        verify(service, times(1)).buscarPorId(1L);
    }
}
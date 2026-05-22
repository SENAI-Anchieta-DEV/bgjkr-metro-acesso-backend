package com.senai.bgjkr_metro_acesso_backend.integration.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.PendenciaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PendenciaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PendenciaService service;

    // =========================
    // RF22 - Listagem de Pendências Ativas
    // =========================

    @Test
    @DisplayName("Deve listar pendências ativas")
    void deveListarPendenciasAtivas() throws Exception {

        // Arrange
        when(service.listarPendenciasAtivas())
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/api/pendencia-atendimento"))
                .andExpect(status().isOk());
    }

    // =========================
    // RF23 - Listagem de Pendências do Agente
    // =========================

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("Deve listar pendências do agente")
    void deveListarPendenciasDoAgente() throws Exception {

        // Arrange
        when(service.listarPendenciasDoAgente("agente@email.com"))
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(
                        get("/api/pendencia-atendimento/agente/agente@email.com")
                )
                .andExpect(status().isOk());
    }

    // =========================
    // RF07 - Remoção de Pendência
    // =========================

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("Deve remover pendência")
    void deveRemoverPendencia() throws Exception {

        // Act + Assert
        mockMvc.perform(delete("/api/pendencia-atendimento/1"))
                .andExpect(status().isNoContent());
    }

    // =========================
// RF24 - Listagem de Pendências por Estação
// =========================

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    @DisplayName("Deve listar pendências por estação")
    void deveListarPendenciasPorEstacao() throws Exception {

        // Arrange
        when(service.listarPendenciasPorEstacao("EST001"))
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(
                        get("/api/pendencia-atendimento/estacao/EST001")
                )
                .andExpect(status().isOk());
    }
}
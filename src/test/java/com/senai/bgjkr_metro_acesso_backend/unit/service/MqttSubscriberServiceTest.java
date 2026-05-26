package com.senai.bgjkr_metro_acesso_backend.unit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.IdentificacaoService;
import com.senai.bgjkr_metro_acesso_backend.infrastructure.mqtt.MqttSubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MqttSubscriberServiceTest {
    @Mock
    private IdentificacaoService identificacaoService;

    @Spy
    private ObjectMapper objectMapper;

    private MqttSubscriberService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.service = new MqttSubscriberService(identificacaoService, objectMapper);
    }

    @Test
    @DisplayName("CT-13 Deve ler payload da tag em DTO")
    void deveLerPayloadDaTagEmDto() throws JsonProcessingException {
        // ARRANGE
        String payload = "{\"codigoTag\": \"1234\",\"bssid\": \"AA:BB:CC:DD:EE:FF\",\"tipo\": true}";
        IdentificacaoDto dtoEsperado = new IdentificacaoDto(
                "1234",
                "AA:BB:CC:DD:EE:FF",
                true
        );

        // ACT
        IdentificacaoDto payloadConvertido = service.converterPayload(payload);

        // ASSERT
        assertEquals(dtoEsperado, payloadConvertido);
    }
}

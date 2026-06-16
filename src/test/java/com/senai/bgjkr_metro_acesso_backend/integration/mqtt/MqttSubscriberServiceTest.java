package com.senai.bgjkr_metro_acesso_backend.integration.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.IdentificacaoService;
import com.senai.bgjkr_metro_acesso_backend.infrastructure.mqtt.MqttSubscriberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MqttSubscriberServiceTest {
    @Test
    @DisplayName("Deve processar payload")
    void deveProcessarPayload() throws Exception {
        IdentificacaoService service = mock(IdentificacaoService.class);
        ObjectMapper mapper = new ObjectMapper();

        MqttSubscriberService subscriber = new MqttSubscriberService(service, mapper);

        String payload = """
                {
                    "codigoTag":"TAG123",
                    "bssid":"BSSID123"
                }
                """;

        subscriber.processMessage(payload);

        verify(service).solicitarPendencia(any());
    }

    @Test
    @DisplayName("RF14 - Deve processar payload e encaminhar identificação corretamente")
    void deveProcessarPayloadCompleto() throws Exception {

        // ARRANGE
        IdentificacaoService service = mock(IdentificacaoService.class);
        ObjectMapper mapper = new ObjectMapper();

        MqttSubscriberService subscriber = new MqttSubscriberService(service, mapper);

        String payload = """
        {
            "codigoTag":"TAG123",
            "bssid":"BSSID123"
        }
        """;

        // ACT
        subscriber.processMessage(payload);

        // ASSERT
        verify(service, times(1)).solicitarPendencia(any(IdentificacaoDto.class));
    }
}
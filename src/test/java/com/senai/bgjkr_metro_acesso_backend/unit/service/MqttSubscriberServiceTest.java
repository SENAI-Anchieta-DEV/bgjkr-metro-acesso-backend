package com.senai.bgjkr_metro_acesso_backend.unit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.IdentificacaoService;
import com.senai.bgjkr_metro_acesso_backend.infrastructure.mqtt.MqttSubscriberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    @DisplayName("Deve converter payload MQTT em DTO corretamente")
    void deveConverterPayloadEmDto() throws Exception {
        // ARRANGE
        String payload = """
                {
                    "codigoTag": "1234",
                    "bssid": "AA:BB:CC:DD:EE:FF"
                }
                """;
        ArgumentCaptor<IdentificacaoDto> captor = ArgumentCaptor.forClass(IdentificacaoDto.class);

        // ACT
        service.processMessage(payload);

        // ASSERT
        verify(identificacaoService).solicitarPendencia(captor.capture());
        IdentificacaoDto dtoCapturado = captor.getValue();

        assertEquals("1234", dtoCapturado.codigoTag());
        assertEquals("AA:BB:CC:DD:EE:FF", dtoCapturado.bssid());
    }

    @Test
    @DisplayName("Deve autenticar antes de solicitar pendência")
    void deveAutenticarAntesDeSolicitarPendencia() throws Exception {
        String payload = "{}";

        doAnswer(_ -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            assertNotNull(auth);
            assertEquals("mqtt-system", auth.getName());

            return null;
        }).when(identificacaoService)
                .solicitarPendencia(any());

        service.processMessage(payload);
    }

    @Test
    @DisplayName("Deve delegar IdentificacaoService para solicitar pendência")
    void deveDelegarIdentificacaoService() throws Exception {
        // ARRANGE
        String payload = """
                {
                    "codigoTag": "1234",
                    "bssid": "AA:BB:CC:DD:EE:FF"
                }
                """;

        // ACT
        service.processMessage(payload);

        // ASSERT
        verify(identificacaoService).solicitarPendencia(any(IdentificacaoDto.class));
    }
}

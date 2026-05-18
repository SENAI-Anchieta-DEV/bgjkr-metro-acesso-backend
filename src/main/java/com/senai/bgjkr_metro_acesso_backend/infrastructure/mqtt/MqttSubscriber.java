package com.senai.bgjkr_metro_acesso_backend.infrastructure.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.IdentificacaoService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j // log padronizado do lombok
public class MqttSubscriber {
    private final IdentificacaoService service;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        try {
            MqttClient client = new MqttClient(
                    "tcp://localhost:1883",
                    MqttClient.generateClientId()
            );

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            client.connect(options);

            log.info("MQTT conectado");

            client.subscribe("metro/tag/entrada", (topic, msg) -> {
                String payload = new String(msg.getPayload());

                log.info("MQTT recebido: {}", payload);

                try {
                    IdentificacaoDto identificacao = objectMapper.readValue(payload, IdentificacaoDto.class);

                    Authentication auth =
                            new UsernamePasswordAuthenticationToken(
                                    "mqtt-system",
                                    null,
                                    List.of(new SimpleGrantedAuthority(
                                            "ROLE_ADMINISTRADOR"
                                    ))
                            );

                    SecurityContextHolder.getContext()
                            .setAuthentication(auth);

                    service.solicitarPendencia(identificacao);
                } catch (JsonProcessingException e) {
                    log.error("Erro ao converter JSON MQTT", e);
                } catch (Exception e) {
                    log.error("Erro processando evento MQTT", e);
                } finally {
                    SecurityContextHolder.clearContext();
                }
            });
        } catch (Exception e) {
            log.error("Erro ao iniciar MQTT", e);
        }
    }
}
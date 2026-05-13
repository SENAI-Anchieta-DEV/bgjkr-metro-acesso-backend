package com.senai.bgjkr_metro_acesso_backend.infrastructure.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.IdentificacaoService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MqttSubscriber {
    private final IdentificacaoService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

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

            System.out.println(" MQTT conectado (Render)");

            client.subscribe("metro/tag/entrada", (topic, msg) -> {
                String payload = new String(msg.getPayload());

                System.out.println(" MQTT recebido:");
                System.out.println(payload);

                try {
                    IdentificacaoDto identificacao = objectMapper.readValue(payload, IdentificacaoDto.class);
                    service.processarEvento(identificacao);
                } catch (Exception e) {
                    System.out.println(" Erro ao converter JSON");
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.out.println(" Erro MQTT");
            e.printStackTrace();
        }
    }
}
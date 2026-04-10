package com.senai.bgjkr_metro_acesso_backend.infrastructure.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagEventoDTO;
import com.senai.bgjkr_metro_acesso_backend.application.service.TagEventoService;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

@Component
public class MqttSubscriber {

    private final TagEventoService service;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MqttSubscriber(TagEventoService service) {
        this.service = service;
    }

    @PostConstruct
    public void init() {
        try {
            MqttClient client = new MqttClient(
                    "tcp://broker.hivemq.com:1883",
                    MqttClient.generateClientId()
            );

            client.connect();

            client.subscribe("metro/tag/entrada", (topic, msg) -> {

                String payload = new String(msg.getPayload());

                System.out.println("📡 MQTT recebido:");
                System.out.println(payload);

                try {
                    TagEventoDTO evento =
                            objectMapper.readValue(payload, TagEventoDTO.class);

                    service.processarEvento(evento);

                } catch (Exception e) {
                    System.out.println("❌ Erro ao converter JSON");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
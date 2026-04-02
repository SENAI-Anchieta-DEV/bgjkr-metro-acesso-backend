package com.senai.bgjkr_metro_acesso_backend.infrastructure.mqtt;

import com.senai.bgjkr_metro_acesso_backend.application.service.EventoService;

public class MqttSubscriber {

    private final EventoService eventoService;

    public MqttSubscriber(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    // Simulação de callback MQTT
    public void mensagemRecebida(String topico, String payload) {
        System.out.println("Mensagem recebida: " + payload);

        eventoService.processarEvento(payload);
    }
}

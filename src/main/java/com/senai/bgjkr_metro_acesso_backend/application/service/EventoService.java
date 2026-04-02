package com.senai.bgjkr_metro_acesso_backend.application.service;

import org.springframework.stereotype.Service;

@Service
public class EventoService {
    private String ultimoEvento; // memória (MVP sem banco)

    public String processarEvento(String payload) {
        System.out.println("Processando evento...");

        // exemplo: payload = MAC do ESP32
        ultimoEvento = "Tag detectada: " + payload;

        return ("Evento registrado: " + ultimoEvento);
    }

    public String getUltimoEvento() {
        return ultimoEvento;
    }
}

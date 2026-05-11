package com.senai.bgjkr_metro_acesso_backend.application.service;

import org.springframework.stereotype.Service;

@Service
public class IdentificacaoService {
    private String ultimoEvento;

    public String processarEvento(String payload) {
        System.out.println("Processando evento...");
        ultimoEvento = "Tag detectada: " + payload;
        return ("Evento registrado: " + ultimoEvento);
    }

    public String getUltimoEvento() {
        return ultimoEvento;
    }
}

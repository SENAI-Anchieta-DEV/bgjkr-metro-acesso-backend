package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagEventoDTO;
import org.springframework.stereotype.Service;

@Service
public class TagEventoService {

    private TagEventoDTO ultimoEvento;

    public void processarEvento(TagEventoDTO evento) {
        System.out.println("🚇 Evento recebido:");

        System.out.println("Tag: " + evento.getCodigoTag());
        System.out.println("Estação: " + evento.getCodigoEstacao());
        System.out.println("Porta: " + evento.getPorta());

        this.ultimoEvento = evento;
    }

    public TagEventoDTO getUltimoEvento() {
        return ultimoEvento;
    }
}
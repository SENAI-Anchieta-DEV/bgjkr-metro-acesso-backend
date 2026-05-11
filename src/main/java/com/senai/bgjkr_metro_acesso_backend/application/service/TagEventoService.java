package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.IdentificacaoDto;
import org.springframework.stereotype.Service;

@Service
public class TagEventoService {

    public void processarEvento(IdentificacaoDto evento) {
        System.out.println("Evento recebido no service:");
        System.out.println(evento.codigoTag());
    }
}
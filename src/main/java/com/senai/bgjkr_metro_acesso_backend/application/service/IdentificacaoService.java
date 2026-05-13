package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.IdentificacaoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentificacaoService {
    private final PendenciaService pendenciaService;

    public void processarEvento(IdentificacaoDto identificacao) {
        System.out.println("Evento recebido no service:");
        System.out.println(identificacao.codigoTag());

        pendenciaService.criarPendencia(identificacao);
    }
}
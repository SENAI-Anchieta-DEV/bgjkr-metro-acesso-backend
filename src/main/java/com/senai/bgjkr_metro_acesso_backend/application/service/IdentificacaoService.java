package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentificacaoService {
    private final PendenciaService pendenciaService;

    public PendenciaResponseDto solicitarPendencia(IdentificacaoDto identificacao) {
        System.out.println("Evento recebido no service:");
        System.out.println(identificacao.codigoTag());

        return PendenciaResponseDto.fromEntity(pendenciaService.criarPendencia(identificacao));
    }
}
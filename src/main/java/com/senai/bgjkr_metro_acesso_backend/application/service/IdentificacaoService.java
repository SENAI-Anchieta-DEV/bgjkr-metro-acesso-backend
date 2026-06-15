package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IdentificacaoService {
    private final PendenciaService pendenciaService;
    private final EntradaService entradaService;

    @Transactional
    public PendenciaResponseDto solicitarPendencia(IdentificacaoDto identificacao) {
        Entrada entrada = entradaService.procurarEntradaPorBssid(identificacao.bssid());
        String codigoEstacao = entrada.getEstacao().getCodigoEstacao();
        String codigoEntrada = entrada.getCodigoEntrada();
        LocalDateTime dataHora = LocalDateTime.now();

        PendenciaRequestDto pendenciaDto = new PendenciaRequestDto(
                identificacao.codigoTag(),
                codigoEstacao,
                codigoEntrada,
                dataHora
        );

        return pendenciaService.criarPendencia(pendenciaDto);
    }
}
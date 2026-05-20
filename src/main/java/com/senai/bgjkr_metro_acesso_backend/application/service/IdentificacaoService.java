package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.EntradaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IdentificacaoService {
    private final PendenciaService pendenciaService;
    private final EntradaService entradaService;

    public PendenciaResponseDto solicitarPendencia(IdentificacaoDto identificacao) {
        System.out.println("Evento recebido no service:");
        System.out.println(identificacao.codigoTag());

        Entrada entrada = entradaService.procurarEntradaPorBssid(identificacao.bssid());
        String codigoEstacao = entrada.getEstacao().getCodigoEstacao();
        String codigoEntrada = entrada.getCodigoEntrada();
        LocalDateTime dataHora = LocalDateTime.now();

        PendenciaRequestDto pendenciaDto = new PendenciaRequestDto(
                identificacao.codigoTag(),
                codigoEstacao,
                codigoEntrada,
                identificacao.tipo(),
                dataHora
        );

        return pendenciaService.criarPendencia(pendenciaDto);
    }
}
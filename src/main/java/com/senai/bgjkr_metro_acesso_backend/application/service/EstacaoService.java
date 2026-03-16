package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Linha;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.EstacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstacaoService {
    private final EstacaoRepository repository;

    // CREATE
    @Transactional
    public EstacaoResponseDto registrarEstacao(EstacaoRequestDto requestDto) {
        Estacao estacaoRegistrada = repository.findByCodigoEstacao(requestDto.codigoEstacao())
                .map(estacao -> estacao.isAtivo() ? estacao : reativarEstacao(estacao, requestDto))
                .orElseGet(() -> criarEstacao(requestDto));

        return EstacaoResponseDto.fromEntity(repository.save(estacaoRegistrada));
    }
}

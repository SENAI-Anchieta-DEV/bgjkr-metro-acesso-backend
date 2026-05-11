package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Linha;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.EstacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstacaoService {
    private final EstacaoRepository repository;

    // CREATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public EstacaoResponseDto registrarEstacao(EstacaoRequestDto requestDto) {
        Estacao estacaoRegistrada = repository.findByCodigoEstacao(requestDto.codigoEstacao())
                .map(estacao -> estacao.isAtivo() ? estacao : reativarEstacao(estacao, requestDto))
                .orElseGet(() -> criarEstacao(requestDto));

        return EstacaoResponseDto.fromEntity(repository.save(estacaoRegistrada));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<EstacaoResponseDto> listarEstacoesAtivas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(EstacaoResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public EstacaoResponseDto buscarEstacaoAtiva(String codigoEstacao) {
        return EstacaoResponseDto.fromEntity(procurarEstacaoAtiva(codigoEstacao));
    }

    // UPDATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public EstacaoResponseDto atualizarEstacao(String codigoEstacao, EstacaoRequestDto requestDto) {
        Estacao estacaoAtualizada = procurarEstacaoAtiva(codigoEstacao);
        atualizarValores(estacaoAtualizada, requestDto);
        return EstacaoResponseDto.fromEntity(repository.save(estacaoAtualizada));
    }

    // DELETE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void removerEstacao(String codigoEstacao) {
        Estacao estacaoRemovida = procurarEstacaoAtiva(codigoEstacao);
        estacaoRemovida.setAtivo(false);
        estacaoRemovida.getEntradas().forEach(entrada -> entrada.setEstacao(null));
        estacaoRemovida.getEntradas().clear();
        estacaoRemovida.getAgentes().forEach(agente -> agente.setEstacao(null));
        estacaoRemovida.getAgentes().clear();
        repository.save(estacaoRemovida);
    }

    // Funções auxiliares
    protected Estacao procurarEstacaoAtiva(String codigoEstacao) {
        return repository
                .findByCodigoEstacaoAndAtivoTrue(codigoEstacao)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Estacao", "codigoEstacao", codigoEstacao));
    }

    private Estacao reativarEstacao(Estacao estacao, EstacaoRequestDto requestDto) {
        atualizarValores(estacao, requestDto);
        estacao.setAtivo(true);
        return estacao;
    }

    private Estacao criarEstacao(EstacaoRequestDto requestDto) {
        return requestDto.toEntity();
    }

    private void atualizarValores(Estacao estacao, EstacaoRequestDto requestDto) {
        estacao.setNome(requestDto.nome());
        estacao.setCodigoEstacao(requestDto.codigoEstacao());
        estacao.setLinhas(Linha.fromNumeros(requestDto.linhas()));
    }
}

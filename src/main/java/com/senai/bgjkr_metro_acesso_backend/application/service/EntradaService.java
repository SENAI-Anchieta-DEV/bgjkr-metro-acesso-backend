package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.entrada.EntradaRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.entrada.EntradaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.EntradaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EntradaService {
    private final EntradaRepository repository;
    private final EstacaoService estacaoService;

    // CREATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public EntradaResponseDto registrarEntrada(EntradaRequestDto requestDto) {
        Entrada entradaRegistrado = repository.findByCodigoEntrada(requestDto.codigoEntrada())
                .map(entrada -> entrada.isAtivo() ? entrada : reativarEntrada(entrada, requestDto))
                .orElseGet(() -> criarEntrada(requestDto));

        return EntradaResponseDto.fromEntity(repository.save(entradaRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<EntradaResponseDto> listarEntradasAtivas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(EntradaResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public EntradaResponseDto buscarEntradaAtiva(String codigoEntrada) {
        return EntradaResponseDto.fromEntity(procurarEntradaAtiva(codigoEntrada));
    }

    // UPDATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public EntradaResponseDto atualizarEntrada(String codigoEntrada, EntradaRequestDto requestDto) {
        Entrada entradaAtualizado = procurarEntradaAtiva(codigoEntrada);
        atualizarValores(entradaAtualizado, requestDto);
        return EntradaResponseDto.fromEntity(repository.save(entradaAtualizado));
    }

    // DELETE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void removerEntrada(String codigoEntrada) {
        Entrada entradaRemovido = procurarEntradaAtiva(codigoEntrada);
        entradaRemovido.setAtivo(false);

        Estacao estacaoVinculada = entradaRemovido.getEstacao();
        if (estacaoVinculada != null) {
            estacaoVinculada.getEntradas().remove(entradaRemovido);
            entradaRemovido.setEstacao(null);
        }

        repository.save(entradaRemovido);
    }

    // Funções auxiliares
    protected Entrada procurarEntradaAtiva(String codigoEntrada) {
        return repository
                .findByCodigoEntradaAndAtivoTrue(codigoEntrada)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Entrada", "codigoEntrada", codigoEntrada));
    }

    private Entrada reativarEntrada(Entrada entrada, EntradaRequestDto requestDto) {
        atualizarValores(entrada, requestDto);
        entrada.setAtivo(true);
        return entrada;
    }

    private Entrada criarEntrada(EntradaRequestDto requestDto) {
        Estacao estacao = estacaoService.procurarEstacaoAtiva(requestDto.codigoEstacao());
        return requestDto.toEntity(estacao);
    }

    private void atualizarValores(Entrada entrada, EntradaRequestDto requestDto) {
        entrada.setEstacao(estacaoService.procurarEstacaoAtiva(requestDto.codigoEstacao()));
        entrada.setCodigoEntrada(requestDto.codigoEntrada());
    }
}


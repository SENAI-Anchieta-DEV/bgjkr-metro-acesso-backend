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

    // READ
    @Transactional(readOnly = true)
    public List<EstacaoResponseDto> listarEstacoesAtivas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(EstacaoResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public EstacaoResponseDto buscarEstacaoAtiva(String codigoEstacao) {
        return EstacaoResponseDto.fromEntity(procurarEstacaoAtiva(codigoEstacao));
    }

    // UPDATE
    @Transactional
    public EstacaoResponseDto atualizarEstacao(String codigoEstacao, EstacaoRequestDto requestDto) {
        Estacao estacaoAtualizada = procurarEstacaoAtiva(codigoEstacao);
        atualizarValores(estacaoAtualizada, requestDto);
        return EstacaoResponseDto.fromEntity(repository.save(estacaoAtualizada));
    }

    // DELETE
    @Transactional
    public void removerEstacao(String codigoEstacao) {
        Estacao estacaoRemovida = procurarEstacaoAtiva(codigoEstacao);
        estacaoRemovida.setAtivo(false);
        estacaoRemovida.getSensores().forEach(sensor -> sensor.setEstacao(null));
        estacaoRemovida.getSensores().clear();
        estacaoRemovida.getAgentes().forEach(agente -> agente.setEstacao(null));
        estacaoRemovida.getAgentes().clear();
        repository.save(estacaoRemovida);
    }

    // Funções auxiliares
    protected Estacao procurarEstacaoAtiva(String codigoEstacao) {
        return repository
                .findByCodigoEstacaoAndAtivoTrue(codigoEstacao)
                .orElseThrow(() -> new RuntimeException("Entidade não encontrada.")); // Exception específica em futura feature
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

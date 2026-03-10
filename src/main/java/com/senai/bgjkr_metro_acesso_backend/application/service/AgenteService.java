package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.AgenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgenteService {
    private final AgenteRepository repository;
    private final EstacaoService estacaoService;

    // CREATE
    @Transactional
    public AgenteResponseDto registrarAgente(AgenteRequestDto requestDto) {
        AgenteAtendimento agenteRegistrado = repository.findByEmail(requestDto.email())
                .map(agente -> agente.isAtivo() ? agente : reativarAgente(agente, requestDto))
                .orElseGet(() -> criarAgente(requestDto));

        return AgenteResponseDto.fromEntity(repository.save(agenteRegistrado));
    }

    // Funções auxiliares
    private void atualizarValores(AgenteAtendimento agente, AgenteRequestDto requestDto) {
        agente.setNome(requestDto.nome());
        agente.setEmail(requestDto.email());
        agente.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        agente.setEstacao(estacaoService.procurarEstacaoAtiva(requestDto.estacaoId()));
        agente.setInicioTurno(requestDto.inicioTurno());
        agente.setFimTurno(requestDto.fimTurno());
    }

    private AgenteAtendimento reativarAgente(AgenteAtendimento agente, AgenteRequestDto requestDto) {
        atualizarValores(agente, requestDto);

        agente.setAtivo(true);

        return agente;
    }

    private AgenteAtendimento procurarAgenteAtivo(String email) {
        return repository
                .findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new RuntimeException("Entidade não encontrada.")); // Exception específica em futura feature
    }

    private AgenteAtendimento criarAgente(AgenteRequestDto requestDto) {
        Estacao estacao = estacaoService.procurarEstacaoAtiva(requestDto.estacaoId());
        AgenteAtendimento agente = requestDto.toEntity(estacao);
        agente.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        return agente;
    }
}

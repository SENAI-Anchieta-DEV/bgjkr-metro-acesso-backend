package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.AgenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenteService {
    private final AgenteRepository repository;
    private final EstacaoService estacaoService;

    // CREATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public AgenteResponseDto registrarAgente(AgenteRequestDto requestDto) {
        AgenteAtendimento agenteRegistrado = repository.findByEmail(requestDto.email())
                .map(agente -> agente.isAtivo() ? agente : reativarAgente(agente, requestDto))
                .orElseGet(() -> criarAgente(requestDto));

        return AgenteResponseDto.fromEntity(repository.save(agenteRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<AgenteResponseDto> listarAgentesAtivos() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(AgenteResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR') or authentication.name == #email")
    public AgenteResponseDto buscarAgenteAtivo(String email) {
        return AgenteResponseDto.fromEntity(procurarAgenteAtivo(email));
    }

    // UPDATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR') or authentication.name == #email")
    public AgenteResponseDto atualizarAgente(String email, AgenteRequestDto requestDto) {
        AgenteAtendimento agenteAtualizado = procurarAgenteAtivo(email);
        atualizarValores(agenteAtualizado, requestDto);
        return AgenteResponseDto.fromEntity(repository.save(agenteAtualizado));
    }

    // DELETE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void removerAgente(String email) {
        AgenteAtendimento agenteRemovido = procurarAgenteAtivo(email);
        agenteRemovido.setAtivo(false);

        Estacao estacaoVinculada = agenteRemovido.getEstacao();
        if (estacaoVinculada != null) {
            estacaoVinculada.getAgentes().remove(agenteRemovido);
            agenteRemovido.setEstacao(null);
        }

        repository.save(agenteRemovido);
    }

    // Funções auxiliares
    private AgenteAtendimento procurarAgenteAtivo(String email) {
        return repository
                .findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("AgenteAtendimento", "email", email));
    }

    private AgenteAtendimento reativarAgente(AgenteAtendimento agente, AgenteRequestDto requestDto) {
        atualizarValores(agente, requestDto);
        agente.setAtivo(true);
        return agente;
    }

    private AgenteAtendimento criarAgente(AgenteRequestDto requestDto) {
        Estacao estacao = estacaoService.procurarEstacaoAtiva(requestDto.codigoEstacao());
        AgenteAtendimento agente = requestDto.toEntity(estacao);
        agente.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        return agente;
    }

    private void atualizarValores(AgenteAtendimento agente, AgenteRequestDto requestDto) {
        agente.setNome(requestDto.nome());
        agente.setEmail(requestDto.email());
        agente.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        agente.setEstacao(estacaoService.procurarEstacaoAtiva(requestDto.codigoEstacao()));
        agente.setInicioTurno(requestDto.inicioTurno());
        agente.setFimTurno(requestDto.fimTurno());
    }
}

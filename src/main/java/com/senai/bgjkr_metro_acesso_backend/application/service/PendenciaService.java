package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.PendenciaAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.pendencia_atendimento.AgenteIndisponivelParaAtendimentoException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.PendenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PendenciaService {
    private final PendenciaRepository repository;
    private final EstacaoService estacaoService;
    private final TagService tagService;
    private final EntradaService entradaService;
    private final AgenteService agenteService;

    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public PendenciaResponseDto criarPendencia(PendenciaRequestDto dto) {
        TagPcd tag = tagService.procurarTagAtiva(dto.codigoTag());
        UsuarioPcd pcd = tag.getUsuarioPcd();
        Estacao estacao = estacaoService.procurarEstacaoAtiva(dto.codigoEstacao());
        Entrada entrada = entradaService.procurarEntradaAtiva(dto.codigoEntrada());
        LocalDateTime dataHora = dto.dataHora();
        LocalTime horario = dataHora.toLocalTime();

        List<AgenteAtendimento> agentesDisponiveis = agenteService.procurarAgentesDisponiveis(estacao, horario);
        if (agentesDisponiveis.isEmpty()) {
            System.out.println(horario);
            System.out.println(agenteService.listarAgentesAtivos().getFirst().inicioTurno());
            System.out.println(agenteService.listarAgentesAtivos().getFirst().fimTurno());
            throw new AgenteIndisponivelParaAtendimentoException();
        }
        Collections.shuffle(agentesDisponiveis);
        AgenteAtendimento agente = agentesDisponiveis.getFirst();

        PendenciaAtendimento pendencia = PendenciaAtendimento.builder()
                .pcdAtendido(pcd)
                .agente(agente)
                .estacao(estacao)
                .entrada(entrada)
                .dataHora(dataHora)
                .statusAtendimento(StatusAtendimento.PENDENTE)
                .ativo(true)
                .build();

        return PendenciaResponseDto.fromEntity(repository.save(pendencia));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR') or authentication.name == #email")
    public void confirmarAtendimento(String id) {
        procurarPendenciaAtiva(id).setStatusAtendimento(StatusAtendimento.CONCLUIDO);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<PendenciaResponseDto> listarPendenciasAtivas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(PendenciaResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR') or authentication.name == #email")
    public List<PendenciaResponseDto> listarPendenciasDoAgente(String email) {
        AgenteAtendimento agente = agenteService.procurarAgenteAtivo(email);
        return repository
                .findAllByAgenteAndAtivoTrue(agente)
                .stream()
                .map(PendenciaResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<PendenciaResponseDto> listarPendenciasPorEstacao(String codigoEstacao) {
        Estacao estacao = estacaoService.procurarEstacaoAtiva(codigoEstacao);
        return repository
                .findAllByEstacaoAndAtivoTrue(estacao)
                .stream()
                .map(PendenciaResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void removerPendencia(String id) {
        PendenciaAtendimento pendenciaRemovida = procurarPendenciaAtiva(id);

        UsuarioPcd pcdVinculado = pendenciaRemovida.getPcdAtendido();
        pcdVinculado.getPendencias().remove(pendenciaRemovida);
        pendenciaRemovida.setPcdAtendido(null);

        AgenteAtendimento agenteVinculado = pendenciaRemovida.getAgente();
        agenteVinculado.getPendencias().remove(pendenciaRemovida);
        pendenciaRemovida.setAgente(null);

        Estacao estacaoVinculada = pendenciaRemovida.getEstacao();
        estacaoVinculada.getPendencias().remove(pendenciaRemovida);
        pendenciaRemovida.setEstacao(null);

        Entrada entradaVinculada = pendenciaRemovida.getEntrada();
        entradaVinculada.getPendencias().remove(pendenciaRemovida);
        pendenciaRemovida.setEntrada(null);

        repository.delete(pendenciaRemovida);
    }

    protected PendenciaAtendimento procurarPendenciaAtiva(String id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("PendenciaAtendimento", "id", id));
    }
}
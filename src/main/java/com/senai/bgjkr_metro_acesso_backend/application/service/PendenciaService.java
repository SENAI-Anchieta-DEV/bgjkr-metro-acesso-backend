package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.PendenciaAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.pendencia.AgenteIndisponivelParaAtendimentoException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.PendenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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

    public void criarPendencia(IdentificacaoDto dto) {
        TagPcd tag = tagService.procurarTagAtiva(dto.codigotTag());
        UsuarioPcd pcd = tag.getUsuarioPcd();
        Estacao estacao = estacaoService.procurarEstacaoAtiva(dto.codigoEstacao());
        Entrada entrada = entradaService.procurarEntradaAtiva(dto.codigoEntrada());
        LocalDateTime dataHora = LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.timestamp()), ZoneOffset.UTC);
        LocalTime horario = dataHora.toLocalTime();

        List<AgenteAtendimento> agentesDisponiveis = agenteService.procurarAgentesDisponiveis(estacao, horario);
        if (agentesDisponiveis.isEmpty()) {
            throw new AgenteIndisponivelParaAtendimentoException();
        }
        Collections.shuffle(agentesDisponiveis);
        AgenteAtendimento agente = agentesDisponiveis.getFirst();

        repository.save(PendenciaAtendimento.builder()
                .pcdAtendido(pcd)
                .agente(agente)
                .estacao(estacao)
                .entrada(entrada)
                .dataHora(dataHora)
                .statusAtendimento(StatusAtendimento.PENDENTE)
                .build()
        );
    }
}
package com.senai.bgjkr_metro_acesso_backend.application.dto.estacao;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Linha;

import java.util.ArrayList;
import java.util.Set;

public record EstacaoRequestDto(
        String nome,
        String codigoEstacao,
        Set<Integer> linhas
) {
    public Estacao toEntity(
    ) {
        return Estacao.builder()
                .nome(nome)
                .codigoEstacao(codigoEstacao)
                .linhas(Linha.fromNumeros(linhas))
                .agentes(new ArrayList<>())
                .sensores(new ArrayList<>())
                .ativo(true)
                .build();
    }
}

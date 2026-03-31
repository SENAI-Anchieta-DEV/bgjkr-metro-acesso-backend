package com.senai.bgjkr_metro_acesso_backend.application.dto.estacao;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Linha;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Set;

public record EstacaoRequestDto(
        @NotBlank
        @Size(max = 200)
        String nome,

        @NotBlank
        @Size(max = 200)
        String codigoEstacao,

        @NotNull
        @NotEmpty
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

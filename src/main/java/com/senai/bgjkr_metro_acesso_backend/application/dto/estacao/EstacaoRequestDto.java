package com.senai.bgjkr_metro_acesso_backend.application.dto.estacao;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Linha;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Set;

@Schema(description = "Objeto de transferência de dados contendo os atributos de uma Estação.")
public record EstacaoRequestDto(
        @Schema(description = "Nome comercial da estação.", example = "Sé")
        @NotBlank
        @Size(max = 200)
        String nome,

        @Schema(description = "Código estrutural único identificador da estação.", example = "EST-SE-01")
        @NotBlank
        @Size(max = 200)
        String codigoEstacao,

        @Schema(description = "Lista numérica com as linhas que passam por esta estação (ex: Linha 1-Azul, Linha 3-Vermelha).", example = "[1, 3]")
        @NotNull
        @NotEmpty
        Set<Integer> linhas
) {
    public Estacao toEntity() {
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
package com.senai.bgjkr_metro_acesso_backend.application.dto.estacao;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Objeto de transferência de dados contendo os atributos de uma Estação.")
public record EstacaoUpdateDto(
        String nome,
        String codigoEstacao,
        Set<Integer> linhas
) {
}
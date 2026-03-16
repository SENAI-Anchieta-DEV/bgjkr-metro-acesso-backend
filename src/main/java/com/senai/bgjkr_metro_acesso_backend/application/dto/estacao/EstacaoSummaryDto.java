package com.senai.bgjkr_metro_acesso_backend.application.dto.estacao;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Linha;

import java.util.Set;

public record EstacaoSummaryDto(
        String id,
        String nome,
        String codigoEstacao,
        Set<Linha> linhas
) {
    public static EstacaoSummaryDto fromEntity(
            Estacao estacao
    ) {
        return new EstacaoSummaryDto(
                estacao.getId(),
                estacao.getNome(),
                estacao.getCodigoEstacao(),
                estacao.getLinhas()
        );
    }
}

package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;

public record PcdSummaryDto(
        String id,
        String nome,
        String email,
        TipoDeficiencia tipoDeficiencia,
        Boolean desejaSuporte
) {
    public static PcdSummaryDto fromEntity(
            UsuarioPcd usuarioPcd
    ) {
        return new PcdSummaryDto(
                usuarioPcd.getId(),
                usuarioPcd.getNome(),
                usuarioPcd.getEmail(),
                usuarioPcd.getTipoDeficiencia(),
                usuarioPcd.isDesejaSuporte()
        );
    }
}
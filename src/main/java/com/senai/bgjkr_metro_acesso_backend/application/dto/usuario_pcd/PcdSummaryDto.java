package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;

import java.util.Set;

public record PcdSummaryDto(
        String id,
        String nome,
        String email,
        Set<TipoDeficiencia> tiposDeficiencia,
        Boolean desejaSuporte
) {
    public static PcdSummaryDto fromEntity(
            UsuarioPcd usuarioPcd
    ) {
        return new PcdSummaryDto(
                usuarioPcd.getId(),
                usuarioPcd.getNome(),
                usuarioPcd.getEmail(),
                usuarioPcd.getTiposDeficiencia(),
                usuarioPcd.isDesejaSuporte()
        );
    }
}
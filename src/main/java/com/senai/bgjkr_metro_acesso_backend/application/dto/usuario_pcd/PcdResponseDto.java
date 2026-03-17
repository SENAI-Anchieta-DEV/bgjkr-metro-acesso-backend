package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;

import java.util.Set;

public record PcdResponseDto(
        String id,
        String nome,
        String email,
        Set<TipoDeficiencia> tiposDeficiencia,
        Boolean desejaSuporte,
        TagSummaryDto tag
) {
    public static PcdResponseDto fromEntity(
            UsuarioPcd usuarioPcd
    ) {
        return new PcdResponseDto(
                usuarioPcd.getId(),
                usuarioPcd.getNome(),
                usuarioPcd.getEmail(),
                usuarioPcd.getTiposDeficiencia(),
                usuarioPcd.isDesejaSuporte(),
                usuarioPcd.getTag() == null ?
                        null :
                        TagSummaryDto.fromEntity(usuarioPcd.getTag())
        );
    }
}
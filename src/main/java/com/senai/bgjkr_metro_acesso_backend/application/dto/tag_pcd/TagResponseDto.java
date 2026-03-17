package com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd;

import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdSummaryDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;

public record TagResponseDto(
        String id,
        String codigoTag,
        PcdSummaryDto usuarioPcd
) {
    public static TagResponseDto fromEntity(
            TagPcd tagPcd
    ) {
        return new TagResponseDto(
                tagPcd.getId(),
                tagPcd.getCodigoTag(),
                tagPcd.getUsuarioPcd() == null ?
                        null :
                        PcdSummaryDto.fromEntity(tagPcd.getUsuarioPcd())
        );
    }
}

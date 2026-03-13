package com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;

public record TagRequestDto(
        String codigoTag
) {
    public TagPcd toEntity() {
        return TagPcd.builder()
                .codigoTag(codigoTag)
                .usuarioPcd(null)
                .ativo(true)
                .build();
    }
}
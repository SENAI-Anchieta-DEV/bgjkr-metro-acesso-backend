package com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagRequestDto(
        @NotBlank
        @Size(max = 200)
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
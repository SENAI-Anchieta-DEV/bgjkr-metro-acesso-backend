package com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Registo de uma nova Tag ou alteração da mesma.")
public record TagRequestDto(
        @Schema(description = "Código gerado para posterior vinculação com um utilizador.", example = "TAG-XYZ-789")
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
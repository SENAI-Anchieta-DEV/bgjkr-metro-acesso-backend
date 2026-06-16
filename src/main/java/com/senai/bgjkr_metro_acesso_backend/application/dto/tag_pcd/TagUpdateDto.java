package com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Registo de uma nova Tag ou alteração da mesma.")
public record TagUpdateDto(
        String codigoTag
) {
}
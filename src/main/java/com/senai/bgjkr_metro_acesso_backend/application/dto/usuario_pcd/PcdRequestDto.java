package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;

public record PcdRequestDto (
        String nome,
        String email,
        String senha,
        TipoDeficiencia tipoDeficiencia,
        Boolean desejaSuporte,
        String codigoTag
) {
}
package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;

public record PcdRequestDto(
        String nome,
        String email,
        String senha,
        TipoDeficiencia tipoDeficiencia,
        Boolean desejaSuporte,
        String codigoTag
) {
    public UsuarioPcd toEntity(
            TagPcd tagPcd
    ) {
        return UsuarioPcd.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .ativo(true)
                .role(Role.USUARIO_PCD)
                .tipoDeficiencia(tipoDeficiencia)
                .desejaSuporte(desejaSuporte)
                .tag(tagPcd)
                .build();
    }
}
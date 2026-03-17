package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;

import java.util.Set;

public record PcdRequestDto(
        String nome,
        String email,
        String senha,
        Set<TipoDeficiencia> tiposDeficiencia,
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
                .tiposDeficiencia(tiposDeficiencia)
                .desejaSuporte(desejaSuporte)
                .tag(tagPcd)
                .build();
    }
}
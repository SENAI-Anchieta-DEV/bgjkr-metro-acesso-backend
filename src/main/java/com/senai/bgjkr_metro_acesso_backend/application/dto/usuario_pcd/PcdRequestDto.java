package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record PcdRequestDto(
        @NotBlank
        @Size(max = 200)
        String nome,

        @NotBlank
        @Email
        @Size(max = 200)
        String email,

        @NotBlank
        @Size(min = 8, max = 200)
        String senha,

        @NotNull
        @NotEmpty
        Set<TipoDeficiencia> tiposDeficiencia,

        @NotNull
        Boolean desejaSuporte,

        @NotBlank
        @Size(max = 200)
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
package com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;

import java.nio.file.Path;
import java.util.Set;

public record FormSolicitacaoResponseDto(
        String nome,
        String email,
        String senha,
        Set<TipoDeficiencia> tiposDeficiencia,
        Boolean desejaSuporte,
        String comprovacaoId
) {
    public static FormSolicitacaoResponseDto fromEntity(
            FormularioPcd formulario
    ) {
        return new FormSolicitacaoResponseDto(
                formulario.getNome(),
                formulario.getEmail(),
                formulario.getSenha(),
                formulario.getTiposDeficiencia(),
                formulario.isDesejaSuporte(),
                formulario.getComprovacaoId()
        );
    }
}

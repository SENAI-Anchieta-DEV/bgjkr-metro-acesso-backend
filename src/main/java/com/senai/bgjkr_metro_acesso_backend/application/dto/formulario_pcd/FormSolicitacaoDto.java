package com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusFormulario;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public record FormSolicitacaoDto(
        String nome,
        String email,
        String senha,
        Set<TipoDeficiencia> tiposDeficiencia,
        Boolean desejaSuporte,
        MultipartFile comprovante
) {
    public FormularioPcd toEntity(
    ) {
        return FormularioPcd.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .tiposDeficiencia(tiposDeficiencia)
                .desejaSuporte(desejaSuporte)
                .motivoReprovacao(null)
                .adminResponsavel(null)
                .status(StatusFormulario.EM_ANALISE)
                .comprovante(comprovante)
                .ativo(true)
                .build();
    }

    public static FormSolicitacaoDto fromEntity(
            FormularioPcd formulario
    ) {
        return new FormSolicitacaoDto(
                formulario.getNome(),
                formulario.getEmail(),
                formulario.getSenha(),
                formulario.getTiposDeficiencia(),
                formulario.isDesejaSuporte(),
                formulario.getComprovante()
        );
    }
}

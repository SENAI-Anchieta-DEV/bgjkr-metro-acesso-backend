package com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusFormulario;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Set;

public record FormSolicitacaoRequestDto(
        String nome,
        String email,
        String senha,
        Set<TipoDeficiencia> tiposDeficiencia,
        Boolean desejaSuporte,
        MultipartFile comprovacao
) {
    public FormularioPcd toEntity(
            String comprovacaoId
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
                .comprovacaoId(comprovacaoId)
                .ativo(true)
                .build();
    }
}

package com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusFormulario;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public record FormSolicitacaoRequestDto(
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

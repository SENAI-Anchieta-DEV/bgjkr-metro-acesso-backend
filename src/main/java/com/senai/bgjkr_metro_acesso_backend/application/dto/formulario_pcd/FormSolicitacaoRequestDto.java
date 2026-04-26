package com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusFormulario;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Schema(description = "Dados submetidos para o registo preliminar de um utilizador PCD (Formulário).")
public record FormSolicitacaoRequestDto(
        @Schema(description = "Nome completo do requerente.", example = "João da Silva")
        @NotBlank
        @Size(max = 200)
        String nome,

        @Schema(description = "E-mail para futura conta e acompanhamento do processo.", example = "joao.silva@gmail.com")
        @NotBlank
        @Email
        @Size(max = 200)
        String email,

        @Schema(description = "Senha que será utilizada quando a conta for aprovada.", example = "SenhaSegura123")
        @NotBlank
        @Size(min = 8, max = 200)
        String senha,

        @Schema(description = "Lista com o(s) tipo(s) de deficiência reportados.", example = "[\"VISUAL\", \"MOTORA\"]")
        @NotNull
        @NotEmpty
        Set<TipoDeficiencia> tiposDeficiencia,

        @Schema(description = "Booleano indicando se o requerente deseja receber suporte presencial dos agentes nas estações.", example = "true")
        @NotNull
        Boolean desejaSuporte,

        @Schema(description = "Ficheiro anexo contendo o laudo ou comprovativo médico da deficiência.", type = "string", format = "binary")
        MultipartFile comprovacao
) {
    public FormularioPcd toEntity(String comprovacaoId) {
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
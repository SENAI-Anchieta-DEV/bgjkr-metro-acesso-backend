package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Schema(description = "Dados para cadastro direto de usuário PCD pelo administrador (multipart/form-data).")
public record PcdRequestDto(
        @Schema(description = "Nome completo do PCD.", example = "Felipe Costa")
        @NotBlank
        @Size(max = 200)
        String nome,

        @Schema(description = "Endereço de e-mail de acesso.", example = "felipe@email.com")
        @NotBlank
        @Email
        @Size(max = 200)
        String email,

        @Schema(description = "Senha segura de acesso.", example = "MinhaSenhaSegura123")
        @NotBlank
        @Size(min = 8, max = 200)
        String senha,

        @Schema(description = "Tipos de deficiência do usuário.", example = "[\"VISUAL\", \"MOTORA\"]")
        @NotNull
        @NotEmpty
        Set<TipoDeficiencia> tiposDeficiencia,

        @Schema(description = "Indica se o usuário deseja suporte presencial nas estações.", example = "true")
        @NotNull
        Boolean desejaSuporte,

        @Schema(description = "Arquivo de laudo médico / comprovação de deficiência.", type = "string", format = "binary")
        @NotNull
        MultipartFile comprovacao
) {
        public UsuarioPcd toEntity(String comprovacaoId, TagPcd tag) {
                return UsuarioPcd.builder()
                        .nome(nome)
                        .email(email)
                        .senha(senha)
                        .ativo(true)
                        .role(Role.USUARIO_PCD)
                        .tiposDeficiencia(tiposDeficiencia)
                        .desejaSuporte(desejaSuporte)
                        .comprovacaoId(comprovacaoId)
                        .tag(tag)
                        .build();
        }
}
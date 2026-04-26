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

import java.util.Set;

@Schema(description = "Objeto de transferência para a configuração de contas definitivas de Utilizadores PCD.")
public record PcdRequestDto(
        @Schema(description = "Nome completo da PCD.", example = "Felipe Costa")
        @NotBlank
        @Size(max = 200)
        String nome,

        @Schema(description = "Endereço de e-mail de acesso da PCD.", example = "felipe.pcd@metroacesso.com")
        @NotBlank
        @Email
        @Size(max = 200)
        String email,

        @Schema(description = "Senha segura de acesso.", example = "MinhaSenhaSegura123")
        @NotBlank
        @Size(min = 8, max = 200)
        String senha,

        @Schema(description = "Coleção dos tipos de deficiência validados do utilizador.", example = "[\"VISUAL\", \"MOTORA\"]")
        @NotNull
        @NotEmpty
        Set<TipoDeficiencia> tiposDeficiencia,

        @Schema(description = "Sinalizador da preferência ou necessidade de assistência presencial nas estações.", example = "true")
        @NotNull
        Boolean desejaSuporte,

        @Schema(description = "Código hexadecimal/identificador da Tag RFID física entregue a este utilizador.", example = "TAG-XYZ-789")
        @NotBlank
        @Size(max = 200)
        String codigoTag
) {
    public UsuarioPcd toEntity(TagPcd tagPcd) {
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
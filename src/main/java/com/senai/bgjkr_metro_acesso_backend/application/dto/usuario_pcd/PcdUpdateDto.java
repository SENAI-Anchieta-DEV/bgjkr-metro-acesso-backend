package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Schema(description = "Dados para atualização de usuário PCD (multipart/form-data). Todos os campos são opcionais.")
public record PcdUpdateDto(
        String nome,
        String email,
        String senha,
        Set<TipoDeficiencia> tiposDeficiencia,
        Boolean desejaSuporte,

        @Schema(description = "Novo arquivo de comprovação (opcional).", type = "string", format = "binary")
        MultipartFile comprovacao
) {
}
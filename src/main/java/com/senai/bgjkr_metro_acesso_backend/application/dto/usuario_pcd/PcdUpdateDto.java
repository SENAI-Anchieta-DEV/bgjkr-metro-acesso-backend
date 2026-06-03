package com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd;

import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Objeto de transferência para a configuração de contas definitivas de Utilizadores PCD.")
public record PcdUpdateDto(
        String nome,
        String email,
        String senha,
        String codigoTag,
        Set<TipoDeficiencia> tiposDeficiencia,
        Boolean desejaSuporte
) {
}
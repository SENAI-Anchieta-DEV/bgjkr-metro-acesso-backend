package com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd;

import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusFormulario;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public record FormAprovacaoResponseDto(
        String nome,
        String email,
        String senha,
        Set<TipoDeficiencia> tiposDeficiencia,
        Boolean desejaSuporte,
        String motivoReprovacao,
        AdminResponseDto adminResponseDto,
        StatusFormulario status,
        MultipartFile comprovante
) {
    public static FormAprovacaoResponseDto fromEntity(
            FormularioPcd formulario
    ) {
        return new FormAprovacaoResponseDto(
                formulario.getNome(),
                formulario.getEmail(),
                formulario.getSenha(),
                formulario.getTiposDeficiencia(),
                formulario.isDesejaSuporte(),
                formulario.getMotivoReprovacao(),
                AdminResponseDto.fromEntity(formulario.getAdminResponsavel()),
                formulario.getStatus(),
                formulario.getComprovante()
        );
    }
}

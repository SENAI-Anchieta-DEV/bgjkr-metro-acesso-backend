package com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd;

public record FormAprovacaoRequestDto(
        Boolean aprovado,
        String motivoReprovacao
) {
}

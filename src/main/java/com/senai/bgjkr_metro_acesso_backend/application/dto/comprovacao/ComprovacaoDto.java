package com.senai.bgjkr_metro_acesso_backend.application.dto.comprovacao;

import org.springframework.core.io.Resource;

public record ComprovacaoDto(
        Resource resource,
        String contentType,
        String comprovacaoId
) {
}

package com.senai.bgjkr_metro_acesso_backend.application.dto.administrador;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de transferência de dados para requisições de Administrador (Criação e Atualização).")
public record AdminUpdateDto(
        String nome,
        String email,
        String senha
) {
}
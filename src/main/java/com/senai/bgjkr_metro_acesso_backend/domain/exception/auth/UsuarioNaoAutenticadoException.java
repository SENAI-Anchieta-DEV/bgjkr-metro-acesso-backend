package com.senai.bgjkr_metro_acesso_backend.domain.exception.auth;

public class UsuarioNaoAutenticadoException extends RuntimeException {
    public UsuarioNaoAutenticadoException() {
        super("O usuário que solicitou a ação não está autenticado.");
    }
}

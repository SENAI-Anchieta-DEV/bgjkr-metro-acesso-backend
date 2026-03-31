package com.senai.bgjkr_metro_acesso_backend.domain.exception.auth;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("Credenciais inválidas para login desse usuário.");
    }
}

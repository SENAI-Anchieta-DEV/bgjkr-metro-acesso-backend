package com.senai.bgjkr_metro_acesso_backend.domain.exception.estacao;

public class NumeroDeLinhaInvalidaException extends RuntimeException {
    public NumeroDeLinhaInvalidaException(int numero) {
        super("Número de linha \"" + numero + "\" não corresponde a nenhuma linha existente do sistema de transporte ferroviário.");
    }
}

package com.senai.bgjkr_metro_acesso_backend.domain.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String tipo, String tipoChave, String chave) {
        super("Entidade do tipo " + tipo + " não foi encontrada por " + tipoChave + " \"" + chave + "\".");
    }
}

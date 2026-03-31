package com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario;

public class RemocaoDeFormularioDePcdAtivoException extends RuntimeException {
    public RemocaoDeFormularioDePcdAtivoException() {
        super("Não é possível remover o formulário de um usuário PcD ainda ativo.");
    }
}

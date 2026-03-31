package com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario;

public class FormularioPcdComEmailDeUsuarioAtivoException extends RuntimeException {
    public FormularioPcdComEmailDeUsuarioAtivoException() {
        super("Não é possível solicitar cadastro de usuário PcD com e-mail de um usuário já ativo no sistema.");
    }
}

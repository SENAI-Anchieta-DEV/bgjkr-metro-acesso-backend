package com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario;

public class AlterarFormularioPcdJaValidadoException extends RuntimeException {
    public AlterarFormularioPcdJaValidadoException() {
        super("Não é possível alterar um formulário de solicitação de cadastro de PcD que não está em análise.");
    }
}

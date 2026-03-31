package com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario;

public class LeituraDeComprovacaoDeDeficienciaException extends RuntimeException {
    public LeituraDeComprovacaoDeDeficienciaException() {
        super("Erro ao ler arquivo de comprovação de deficiência.");
    }
}

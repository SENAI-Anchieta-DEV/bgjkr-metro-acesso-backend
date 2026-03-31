package com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario;

public class ComprovacaoDeDeficienciaAusenteException extends RuntimeException {
    public ComprovacaoDeDeficienciaAusenteException() {
        super("Arquivo de comprovação de deficiência obrigatório está ausente.");
    }
}

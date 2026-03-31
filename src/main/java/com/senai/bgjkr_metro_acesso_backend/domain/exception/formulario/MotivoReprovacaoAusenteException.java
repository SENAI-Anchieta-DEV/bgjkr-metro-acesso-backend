package com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario;

public class MotivoReprovacaoAusenteException extends RuntimeException {
    public MotivoReprovacaoAusenteException() {
        super("Motivo para a reprovação de uma solicitação de cadastro de PcD é obrigatório.");
    }
}

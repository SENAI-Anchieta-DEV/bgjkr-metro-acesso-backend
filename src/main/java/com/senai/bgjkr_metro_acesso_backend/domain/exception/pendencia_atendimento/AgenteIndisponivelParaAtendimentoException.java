package com.senai.bgjkr_metro_acesso_backend.domain.exception.pendencia_atendimento;

public class AgenteIndisponivelParaAtendimentoException extends RuntimeException {
    public AgenteIndisponivelParaAtendimentoException() {
        super("Nenhum agente disponível para atendimento no horário e estação solicitados.");
    }
}

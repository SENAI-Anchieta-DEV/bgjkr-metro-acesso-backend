package com.senai.bgjkr_metro_acesso_backend.domain.exception.tag_pcd;

public class TagIndisponivelException extends RuntimeException {
    public TagIndisponivelException() {
        super("Não há tags disponíveis para a requisição.");
    }
}

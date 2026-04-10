package com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd;

public class TagEventoDTO {

    private String codigoTag;
    private String codigoEstacao;
    private String porta;
    private long timestamp;

    public String getCodigoTag() { return codigoTag; }
    public void setCodigoTag(String codigoTag) { this.codigoTag = codigoTag; }

    public String getCodigoEstacao() { return codigoEstacao; }
    public void setCodigoEstacao(String codigoEstacao) { this.codigoEstacao = codigoEstacao; }

    public String getPorta() { return porta; }
    public void setPorta(String porta) { this.porta = porta; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
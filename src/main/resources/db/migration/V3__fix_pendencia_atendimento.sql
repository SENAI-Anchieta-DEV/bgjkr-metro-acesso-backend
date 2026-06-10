DROP TABLE IF EXISTS pendencias;

CREATE TABLE pendencias
(
    id                 VARCHAR(36) PRIMARY KEY,
    ativo              BOOLEAN      NOT NULL,
    data_hora          TIMESTAMP    NOT NULL,
    agente_id          VARCHAR(36),
    entrada_id         VARCHAR(36),
    estacao_id         VARCHAR(36),
    pcd_atendido_id    VARCHAR(36),
    CONSTRAINT fk_pendencias_agente   FOREIGN KEY (agente_id)       REFERENCES usuarios (id),
    CONSTRAINT fk_pendencias_entrada  FOREIGN KEY (entrada_id)      REFERENCES entradas (id),
    CONSTRAINT fk_pendencias_estacao  FOREIGN KEY (estacao_id)      REFERENCES estacoes (id),
    CONSTRAINT fk_pendencias_pcd      FOREIGN KEY (pcd_atendido_id) REFERENCES usuarios (id)
);
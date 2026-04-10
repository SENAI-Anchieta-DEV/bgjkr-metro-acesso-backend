-- ========================
--        USUARIOS
-- ========================
CREATE TABLE usuarios (
                          id          VARCHAR(36)  NOT NULL PRIMARY KEY,
                          nome        VARCHAR(120) NOT NULL,
                          email       VARCHAR(120) NOT NULL,
                          senha       VARCHAR(255) NOT NULL,
                          ativo       BOOLEAN      NOT NULL,
                          role        VARCHAR(100) NOT NULL,
                          CONSTRAINT uq_usuarios_email UNIQUE (email)
);

-- ========================
--     ADMINISTRADORES
-- ========================
CREATE TABLE administradores (
                                 id VARCHAR(36) NOT NULL PRIMARY KEY,
                                 CONSTRAINT fk_administradores_usuario FOREIGN KEY (id) REFERENCES usuarios (id)
);

-- ========================
--       ESTACOES
-- ========================
CREATE TABLE estacoes (
                          id             VARCHAR(36)  NOT NULL PRIMARY KEY,
                          nome           VARCHAR(120) NOT NULL,
                          codigo_estacao VARCHAR(120) NOT NULL,
                          ativo          BOOLEAN      NOT NULL,
                          CONSTRAINT uq_estacoes_codigo UNIQUE (codigo_estacao)
);

CREATE TABLE estacao_linhas (
                                estacao_id VARCHAR(36) NOT NULL,
                                linha      VARCHAR(50) NOT NULL,
                                CONSTRAINT fk_estacao_linhas_estacao FOREIGN KEY (estacao_id) REFERENCES estacoes (id)
);

-- ========================
--   AGENTES ATENDIMENTO
-- ========================
CREATE TABLE agentes_atendimento (
                                     id          VARCHAR(36) NOT NULL PRIMARY KEY,
                                     estacao_id  VARCHAR(36),
                                     inicio_turno TIME        NOT NULL,
                                     fim_turno    TIME        NOT NULL,
                                     CONSTRAINT fk_agentes_usuario  FOREIGN KEY (id)         REFERENCES usuarios (id),
                                     CONSTRAINT fk_agentes_estacao  FOREIGN KEY (estacao_id) REFERENCES estacoes (id)
);

-- ========================
--         TAGS
-- ========================
CREATE TABLE tags (
                      id          VARCHAR(36) NOT NULL PRIMARY KEY,
                      codigo_tag  VARCHAR(50) NOT NULL,
                      ativo       BOOLEAN     NOT NULL,
                      CONSTRAINT uq_tags_codigo UNIQUE (codigo_tag)
);

-- ========================
--      USUARIOS PCD
-- ========================
CREATE TABLE usuarios_pcd (
                              id             VARCHAR(36) NOT NULL PRIMARY KEY,
                              deseja_suporte BOOLEAN     NOT NULL,
                              tag_id         VARCHAR(36),
                              CONSTRAINT fk_usuarios_pcd_usuario FOREIGN KEY (id)     REFERENCES usuarios (id),
                              CONSTRAINT fk_usuarios_pcd_tag     FOREIGN KEY (tag_id) REFERENCES tags (id),
                              CONSTRAINT uq_usuarios_pcd_tag     UNIQUE (tag_id)
);

CREATE TABLE pcd_tipos_deficiencia (
                                       pcd_id          VARCHAR(36) NOT NULL,
                                       tipo_deficiencia VARCHAR(50) NOT NULL,
                                       CONSTRAINT fk_pcd_tipos_usuario_pcd FOREIGN KEY (pcd_id) REFERENCES usuarios_pcd (id)
);

-- ========================
--      FORMULARIOS
-- ========================
CREATE TABLE formularios (
                             id                 VARCHAR(36)  NOT NULL PRIMARY KEY,
                             nome               VARCHAR(120) NOT NULL,
                             email              VARCHAR(120) NOT NULL,
                             senha              VARCHAR(255) NOT NULL,
                             deseja_suporte     BOOLEAN      NOT NULL,
                             motivo_reprovacao  VARCHAR(255),
                             administrador_id   VARCHAR(36),
                             status             VARCHAR(50)  NOT NULL,
                             comprovacao_id     VARCHAR(255) NOT NULL,
                             ativo              BOOLEAN      NOT NULL,
                             CONSTRAINT uq_formularios_email   UNIQUE (email),
                             CONSTRAINT fk_formularios_admin   FOREIGN KEY (administrador_id) REFERENCES administradores (id)
);

CREATE TABLE formularios_tipos_deficiencia (
                                               formulario_id    VARCHAR(36) NOT NULL,
                                               tipo_deficiencia VARCHAR(50) NOT NULL,
                                               CONSTRAINT fk_formularios_tipos_formulario FOREIGN KEY (formulario_id) REFERENCES formularios (id)
);

-- ========================
--        SENSORES
-- ========================
CREATE TABLE sensores (
                          id            VARCHAR(36) NOT NULL PRIMARY KEY,
                          estacao_id    VARCHAR(36),
                          codigo_sensor VARCHAR(50) NOT NULL,
                          porta         VARCHAR(50),
                          ativo         BOOLEAN     NOT NULL,
                          CONSTRAINT uq_sensores_codigo  UNIQUE (codigo_sensor),
                          CONSTRAINT fk_sensores_estacao FOREIGN KEY (estacao_id) REFERENCES estacoes (id)
);
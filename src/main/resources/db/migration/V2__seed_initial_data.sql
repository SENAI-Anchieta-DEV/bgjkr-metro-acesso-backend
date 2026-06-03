-- ========================
-- TAG (PRECISA VIR ANTES)
-- ========================
INSERT INTO tags (id,
                  codigo_tag,
                  ativo)
VALUES ('tag-fisica-esp32',
        'A4:CB:8F:20:F9:F4',
        true);

-- ========================
-- USUARIO PCD
-- ========================
INSERT INTO usuarios (id,
                      nome,
                      email,
                      senha,
                      role,
                      ativo)
VALUES ('u2f3a222-2222-2222-2222-222222222222',
        'Pedro Henricky',
        'pedro@gmail.com',
        'pedro123',
        'USUARIO_PCD',
        true);

-- ========================
-- USUARIOS_PCD (DEPOIS DA TAG)
-- ========================
INSERT INTO usuarios_pcd (id,
                          deseja_suporte,
                          tag_id)
VALUES ('u2f3a222-2222-2222-2222-222222222222',
        true,
        'tag-fisica-esp32');

-- ========================
-- PCD TIPOS
-- ========================
INSERT INTO pcd_tipos_deficiencia (pcd_id,
                                   tipos_deficiencia)
VALUES ('u2f3a222-2222-2222-2222-222222222222',
        'VISUAL');

-- ========================
-- ESTACAO
-- ========================
INSERT INTO estacoes (id,
                      codigo_estacao,
                      nome,
                      ativo)
VALUES ('e3f3a333-3333-3333-3333-333333333333',
        'se01',
        'Sé',
        true);

-- ========================
-- USUARIO AGENTE
-- ========================
INSERT INTO usuarios (id,
                      nome,
                      email,
                      senha,
                      role,
                      ativo)
VALUES ('a4f3a444-4444-4444-4444-444444444444',
        'Júnior',
        'junior@metroacesso.com',
        'junior123',
        'AGENTE_ATENDIMENTO',
        true);

-- ========================
-- AGENTE ATENDIMENTO
-- ========================
INSERT INTO agentes_atendimento (id,
                                 inicio_turno,
                                 fim_turno,
                                 estacao_id)
VALUES ('a4f3a444-4444-4444-4444-444444444444',
        '00:00:00',
        '23:59:59',
        'e3f3a333-3333-3333-3333-333333333333');

-- ========================
-- ENTRADA
-- ========================
INSERT INTO entradas (id,
                      estacao_id,
                      codigo_entrada,
                      bssid,
                      ativo)
VALUES ('celular-brayan123',
        'e3f3a333-3333-3333-3333-333333333333',
        '123321',
        '26:0B:02:11:73:3F',
        true);

-- ========================
-- ESTACAO_LINHAS
-- ========================
INSERT INTO estacao_linhas (estacao_id, linha)
VALUES ('e3f3a333-3333-3333-3333-333333333333', 'AZUL');

INSERT INTO estacao_linhas (estacao_id, linha)
VALUES ('e3f3a333-3333-3333-3333-333333333333', 'VERMELHA');
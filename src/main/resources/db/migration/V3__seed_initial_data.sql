-- ========================
-- TAG
-- ========================

-- ========================
-- TAG
-- ========================
INSERT INTO tags (id, codigo_tag, ativo)
VALUES (
           't1f3a111-1111-1111-1111-111111111111',
           '1',
           true
       );

-- ========================
-- USUARIO PCD
-- ========================
INSERT INTO usuarios (
    id,
    nome,
    email,
    senha,
    role,
    ativo
)
VALUES (
           'u2f3a222-2222-2222-2222-222222222222',
           'Pedro Henricky',
           'pedro@gmail.com',
           'pedro123',
           'USUARIO_PCD',
           true
       );

-- ========================
-- USUARIOS_PCD (TEM QUE VIR ANTES!)
-- ========================
INSERT INTO usuarios_pcd (
    id,
    deseja_suporte,
    tag_id
)
VALUES (
           'u2f3a222-2222-2222-2222-222222222222',
           true,
           't1f3a111-1111-1111-1111-111111111111'
       );

-- ========================
-- PCD TIPOS (AGORA SIM)
-- ========================
INSERT INTO pcd_tipos_deficiencia (
    pcd_id,
    tipo_deficiencia
)
VALUES (
           'u2f3a222-2222-2222-2222-222222222222',
           'VISUAL'
       );

-- ========================
-- ESTACAO
-- ========================
INSERT INTO estacoes (
    id,
    codigo_estacao,
    nome,
    ativo
)
VALUES (
           'e3f3a333-3333-3333-3333-333333333333',
           'se01',
           'Sé',
           true
       );

-- ========================
-- USUARIO AGENTE
-- ========================
INSERT INTO usuarios (
    id,
    nome,
    email,
    senha,
    role,
    ativo
)
VALUES (
           'a4f3a444-4444-4444-4444-444444444444',
           'Júnior',
           'junior@metroacesso.com',
           'junior123',
           'AGENTE_ATENDIMENTO',
           true
       );

-- ========================
-- AGENTE ATENDIMENTO
-- ========================
INSERT INTO agentes_atendimento (
    id,
    inicio_turno,
    fim_turno,
    estacao_id
)
VALUES (
           'a4f3a444-4444-4444-4444-444444444444',
           '12:00:00',
           '12:00:59',
           'e3f3a333-3333-3333-3333-333333333333'
       );
-- ========================
--     ADMIN INICIAL
-- ========================
-- Senha: admin123

-- ADMIN INICIAL
INSERT INTO usuarios (id, nome, email, senha, ativo, role)
VALUES (
           '11111111-1111-1111-1111-111111111111',
           'Administrador',
           'admin@metroacesso.com',
           '$2b$10$6AQJZzrWuUAI49iySZ7h7uzr8b8cgDUClKgJhMXMRE/q7.kOrneBa',
           true,
           'ADMINISTRADOR'
       );

INSERT INTO administradores (id)
VALUES (
           '11111111-1111-1111-1111-111111111111'
       );
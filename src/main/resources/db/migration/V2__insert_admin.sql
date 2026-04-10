-- ========================
--     ADMIN INICIAL
-- ========================
-- Senha: admin123
INSERT INTO usuarios (id, nome, email, senha, ativo, role)
VALUES (
           gen_random_uuid()::text,
           'Administrador',
           'admin@metroacesso.com',
           '$2b$10$6AQJZzrWuUAI49iySZ7h7uzr8b8cgDUClKgJhMXMRE/q7.kOrneBa',
           true,
           'ADMINISTRADOR'
       );

INSERT INTO administradores (id)
SELECT id FROM usuarios WHERE email = 'admin@metroacesso.com';
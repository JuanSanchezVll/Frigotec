-- DADOS INICIAIS (sempre carregados): só o administrador de entrada e o catálogo.
-- Clientes, técnico, equipamentos e chamados de exemplo estão em data-demo.sql (também carregado por padrão).

-- 1. Endereço do administrador inicial
INSERT INTO endereco (id, estado, cidade, rua, numero, cep) VALUES
    ('11111111-1111-1111-1111-111111111111', 'SP', 'São Paulo', 'Rua das Flores', '100', '01001000');

-- 2. Administrador inicial: é com ele que se cria o primeiro técnico e os outros administradores.
-- Ninguém consegue se cadastrar como ADMIN pela tela; por isso este registro precisa existir.
-- E-mail: admin@frigotec.com | senha: senha123 (troque depois; em produção use SEED_MODE=never)
INSERT INTO usuario (id, perfil, nome_completo, email, cpf, senha, telefone, celular, endereco_id) VALUES
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'ADMIN', 'Administrador Frigotec', 'admin@frigotec.com', '16899535009',
     '$2a$10$7rV.ttF6IWKudwd2C1cyS.qJeD/ijRqq9vswkP9frBEp.8ythYSV6', '1133335555', '11966665555',
     '11111111-1111-1111-1111-111111111111');

-- 4. Fabricante (AUTO). FrigoPro é a marca própria: só ela tem garantia cadastrável.
INSERT INTO fabricante (id, nome, propria) VALUES
                                      (1001, 'FrigoPro', TRUE),
                                      (1002, 'Rational', FALSE),
                                      (1003, 'ITW', FALSE),
                                      (1004, 'Tramontina', FALSE),
                                      (1005, 'Skymsen', FALSE);

-- 5. Equipamento (IDENTITY): modelos do catálogo
INSERT INTO equipamento (modelo, fabricante_id) VALUES
                                                    ('Chapa Bifteira FP-600', 1001),
                                                    ('Fritadeira Elétrica FP-20L', 1001),
                                                    ('Forno Combinado iCombi Pro', 1002),
                                                    ('Forno Turbo ITW-4', 1003),
                                                    ('Fogão Industrial 6 Bocas', 1004),
                                                    ('Cilindro Laminador CL-300', 1005);

-- 7. Status (AUTO)
INSERT INTO status (id, nome, descricao) VALUES
                                             (1001, 'ABERTO', 'Chamado aberto'),
                                             (1002, 'EM_ANDAMENTO', 'Técnico em atendimento'),
                                             (1003, 'ENCERRADO', 'Chamado finalizado');

-- 8. Roles (IDENTITY)
INSERT INTO roles (descricao) VALUES ('ADMIN'), ('TECNICO'), ('CLIENTE');

ALTER SEQUENCE status_seq RESTART WITH 2000;
ALTER SEQUENCE chamado_seq RESTART WITH 2000;
ALTER SEQUENCE fabricante_seq RESTART WITH 2000;
ALTER SEQUENCE encerramento_seq RESTART WITH 2000;
ALTER SEQUENCE mensagem_email_seq RESTART WITH 2000;

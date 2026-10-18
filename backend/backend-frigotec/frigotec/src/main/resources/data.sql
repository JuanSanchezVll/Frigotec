-- 1. Endereco
INSERT INTO endereco (id, estado, cidade, rua, numero, cep) VALUES
                                                                ('11111111-1111-1111-1111-111111111111', 'SP', 'São Paulo', 'Rua das Flores', '100', '01001000'),
                                                                ('22222222-2222-2222-2222-222222222222', 'RJ', 'Rio de Janeiro', 'Av. Atlântica', '250', '22010000');

-- 2. Empresa (IDENTITY)
INSERT INTO empresa (razao_social, inscricao_estadual, cnpj) VALUES
                                                                 ('Frio Total Ltda', '123456789', '12345678000199'),
                                                                 ('Gelo Forte S.A.', '987654321', '98765432000188');

-- 3. Usuario
INSERT INTO usuario (id, nome_completo, email, cpf, senha, telefone, celular, endereco_id, empresa_id) VALUES
                                                                                                           ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'João Silva', 'joao@email.com', '11122233344',
                                                                                                            '$2a$10$abcdefghijklmnopqrstuv', '1133334444', '11999998888',
                                                                                                            '11111111-1111-1111-1111-111111111111',
                                                                                                            (SELECT id FROM empresa WHERE cnpj = '12345678000199')),
                                                                                                           ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Maria Souza', 'maria@email.com', '55566677788',
                                                                                                            '$2a$10$abcdefghijklmnopqrstuv', '2122223333', '21988887777',
                                                                                                            '22222222-2222-2222-2222-222222222222', NULL);

-- 4. Fabricante (AUTO)
INSERT INTO fabricante (id, nome) VALUES
                                      (1001, 'Electrolux'),
                                      (1002, 'Brastemp'),
                                      (1003, 'Consul');

-- 5. Equipamento (IDENTITY)
INSERT INTO equipamento (modelo, fabricante_id) VALUES
                                                    ('Geladeira Frost Free 400L', 1001),
                                                    ('Freezer Horizontal 300L', 1002),
                                                    ('Ar Condicionado Split 12000', 1003);

-- 6. EquipamentoUsuario (IDENTITY)
INSERT INTO equipamento_usuario (equipamento_id, usuario_id, numero_serie) VALUES
                                                                               ((SELECT id FROM equipamento WHERE modelo = 'Geladeira Frost Free 400L'),
                                                                                'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'SN-0001'),
                                                                               ((SELECT id FROM equipamento WHERE modelo = 'Freezer Horizontal 300L'),
                                                                                'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'SN-0002');

-- 7. Status (AUTO)
INSERT INTO status (id, nome, descricao) VALUES
                                             (1001, 'ABERTO', 'Chamado aberto'),
                                             (1002, 'EM_ANDAMENTO', 'Técnico em atendimento'),
                                             (1003, 'ENCERRADO', 'Chamado finalizado');

-- 8. Roles (IDENTITY)
INSERT INTO roles (descricao) VALUES ('ADMIN'), ('TECNICO'), ('CLIENTE');

-- 9. Chamado (AUTO)
INSERT INTO chamado (id, data_pedido, descricao, problema, usuario_id, equipamento_usuario_id) VALUES
    (1001, '2026-10-10', 'Geladeira não gela', 'Compressor não liga',
     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     (SELECT id FROM equipamento_usuario WHERE numero_serie = 'SN-0001'));

-- 10. Historico (AUTO)
INSERT INTO historico (id, data_inicio, chamado_id) VALUES
    (1001, '2026-10-10', 1001);

UPDATE status SET status_id = 1001 WHERE id IN (1001, 1002);

-- 11. Encerramento (AUTO)
INSERT INTO encerramento (id, descricao, data_encerramento, motivo, chamado_id) VALUES
    (1001, 'Compressor trocado', '2026-10-15', 'Reparo concluído', 1001);

-- 12. MensagemEmail (AUTO)
INSERT INTO mensagem_email (id, chamado_id, usuario_id, descricao, assunto) VALUES
    (1001, 1001, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     'Seu chamado foi aberto e será atendido em breve.', 'Chamado #1001 aberto');

-- 13. Garantia (IDENTITY)
INSERT INTO garantia (data_origem, data_fim, garantia_ativa, nota_fiscal, id_equipamento) VALUES
    ('2026-01-10', '2027-01-10', 1, 'NF-123456',
     (SELECT id FROM equipamento WHERE modelo = 'Geladeira Frost Free 400L'));

ALTER SEQUENCE status_seq RESTART WITH 2000;
ALTER SEQUENCE chamado_seq RESTART WITH 2000;
ALTER SEQUENCE fabricante_seq RESTART WITH 2000;
ALTER SEQUENCE historico_seq RESTART WITH 2000;
ALTER SEQUENCE encerramento_seq RESTART WITH 2000;
ALTER SEQUENCE mensagem_email_seq RESTART WITH 2000;
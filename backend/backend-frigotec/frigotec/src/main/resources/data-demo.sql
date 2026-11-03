-- DADOS DE EXEMPLO (carregados por padrão): 2 clientes, 1 técnico, equipamentos, 7 chamados em situações diferentes, orçamento e garantias.
-- Para não carregar, use SEED_DATA=classpath:data.sql. Senha de todos os usuários de exemplo: senha123

INSERT INTO endereco (id, estado, cidade, rua, numero, cep) VALUES
    ('22222222-2222-2222-2222-222222222222', 'RJ', 'Rio de Janeiro', 'Av. Atlântica', '250', '22010000');

-- 2. Empresa (IDENTITY)
INSERT INTO empresa (razao_social, inscricao_estadual, cnpj) VALUES
                                                                 ('Frio Total Ltda', '123456789', '12345678000199'),
                                                                 ('Gelo Forte S.A.', '987654321', '98765432000188');

-- 3. Usuario
INSERT INTO usuario (id, perfil, nome_completo, email, cpf, senha, telefone, celular, endereco_id, empresa_id) VALUES
                                                                                                           ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'CLIENTE', 'João Silva', 'mendesgusta58@gmail.com', '11122233344',
                                                                                                            '$2a$10$7rV.ttF6IWKudwd2C1cyS.qJeD/ijRqq9vswkP9frBEp.8ythYSV6', '1133334444', '11999998888',
                                                                                                            '11111111-1111-1111-1111-111111111111',
                                                                                                            (SELECT id FROM empresa WHERE cnpj = '12345678000199')),
                                                                                                           ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'CLIENTE', 'Maria Souza', 'guimesquita.0612@gmail.com', '55566677788',
                                                                                                            '$2a$10$7rV.ttF6IWKudwd2C1cyS.qJeD/ijRqq9vswkP9frBEp.8ythYSV6', '2122223333', '21988887777',
                                                                                                            '22222222-2222-2222-2222-222222222222', NULL);

-- Técnico de exemplo
INSERT INTO usuario (id, perfil, nome_completo, email, cpf, senha, telefone, celular, endereco_id) VALUES
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'TECNICO', 'Davi Jeronimo', 'davi@frigotec.com', '39053344705',
     '$2a$10$7rV.ttF6IWKudwd2C1cyS.qJeD/ijRqq9vswkP9frBEp.8ythYSV6', '1133334444', '11977776666',
     '11111111-1111-1111-1111-111111111111');

-- 6. EquipamentoUsuario (IDENTITY): unidades dos clientes de exemplo
INSERT INTO equipamento_usuario (equipamento_id, usuario_id, numero_serie) VALUES
    ((SELECT id FROM equipamento WHERE modelo = 'Chapa Bifteira FP-600'),      'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'FP-0001'),
    ((SELECT id FROM equipamento WHERE modelo = 'Fritadeira Elétrica FP-20L'), 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'FP-0002'),
    ((SELECT id FROM equipamento WHERE modelo = 'Forno Turbo ITW-4'),          'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'ITW-0001'),
    ((SELECT id FROM equipamento WHERE modelo = 'Forno Combinado iCombi Pro'), 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'RT-0002'),
    ((SELECT id FROM equipamento WHERE modelo = 'Chapa Bifteira FP-600'),      'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'FP-0003');

-- 9. Chamado (AUTO), um em cada situação do fluxo:
-- 1001 na fila (sem técnico) | 1002 aprovado | 1003 visita realizada (testa orçamento) | 1004 finalizado
-- 1005 na fila, equipamento com garantia EM ANÁLISE | 1006 contato do técnico (cliente aprova o serviço) | 1007 aprovação do serviço (técnico agenda a visita)
INSERT INTO chamado (id, protocolo, data_pedido, descricao, problema, usuario_id, tecnico_id, equipamento_usuario_id, visita_agendada_em) VALUES
    (1001, 'PR00001001', '2026-10-10', 'A chapa não esquenta', 'Não liga',
     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', NULL,
     (SELECT id FROM equipamento_usuario WHERE numero_serie = 'FP-0001'), NULL),
    (1002, 'PR00001002', '2026-10-12', 'O forno apresenta erro no painel', 'Painel com erro',
     'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc',
     (SELECT id FROM equipamento_usuario WHERE numero_serie = 'RT-0002'), NULL),
    (1003, 'PR00001003', '2026-10-05', 'Forno não aquece', 'Resistência queimada',
     'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc',
     (SELECT id FROM equipamento_usuario WHERE numero_serie = 'RT-0002'), '2026-10-20 10:00:00'),
    (1004, 'PR00001004', '2026-09-01', 'Forno turbo não esquenta de forma uniforme', 'Temperatura irregular',
     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'cccccccc-cccc-cccc-cccc-cccccccccccc',
     (SELECT id FROM equipamento_usuario WHERE numero_serie = 'ITW-0001'), '2026-09-12 14:00:00'),
    (1005, 'PR00001005', '2026-10-28', 'A fritadeira desarma o disjuntor', 'Não liga',
     'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', NULL,
     (SELECT id FROM equipamento_usuario WHERE numero_serie = 'FP-0002'), NULL),
    (1006, 'PR00001006', '2026-10-25', 'A chapa faz barulho ao esquentar', 'Ruído',
     'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc',
     (SELECT id FROM equipamento_usuario WHERE numero_serie = 'FP-0003'), NULL),
    (1007, 'PR00001007', '2026-10-22', 'O forno vaza água na porta', 'Vazamento',
     'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'cccccccc-cccc-cccc-cccc-cccccccccccc',
     (SELECT id FROM equipamento_usuario WHERE numero_serie = 'RT-0002'), NULL);

-- 10. Historico (IDENTITY): o status atual é o último registro de cada chamado
INSERT INTO historico (status, data_alteracao, responsavel_id, chamado_id) VALUES
    ('ENVIADO',               '2026-10-10 09:00:00', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1001),
    ('ENVIADO',               '2026-10-12 14:30:00', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 1002),
    ('APROVADO_PELO_TECNICO', '2026-10-13 08:15:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1002),
    ('ENVIADO',               '2026-10-05 10:00:00', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 1003),
    ('APROVADO_PELO_TECNICO', '2026-10-06 09:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1003),
    ('CONTATO_DO_TECNICO',    '2026-10-07 09:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1003),
    ('APROVACAO_DO_SERVICO',  '2026-10-08 09:00:00', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 1003),
    ('VISITA_AGENDADA',       '2026-10-09 09:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1003),
    ('ENVIADO',               '2026-09-01 08:00:00', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1004),
    ('APROVADO_PELO_TECNICO', '2026-09-02 09:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1004),
    ('CONTATO_DO_TECNICO',    '2026-09-03 09:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1004),
    ('APROVACAO_DO_SERVICO',  '2026-09-04 09:00:00', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1004),
    ('VISITA_AGENDADA',       '2026-09-05 09:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1004),
    ('FINALIZADO',            '2026-09-15 16:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1004),
    ('ENVIADO',               '2026-10-28 10:00:00', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 1005),
    ('ENVIADO',               '2026-10-25 11:00:00', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 1006),
    ('APROVADO_PELO_TECNICO', '2026-10-26 09:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1006),
    ('CONTATO_DO_TECNICO',    '2026-10-27 09:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1006),
    ('ENVIADO',               '2026-10-22 15:00:00', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 1007),
    ('APROVADO_PELO_TECNICO', '2026-10-23 09:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1007),
    ('CONTATO_DO_TECNICO',    '2026-10-24 09:00:00', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 1007),
    ('APROVACAO_DO_SERVICO',  '2026-10-25 09:00:00', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 1007);

-- 11. Encerramento do chamado finalizado (AUTO)
INSERT INTO encerramento (id, descricao, data_encerramento, motivo, chamado_id) VALUES
    (1001, 'Substituído o termostato e calibrado o forno. Teste de aquecimento uniforme concluído.', '2026-09-15', 'Reparo concluído', 1004);

-- 12. Orçamento do chamado finalizado, já aceito pelo cliente (IDENTITY)
INSERT INTO orcamento (valor, descricao, chamado_id, is_aceito, criado_em, respondido_em) VALUES
    (480.00, 'Termostato industrial e mão de obra', 1004, TRUE, '2026-09-12 16:00:00', '2026-09-13 09:00:00');

-- 13. MensagemEmail (AUTO)
INSERT INTO mensagem_email (id, chamado_id, usuario_id, descricao, assunto) VALUES
    (1001, 1001, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
     'Seu chamado foi aberto e será atendido em breve.', 'Chamado PR00001001 aberto');

-- 14. Garantia (IDENTITY): só equipamentos da marca própria. FP-0001 ativa, FP-0002 em análise, FP-0003 vencida
INSERT INTO garantia (equipamento_usuario_id, tipo, data_compra, nota_fiscal, validacao, data_fim_garantia) VALUES
    ((SELECT id FROM equipamento_usuario WHERE numero_serie = 'FP-0001'), 'Garantia de fábrica', '2026-01-10', 'NF-123456', 'VALIDADA',   '2027-01-10'),
    ((SELECT id FROM equipamento_usuario WHERE numero_serie = 'FP-0002'), 'Garantia comum',      '2026-10-20', 'NF-777',    'EM_ANALISE', NULL),
    ((SELECT id FROM equipamento_usuario WHERE numero_serie = 'FP-0003'), 'Garantia comum',      '2025-06-01', 'NF-555',    'VALIDADA',   '2026-06-01');

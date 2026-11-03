-- Consultas para mostrar no H2 Console durante a apresentação.
-- Abrir: http://localhost:8080/h2-console   JDBC URL: jdbc:h2:mem:frigotec   Usuário: sa   Senha: (vazia)
-- Rode uma consulta por vez (selecione o texto e clique em Run, ou Ctrl+Enter).

-- 1. As 14 tabelas do modelo (slide do DER)
SHOW TABLES;

-- 2. Usuários e perfis. A senha NUNCA fica em texto: só o início do hash BCrypt ($2a$10$...)
SELECT id, nome_completo, email, perfil, LEFT(senha, 7) AS inicio_do_hash
FROM usuario
ORDER BY perfil, nome_completo;

-- 3. Quantos usuários por perfil (uma única tabela: o técnico é um usuário com perfil TECNICO)
SELECT perfil, COUNT(*) AS total
FROM usuario
GROUP BY perfil;

-- 4. Marcas e qual é a própria (só ela tem garantia)
SELECT id, nome, propria
FROM fabricante
ORDER BY nome;

-- 5. Relacionamento do CRUD: cliente -> equipamento -> modelo -> marca -> garantia
SELECT u.nome_completo AS cliente,
       f.nome          AS marca,
       e.modelo,
       eu.id           AS unidade,
       eu.numero_serie,
       g.validacao,
       g.data_fim_garantia
FROM equipamento_usuario eu
JOIN usuario u      ON u.id = eu.usuario_id
JOIN equipamento e  ON e.id = eu.equipamento_id
JOIN fabricante f   ON f.id = e.fabricante_id
LEFT JOIN garantia g ON g.equipamento_usuario_id = eu.id
ORDER BY u.nome_completo, eu.id;

-- 6. Chamados com o STATUS ATUAL (o último registro do histórico, não uma coluna do chamado)
SELECT c.protocolo,
       cli.nome_completo AS cliente,
       tec.nome_completo AS tecnico,
       (SELECT h.status FROM historico h WHERE h.chamado_id = c.id ORDER BY h.id DESC LIMIT 1) AS status_atual,
       c.problema
FROM chamado c
JOIN usuario cli      ON cli.id = c.usuario_id
LEFT JOIN usuario tec ON tec.id = c.tecnico_id
ORDER BY c.id DESC;

-- 7. Linha do tempo de um chamado (troque o protocolo): histórico imutável, com quem executou cada passo
SELECT h.status, h.data_alteracao, u.nome_completo AS responsavel
FROM historico h
LEFT JOIN usuario u ON u.id = h.responsavel_id
WHERE h.chamado_id = (SELECT id FROM chamado WHERE protocolo = 'PR00001001')
ORDER BY h.id;

-- 8. PADRÃO OBSERVER / API externa: os e-mails disparados pelos eventos (PENDENTE, ENVIADO ou ERRO)
SELECT id, assunto, status, data_envio
FROM mensagem_email
ORDER BY id DESC;

-- 9. Arquivos enviados (upload): metadados no banco, conteúdo em disco
SELECT a.id, a.categoria, a.nome_original, a.tipo_conteudo, a.tamanho,
       c.protocolo, a.equipamento_usuario_id
FROM anexo a
LEFT JOIN chamado c ON c.id = a.chamado_id
ORDER BY a.id DESC;

-- 10. Os mesmos números do DASHBOARD do técnico, direto no banco
-- 10a. Chamados novos: sem técnico e ainda no status ENVIADO
SELECT COUNT(*) AS chamados_novos
FROM chamado c
WHERE c.tecnico_id IS NULL
  AND (SELECT h.status FROM historico h WHERE h.chamado_id = c.id ORDER BY h.id DESC LIMIT 1) = 'ENVIADO';

-- 10b. Meus chamados (troque o e-mail): da carteira do técnico e não finalizados
SELECT COUNT(*) AS meus_chamados
FROM chamado c
JOIN usuario t ON t.id = c.tecnico_id
WHERE t.email = 'davi@frigotec.com'
  AND (SELECT h.status FROM historico h WHERE h.chamado_id = c.id ORDER BY h.id DESC LIMIT 1) <> 'FINALIZADO';

-- 10c. Orçamentos aguardando resposta do cliente (is_aceito nulo = pendente)
SELECT COUNT(*) AS orcamentos_pendentes
FROM orcamento
WHERE is_aceito IS NULL;

-- 11. Orçamentos e o chamado de cada um
SELECT o.id, c.protocolo, o.valor,
       CASE WHEN o.is_aceito IS NULL THEN 'PENDENTE' WHEN o.is_aceito THEN 'ACEITO' ELSE 'RECUSADO' END AS situacao
FROM orcamento o
JOIN chamado c ON c.id = o.chamado_id;

-- 12. Troca de técnico (auditoria da redesignação)
SELECT r.feito_em, c.protocolo, ant.nome_completo AS tecnico_anterior, novo.nome_completo AS tecnico_novo, r.motivo
FROM redesignacao r
JOIN chamado c ON c.id = r.chamado_id
LEFT JOIN usuario ant ON ant.id = r.tecnico_anterior_id
JOIN usuario novo     ON novo.id = r.tecnico_novo_id;

-- 13. Garantias e a situação de validação pela equipe
SELECT g.id, eu.numero_serie, g.tipo, g.data_compra, g.nota_fiscal, g.validacao, g.data_fim_garantia
FROM garantia g
JOIN equipamento_usuario eu ON eu.id = g.equipamento_usuario_id;

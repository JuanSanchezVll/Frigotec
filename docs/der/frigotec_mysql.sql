-- DER do Portal Frigotec em MySQL 8 — espelha as entidades JPA do backend (pacote entity), tabela por tabela.
-- O H2 em memória usado em desenvolvimento gera estas mesmas tabelas; em produção basta apontar o Spring para o MySQL.
CREATE DATABASE IF NOT EXISTS frigotec CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE frigotec;

-- ===== Usuário =====
CREATE TABLE endereco (
    id          CHAR(36)     NOT NULL,
    estado      VARCHAR(255),
    cidade      VARCHAR(255),
    rua         VARCHAR(255),
    numero      VARCHAR(255),
    cep         VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE empresa (
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    razao_social        VARCHAR(255),
    inscricao_estadual  VARCHAR(255),
    cnpj                VARCHAR(255),
    PRIMARY KEY (id)
);

-- Uma única tabela de usuários; o perfil (CLIENTE, TECNICO, ADMIN) é uma coluna (SDD RN-USR-01). Não existe tabela "tecnico".
CREATE TABLE usuario (
    id                  CHAR(36)     NOT NULL,
    nome_completo       VARCHAR(255) NOT NULL,
    email               VARCHAR(255) NOT NULL,
    cpf                 VARCHAR(255),
    cnpj                VARCHAR(255),
    razao_social        VARCHAR(255),
    inscricao_estadual  VARCHAR(255),
    senha               VARCHAR(255) NOT NULL,      -- hash BCrypt, nunca devolvido pela API
    telefone            VARCHAR(10),
    celular             VARCHAR(13),
    perfil              ENUM('CLIENTE','TECNICO','ADMIN') NOT NULL,
    endereco_id         CHAR(36)     NOT NULL,
    empresa_id          BIGINT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuario_email (email),
    UNIQUE KEY uk_usuario_cpf (cpf),
    UNIQUE KEY uk_usuario_cnpj (cnpj),
    CONSTRAINT fk_usuario_endereco FOREIGN KEY (endereco_id) REFERENCES endereco (id),
    CONSTRAINT fk_usuario_empresa  FOREIGN KEY (empresa_id)  REFERENCES empresa (id)
);

-- ===== Equipamento =====
CREATE TABLE fabricante (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    nome     VARCHAR(255),
    propria  BOOLEAN      NOT NULL DEFAULT FALSE,   -- TRUE = FrigoPro, a única marca com garantia gerida pela Frigotec (RN-EQP-03)
    PRIMARY KEY (id),
    UNIQUE KEY uk_fabricante_nome (nome)
);

CREATE TABLE equipamento (            -- catálogo: modelo de uma marca
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    modelo         VARCHAR(255),
    fabricante_id  BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_equipamento_fabricante FOREIGN KEY (fabricante_id) REFERENCES fabricante (id)
);

-- Tabela associativa usuário × modelo, com PK própria (id) e dado próprio (numero_serie): a unidade física do cliente.
CREATE TABLE equipamento_usuario (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    equipamento_id  BIGINT       NOT NULL,
    usuario_id      CHAR(36)     NOT NULL,
    numero_serie    VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_eu_equipamento FOREIGN KEY (equipamento_id) REFERENCES equipamento (id),
    CONSTRAINT fk_eu_usuario     FOREIGN KEY (usuario_id)     REFERENCES usuario (id)
);

-- Garantia é de uma unidade (RN-GAR-01): relação 1 para 1 com equipamento_usuario. A situação ativa/vencida é calculada, não gravada.
CREATE TABLE garantia (
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    equipamento_usuario_id  BIGINT       NOT NULL,
    tipo                    VARCHAR(255),
    data_compra             DATE,
    nota_fiscal             VARCHAR(255),
    validacao               ENUM('EM_ANALISE','VALIDADA','RECUSADA') NOT NULL,
    data_fim_garantia       DATE,
    PRIMARY KEY (id),
    UNIQUE KEY uk_garantia_eu (equipamento_usuario_id),
    CONSTRAINT fk_garantia_eu FOREIGN KEY (equipamento_usuario_id) REFERENCES equipamento_usuario (id)
);

-- ===== Chamado =====
CREATE TABLE chamado (
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    protocolo               VARCHAR(255),           -- PR + 8 dígitos, gerado pelo servidor
    data_pedido             DATE,
    problema                VARCHAR(255),           -- categoria
    descricao               VARCHAR(255),
    usuario_id              CHAR(36),               -- cliente que abriu
    tecnico_id              CHAR(36),               -- técnico responsável (usuário com perfil TECNICO)
    equipamento_usuario_id  BIGINT,
    visita_agendada_em      DATETIME(6),
    PRIMARY KEY (id),
    UNIQUE KEY uk_chamado_protocolo (protocolo),
    CONSTRAINT fk_chamado_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
    CONSTRAINT fk_chamado_tecnico FOREIGN KEY (tecnico_id) REFERENCES usuario (id),
    CONSTRAINT fk_chamado_eu      FOREIGN KEY (equipamento_usuario_id) REFERENCES equipamento_usuario (id)
);

-- Histórico imutável: o status atual é o último registro do chamado (RN-CHM-16). O status é um enum, não uma tabela.
CREATE TABLE historico (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    chamado_id      BIGINT,
    status          ENUM('ENVIADO','APROVADO_PELO_TECNICO','CONTATO_DO_TECNICO','APROVACAO_DO_SERVICO','VISITA_AGENDADA','FINALIZADO'),
    data_alteracao  DATETIME(6),
    responsavel_id  CHAR(36),
    PRIMARY KEY (id),
    CONSTRAINT fk_historico_chamado     FOREIGN KEY (chamado_id)     REFERENCES chamado (id),
    CONSTRAINT fk_historico_responsavel FOREIGN KEY (responsavel_id) REFERENCES usuario (id)
);

CREATE TABLE encerramento (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    chamado_id         BIGINT       NOT NULL,
    motivo             VARCHAR(255) NOT NULL,
    descricao          VARCHAR(255) NOT NULL,
    data_encerramento  DATE         NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_encerramento_chamado FOREIGN KEY (chamado_id) REFERENCES chamado (id)
);

CREATE TABLE orcamento (
    id            BIGINT         NOT NULL AUTO_INCREMENT,
    chamado_id    BIGINT,
    valor         DECIMAL(38,2),
    descricao     VARCHAR(255),
    is_aceito     BOOLEAN,                  -- NULL = pendente, TRUE = aceito, FALSE = recusado (RN-ORC-04)
    criado_em     DATETIME(6),
    respondido_em DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_orcamento_chamado FOREIGN KEY (chamado_id) REFERENCES chamado (id)
);

CREATE TABLE mensagem_email (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    chamado_id   BIGINT,
    usuario_id   CHAR(36),
    assunto      VARCHAR(255),
    descricao    VARCHAR(255),
    data_envio   DATETIME(6),
    status       ENUM('PENDENTE','ENVIADO','ERRO'),
    PRIMARY KEY (id),
    CONSTRAINT fk_email_chamado FOREIGN KEY (chamado_id) REFERENCES chamado (id),
    CONSTRAINT fk_email_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

-- Auditoria da troca de técnico (RN-CHM-12); não altera o status.
CREATE TABLE redesignacao (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    chamado_id           BIGINT       NOT NULL,
    tecnico_anterior_id  CHAR(36),
    tecnico_novo_id      CHAR(36)     NOT NULL,
    motivo               VARCHAR(255) NOT NULL,
    feito_por_id         CHAR(36)     NOT NULL,
    feito_em             DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_red_chamado  FOREIGN KEY (chamado_id)          REFERENCES chamado (id),
    CONSTRAINT fk_red_anterior FOREIGN KEY (tecnico_anterior_id) REFERENCES usuario (id),
    CONSTRAINT fk_red_novo     FOREIGN KEY (tecnico_novo_id)     REFERENCES usuario (id),
    CONSTRAINT fk_red_feito    FOREIGN KEY (feito_por_id)        REFERENCES usuario (id)
);

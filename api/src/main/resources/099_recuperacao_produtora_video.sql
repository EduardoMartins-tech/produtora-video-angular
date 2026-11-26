-- Atividade de Recuperação - Banco de Dados
-- 99. Sistema de Controle de Produtora de Vídeo
-- Aluno: Eduardo Ferreira Martins

-- =========================
-- DDL - estrutura do banco
-- =========================

-- apaga o banco de uma execução anterior
DROP DATABASE IF EXISTS produtoravideo;

CREATE DATABASE produtoravideo;

USE produtoravideo;

CREATE TABLE Cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nomeCliente VARCHAR(100) NOT NULL,
    cpfCnpj VARCHAR(18) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- cada projeto pertence a um cliente
CREATE TABLE ProjetoVideo (
    idProjeto INT AUTO_INCREMENT PRIMARY KEY,
    idCliente INT NOT NULL,
    tituloProjeto VARCHAR(150) NOT NULL,
    dataInicio DATE NOT NULL,
    dataEntrega DATE NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

-- cada entrega pertence a um projeto
CREATE TABLE EntregaProjeto (
    idEntrega INT AUTO_INCREMENT PRIMARY KEY,
    idProjeto INT NOT NULL,
    dataEntrega DATE NOT NULL,
    formatoArquivo VARCHAR(20) NOT NULL,
    statusEntrega VARCHAR(30) NOT NULL,
    FOREIGN KEY (idProjeto) REFERENCES ProjetoVideo(idProjeto)
);

-- ALTER: campo novo com o valor cobrado pelo projeto
ALTER TABLE ProjetoVideo ADD COLUMN orcamento DECIMAL(10, 2) NOT NULL DEFAULT 0.00;

-- =========================
-- DML - manipulação dos dados
-- =========================

-- INSERT
INSERT INTO Cliente (nomeCliente, cpfCnpj, telefone, email) VALUES
    ('Agência Horizonte Digital Ltda', '12.345.678/0001-90', '(11) 98765-4321', 'contato@horizontedigital.com.br'),
    ('Mariana Costa Produções', '234.567.890-12', '(11) 97654-3210', 'mariana.costa@email.com.br'),
    ('TechNova Soluções Corporativas S.A.', '98.765.432/0001-55', '(21) 99876-5432', 'marketing@technova.com.br'),
    ('Eventos & Festas Estrela Ltda', '45.678.912/0001-33', '(31) 98456-7890', 'eventos@estrela.com.br');

INSERT INTO ProjetoVideo (idCliente, tituloProjeto, dataInicio, dataEntrega, orcamento) VALUES
    (1, 'Comercial Institucional 4K - Campanha de Verão', '2026-02-01', '2026-02-28', 12500.00),
    (2, 'Documentário Cultural - Bastidores da Música', '2026-03-05', '2026-04-15', 8500.00),
    (3, 'Vídeo de Treinamento Corporativo e Onboarding', '2026-03-10', '2026-03-30', 6200.00),
    (4, 'Aftermovie Festival de Inverno', '2026-04-01', '2026-04-20', 4800.00);

INSERT INTO EntregaProjeto (idProjeto, dataEntrega, formatoArquivo, statusEntrega) VALUES
    (1, '2026-02-27', 'MP4', 'Aprovado'),
    (2, '2026-04-14', 'MOV', 'Em Revisão'),
    (3, '2026-03-29', 'MP4', 'Entregue'),
    (4, '2026-04-19', 'AVI', 'Pendente');

-- UPDATE
UPDATE Cliente
SET telefone = '(11) 99988-7766',
    email = 'atendimento@horizontedigital.com.br'
WHERE idCliente = 1;

UPDATE ProjetoVideo
SET dataEntrega = '2026-04-22',
    orcamento = 9300.00
WHERE idProjeto = 2;

UPDATE EntregaProjeto
SET statusEntrega = 'Aprovado'
WHERE idEntrega = 2;

-- DELETE (primeiro a entrega, depois o projeto e por último o cliente,
-- senão a chave estrangeira não deixa apagar)
DELETE FROM EntregaProjeto
WHERE idEntrega = 4;

DELETE FROM ProjetoVideo
WHERE idProjeto = 4;

DELETE FROM Cliente
WHERE idCliente = 4;

-- =========================
-- DQL - consultas
-- =========================

-- Consulta 1 - Cliente: lista os clientes
SELECT nomeCliente, cpfCnpj, telefone, email
FROM Cliente;

-- Consulta 2 - Cliente: clientes de São Paulo (DDD 11) em ordem alfabética
SELECT idCliente, nomeCliente, telefone, email
FROM Cliente
WHERE telefone LIKE '(11)%'
ORDER BY nomeCliente ASC;

-- Consulta 3 - ProjetoVideo: lista os projetos
SELECT tituloProjeto, dataInicio, dataEntrega, orcamento
FROM ProjetoVideo;

-- Consulta 4 - ProjetoVideo: projetos com orçamento a partir de 7000, pela data de entrega
SELECT idProjeto, tituloProjeto, idCliente, dataEntrega, orcamento
FROM ProjetoVideo
WHERE orcamento >= 7000.00
ORDER BY dataEntrega ASC;

-- Consulta 5 - EntregaProjeto: lista as entregas
SELECT idProjeto, dataEntrega, formatoArquivo, statusEntrega
FROM EntregaProjeto;

-- Consulta 6 - EntregaProjeto: entregas aprovadas, da mais recente para a mais antiga
SELECT idEntrega, idProjeto, dataEntrega, formatoArquivo, statusEntrega
FROM EntregaProjeto
WHERE statusEntrega = 'Aprovado'
ORDER BY dataEntrega DESC;

-- Consulta extra: junta as 3 tabelas (cliente, projeto e entrega)
SELECT
    c.nomeCliente,
    p.tituloProjeto,
    p.orcamento,
    e.dataEntrega,
    e.formatoArquivo,
    e.statusEntrega
FROM EntregaProjeto e
INNER JOIN ProjetoVideo p ON e.idProjeto = p.idProjeto
INNER JOIN Cliente c ON p.idCliente = c.idCliente
ORDER BY e.dataEntrega ASC;

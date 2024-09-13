-- Inserir hotéis na mesma cidade
INSERT INTO hotel (nome, endereco, cidade, estado, pais, telefone, numero_de_quartos, numero_de_hospedes, preco_por_noite, avaliacao)
VALUES
('Hotel Teste 1', '101 Teste Ave', 'Cidade Teste', 'Estado Teste', 'Brasil', '111-222-3333', 80, 200, 150.00, 4.2),
('Hotel Teste 2', '102 Teste Blvd', 'Cidade Teste', 'Estado Teste', 'Brasil', '222-333-4444', 60, 500, 200.00, 4.5),
('Hotel Teste 3', '103 Teste Lane', 'Cidade Teste', 'Estado Teste', 'Brasil', '333-444-5555', 120, 20, 120.00, 3.9),
('Hotel Teste 4', '104 Teste St', 'Cidade Teste', 'Estado Teste', 'Brasil', '444-555-6666', 100, 89, 180.00, 4.0),
('Hotel Teste 5', '105 Teste Rd', 'Cidade Teste', 'Estado Teste', 'Brasil', '555-666-7777', 90, 2500, 220.00, 4.7);

-- Inserir comodidades para cada hotel na tabela comodidade
INSERT INTO comodidade (hotel_id, descricao)
SELECT id, 'Wi-Fi grátis' FROM hotel WHERE nome = 'Hotel Teste 1';
INSERT INTO comodidade (hotel_id, descricao)
SELECT id, 'Estacionamento gratuito' FROM hotel WHERE nome = 'Hotel Teste 1';
INSERT INTO comodidade (hotel_id, descricao)
SELECT id, 'Piscina' FROM hotel WHERE nome = 'Hotel Teste 2';
INSERT INTO comodidade (hotel_id, descricao)
SELECT id, 'Café da manhã incluso' FROM hotel WHERE nome = 'Hotel Teste 3';
INSERT INTO comodidade (hotel_id, descricao)
SELECT id, 'Academia' FROM hotel WHERE nome = 'Hotel Teste 4';
INSERT INTO comodidade (hotel_id, descricao)
SELECT id, 'Spa' FROM hotel WHERE nome = 'Hotel Teste 5';

-- Inserir quartos para os hotéis na mesma cidade
INSERT INTO quarto (hotel_id, tipo_quarto, preco_por_noite, max_hospedes, disponivel)
SELECT id, 'Standard', 160.00, 2, TRUE
FROM hotel
WHERE nome = 'Hotel Teste 1';

INSERT INTO quarto (hotel_id, tipo_quarto, preco_por_noite, max_hospedes, disponivel)
SELECT id, 'Superior', 210.00, 2, TRUE
FROM hotel
WHERE nome = 'Hotel Teste 2';

INSERT INTO quarto (hotel_id, tipo_quarto, preco_por_noite, max_hospedes, disponivel)
SELECT id, 'Economy', 130.00, 3, TRUE
FROM hotel
WHERE nome = 'Hotel Teste 3';

INSERT INTO quarto (hotel_id, tipo_quarto, preco_por_noite, max_hospedes, disponivel)
SELECT id, 'Deluxe', 190.00, 2, TRUE
FROM hotel
WHERE nome = 'Hotel Teste 4';

INSERT INTO quarto (hotel_id, tipo_quarto, preco_por_noite, max_hospedes, disponivel)
SELECT id, 'Presidential Suite', 230.00, 4, TRUE
FROM hotel
WHERE nome = 'Hotel Teste 5';

-- Inserir reservas para os hotéis na mesma cidade
INSERT INTO reserva (hotel_id, quarto_id, nome_hospede, email_hospede, telefone_hospede, data_check_in, data_check_out, numero_hospedes, status)
SELECT h.id, q.id, 'Lucas Santos', 'lucas.santos@example.com', '666-777-8888', '2024-11-01', '2024-11-05', 2, 'CONFIRMADA'
FROM hotel h
JOIN quarto q ON q.hotel_id = h.id
WHERE h.nome = 'Hotel Teste 1'
AND q.tipo_quarto = 'Standard';

INSERT INTO reserva (hotel_id, quarto_id, nome_hospede, email_hospede, telefone_hospede, data_check_in, data_check_out, numero_hospedes, status)
SELECT h.id, q.id, 'Fernanda Lima', 'fernanda.lima@example.com', '777-888-9999', '2024-11-06', '2024-11-10', 2, 'PENDENTE'
FROM hotel h
JOIN quarto q ON q.hotel_id = h.id
WHERE h.nome = 'Hotel Teste 2'
AND q.tipo_quarto = 'Superior';

INSERT INTO reserva (hotel_id, quarto_id, nome_hospede, email_hospede, telefone_hospede, data_check_in, data_check_out, numero_hospedes, status)
SELECT h.id, q.id, 'Roberto Oliveira', 'roberto.oliveira@example.com', '888-999-0000', '2024-11-15', '2024-11-20', 3, 'CANCELADA'
FROM hotel h
JOIN quarto q ON q.hotel_id = h.id
WHERE h.nome = 'Hotel Teste 3'
AND q.tipo_quarto = 'Economy';

INSERT INTO reserva (hotel_id, quarto_id, nome_hospede, email_hospede, telefone_hospede, data_check_in, data_check_out, numero_hospedes, status)
SELECT h.id, q.id, 'Ana Paula', 'ana.paula@example.com', '999-000-1111', '2024-12-01', '2024-12-07', 2, 'PENDENTE'
FROM hotel h
JOIN quarto q ON q.hotel_id = h.id
WHERE h.nome = 'Hotel Teste 4'
AND q.tipo_quarto = 'Deluxe';

INSERT INTO reserva (hotel_id, quarto_id, nome_hospede, email_hospede, telefone_hospede, data_check_in, data_check_out, numero_hospedes, status)
SELECT h.id, q.id, 'Juliana Almeida', 'juliana.almeida@example.com', '000-111-2222', '2024-12-10', '2024-12-15', 4, 'CONFIRMADA'
FROM hotel h
JOIN quarto q ON q.hotel_id = h.id
WHERE h.nome = 'Hotel Teste 5'
AND q.tipo_quarto = 'Presidential Suite';


---- Inserir notificações para as reservas
--INSERT INTO notificacao (reserva_id, email_destinatario, enviada_em, mensagem, visualizada, tipo_notificacao)
--SELECT r.id, 'fernanda.lima@example.com', NOW(), 'Sua reserva está pendente.', FALSE, 'PENDENTE'
--FROM reserva r
--WHERE r.nome_hospede = 'Fernanda Lima';
--
--INSERT INTO notificacao (reserva_id, email_destinatario, enviada_em, mensagem, visualizada, tipo_notificacao)
--SELECT r.id, 'roberto.oliveira@example.com', NOW(), 'Sua reserva foi cancelada.', FALSE, 'CANCELADA'
--FROM reserva r
--WHERE r.nome_hospede = 'Roberto Oliveira';
--
--INSERT INTO notificacao (reserva_id, email_destinatario, enviada_em, mensagem, visualizada, tipo_notificacao)
--SELECT r.id, 'ana.paula@example.com', NOW(), 'Sua reserva está pendente.', FALSE, 'PENDENTE'
--FROM reserva r
--WHERE r.nome_hospede = 'Ana Paula';
--
--INSERT INTO notificacao (reserva_id, email_destinatario, enviada_em, mensagem, visualizada, tipo_notificacao)
--SELECT r.id, 'juliana.almeida@example.com', NOW(), 'Sua reserva foi confirmada.', FALSE, 'CONFIRMADA'
--FROM reserva r
--WHERE r.nome_hospede = 'Juliana Almeida';



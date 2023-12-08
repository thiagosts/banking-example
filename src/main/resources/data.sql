CREATE TABLE cliente (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL
);

CREATE TABLE conta (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       cliente_id BIGINT NOT NULL,
                       numero_conta INT NOT NULL,
                       saldo DECIMAL(10, 2) NOT NULL,
                       ativa BOOLEAN NOT NULL,
                       limite_diario DECIMAL(10, 2) NOT NULL,
                       agencia INT NOT NULL,
                       FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

INSERT INTO cliente (nome) VALUES ('TESTE');
INSERT INTO cliente (nome) VALUES ('TESTE 2');
INSERT INTO cliente (nome) VALUES ('TESTE 3');

INSERT INTO conta (cliente_id, numero_conta, saldo, ativa, limite_diario, agencia) VALUES (1, 10001, 100.00, true, 1000.00, 1);
INSERT INTO conta (cliente_id, numero_conta, saldo, ativa, limite_diario, agencia) VALUES (2, 10002, 500.00, true, 1000.00, 1);
INSERT INTO conta (cliente_id, numero_conta, saldo, ativa, limite_diario, agencia) VALUES (3, 10003, 1000.00, true, 1000.00, 1);

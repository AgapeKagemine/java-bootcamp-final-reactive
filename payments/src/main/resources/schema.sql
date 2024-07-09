DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS balance;

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    amount FLOAT(8) NOT NULL,
    order_id FLOAT(8) NOT NULL,
    payment_date TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    mode VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    reference_number VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS balance (
    id BIGSERIAL PRIMARY KEY,
    amount FLOAT(8) NOT NULL
);

INSERT INTO balance (amount) 
VALUES 
    (50000),
    (0),
    (9999999);
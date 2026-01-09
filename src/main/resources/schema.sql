CREATE TABLE IF NOT EXISTS transaction
(
    public_id VARCHAR(36) PRIMARY KEY,
    type VARCHAR(255),
    date TIMESTAMP NOT NULL,
    account_number VARCHAR(255),
    currency VARCHAR(255),
    amount DECIMAL(19,2),
    merchant_name VARCHAR(255),
    merchant_logo VARCHAR(255),

 CONSTRAINT ck_transaction_type CHECK (type IN ('DEPOSIT', 'WITHDRAWAL', 'PAYMENT', 'TRANSFER'))
);
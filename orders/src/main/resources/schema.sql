DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS orders_item CASCADE;

CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    billing_address VARCHAR(255) NOT NULL,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    order_status VARCHAR(255) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    shipping_address VARCHAR(255) NOT NULL,
    total_amount FLOAT(8) NOT NULL
);

CREATE TABLE IF NOT EXISTS orders_item (
    id BIGSERIAL PRIMARY KEY,
    price FLOAT(8) NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    order_id BIGINT NOT NULL,
    CONSTRAINT orders_item_fk
        FOREIGN KEY (order_id)
            REFERENCES orders(id)
            ON DELETE CASCADE
);
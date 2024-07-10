DROP TABLE IF EXISTS products;

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price FLOAT(8) NOT NULL,
    category VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),
    image_url VARCHAR(255),
    stock_quantity INTEGER NOT NULL,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO products (name, price, category, description, image_url, stock_quantity)
VALUES
    ('Product 1', 10, 'Category 1', 'Description 1', 'image1.jpg', 100),
    ('Product 2', 20, 'Category 2', 'Description 2', 'image2.jpg', 50),
    ('Product 3', 30, 'Category 3', 'Description 3', 'image3.jpg', 20),
    ('Product 4', 40, 'Category 4', 'Description 4', 'image4.jpg', 10),
    ('Product 5', 50, 'Category 5', 'Description 5', 'image5.jpg', 5),
    ('Product 6', 60, 'Category 6', 'Description 6', 'image6.jpg', 1);
CREATE TABLE customers (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           email VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        order_date DATE NOT NULL,
                        total_amount NUMERIC(12,2) NOT NULL,
                        customer_id BIGINT NOT NULL,
                        CONSTRAINT fk_customer
                            FOREIGN KEY(customer_id)
                                REFERENCES customers(id)
                                ON DELETE CASCADE
);
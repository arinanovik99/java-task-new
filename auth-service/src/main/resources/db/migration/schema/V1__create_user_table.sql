CREATE SEQUENCE user_id_seq START WITH 1;

CREATE TABLE users
(
    id            bigint       NOT NULL DEFAULT nextval('user_id_seq'),
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    role          VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),

    PRIMARY KEY (id)
);
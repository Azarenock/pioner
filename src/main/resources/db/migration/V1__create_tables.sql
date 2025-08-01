CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(500) NOT NULL,
                       date_of_birth DATE,
                       password VARCHAR(500) NOT NULL
);

CREATE TABLE email_data (
                            id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            email VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE phone_data (
                            id BIGSERIAL PRIMARY KEY,
                            user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            phone VARCHAR(13) NOT NULL UNIQUE,
                            CONSTRAINT phone_pattern CHECK (phone ~ '^7\d{10}$')
    );

CREATE TABLE accounts (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                          balance DECIMAL(19,2) NOT NULL,
                          initial_deposit DECIMAL(19,2) NOT NULL
);
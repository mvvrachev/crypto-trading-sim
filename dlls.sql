create table if not exists Users (
    id serial primary key,
    username varchar(100) not null,
	password varchar(100) not null,
    balance decimal(15, 2) not null default 10000
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    type VARCHAR(4) CHECK (type IN ('BUY', 'SELL')) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE holdings (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    UNIQUE (user_id, symbol)
);
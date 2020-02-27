CREATE TABLE IF NOT EXISTS person (
    id SERIAL PRIMARY KEY,
    firstname VARCHAR(100),
    lastname VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100)
);

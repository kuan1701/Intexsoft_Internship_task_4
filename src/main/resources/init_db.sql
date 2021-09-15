CREATE DATABASE bookstore;

CREATE TABLE IF NOT EXISTS book
(
    id SERIAL NOT NULL,
    title VARCHAR DEFAULT 255 NOT NULL,
    author VARCHAR DEFAULT 255 NOT NULL,
    price NUMERIC NOT NULL
);

CREATE UNIQUE INDEX book_id_uindex
    ON book (id);

ALTER TABLE book
    ADD CONSTRAINT book_pk
        PRIMARY KEY (id);
CREATE TABLE IF NOT EXISTS books
(
    id NUMERIC PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    quantity INT NOT NULL CHECK (quantity >= 0),
    author_id NUMERIC NOT NULL,
    CONSTRAINT fk_author
        FOREIGN KEY (author_id) REFERENCES authors(id)
);

ALTER TABLE books
    ALTER COLUMN id TYPE NUMERIC;

ALTER TABLE books
    ALTER COLUMN author_id TYPE NUMERIC;

CREATE SEQUENCE books_id_seq;

ALTER TABLE books
    ALTER COLUMN id SET DEFAULT nextval('books_id_seq');

SELECT setval('books_id_seq', COALESCE((SELECT MAX(id) FROM books)::BIGINT, 1));
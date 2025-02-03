CREATE TABLE IF NOT EXISTS authors
(
    id NUMERIC PRIMARY KEY,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    biography TEXT
);

ALTER TABLE authors
    ALTER COLUMN id TYPE NUMERIC;

CREATE SEQUENCE authors_id_seq;

ALTER TABLE authors
    ALTER COLUMN id SET DEFAULT nextval('authors_id_seq');

SELECT setval('authors_id_seq', COALESCE((SELECT MAX(id) FROM authors)::BIGINT, 1));


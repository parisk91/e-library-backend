CREATE TABLE IF NOT EXISTS books_users
(
    book_id NUMERIC NOT NULL,
    user_id NUMERIC NOT NULL,
    PRIMARY KEY (book_id, user_id),
    CONSTRAINT fk_book
        FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE ,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE books_users
    ALTER COLUMN book_id TYPE NUMERIC;

ALTER TABLE books_users
    ALTER COLUMN USER_id TYPE NUMERIC;
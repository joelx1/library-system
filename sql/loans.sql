CREATE TABLE loans (
    loan_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    item_id INT NOT NULL,
    bdate DATE NOT NULL DEFAULT CURRENT_DATE,
    rdate DATE,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_book
        FOREIGN KEY (item_id)
        REFERENCES books(item_id)
        ON DELETE CASCADE
);
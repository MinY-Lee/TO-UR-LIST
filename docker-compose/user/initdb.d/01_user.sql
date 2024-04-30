create table user
(
    user_id        CHAR(36) PRIMARY KEY,
    user_google_id CHAR(25) UNIQUE    NOT NULL,
    user_nickname  VARCHAR(45) UNIQUE NOT NULL,
    user_name      VARCHAR(35)        NOT NULL,
    user_birth     TIMESTAMP          NULL,
    user_gender    TINYINT                     DEFAULT 0,
    created_at     TIMESTAMP          NOT NULL default current_timestamp,
    deleted_at     TIMESTAMP          NULL
);

create table user
(
    user_id        CHAR(36) primary key,
    user_google_id CHAR(25) unique    not null,
    user_nickname  VARCHAR(45) unique not null,
    user_name      VARCHAR(35)        not null,
    user_birth     TIMESTAMP null,
    user_gender    TINYINT default 0,
    created_at     TIMESTAMP not null default current_timestamp,
    deleted_at     TIMESTAMP null
);

create table userinfo
(
    user_google_id CHAR(25) PRIMARY KEY,
    user_id        CHAR(36) UNIQUE NOT NULL
);

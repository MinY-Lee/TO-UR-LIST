create table if not exists country
(
    country_code char(3) not null primary key,
    country_name_eng varchar(50) not null,
    country_name_kor varchar(50) not null,
    climate varchar(200),
    language varchar(50),
    utc double,
    voltage varchar(20),
    plug_type varchar(50)
);

LOAD DATA INFILE '/docker-entrypoint-initdb.d/country_info_final_updated.csv'
    INTO TABLE country
    FIELDS
    TERMINATED BY ','
    OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
    LINES
    TERMINATED BY '\r\n'
    ignore 1 rows;
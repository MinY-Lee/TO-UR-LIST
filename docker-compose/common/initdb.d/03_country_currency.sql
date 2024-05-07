create table if not exists country_currency
(
    country_code  char(3)  not null,
    currency_code char(3)  not null,
    currency_sign varchar(5)  not null,
    PRIMARY KEY (country_code,currency_code),
    FOREIGN KEY (country_code) REFERENCES country(country_code)
);

LOAD DATA LOCAL INFILE '/docker-entrypoint-initdb.d/currency_info_final.csv'
    into table country_currency
    FIELDS
    TERMINATED BY ','
    OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
    LINES
    TERMINATED BY '\n'
    ignore 1 rows;
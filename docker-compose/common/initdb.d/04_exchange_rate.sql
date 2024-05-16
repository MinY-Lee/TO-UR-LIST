create table exchange_rate
(
    currency_code char(3) not null,
    date timestamp not null,
    exchange_rate double not null,
    PRIMARY KEY (currency_code,date)
#     FOREIGN KEY (country_code,currency_code) REFERENCES country_currency(country_code,currency_code)
);

LOAD DATA LOCAL INFILE '/docker-entrypoint-initdb.d/exchange_rates_final.csv'
    into table exchange_rate
    FIELDS
    TERMINATED BY ','
    OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
    LINES
    TERMINATED BY '\r\n'
    ignore 1 rows;

LOAD DATA LOCAL INFILE '/docker-entrypoint-initdb.d/exchange_rates_final.csv'
    into table exchange_rate
    FIELDS
    TERMINATED BY ','
    OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
    LINES
    TERMINATED BY '\n'
    ignore 1 rows;
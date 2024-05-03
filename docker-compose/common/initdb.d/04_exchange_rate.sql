create table exchange_rate
(
    exchange_rate double not null,
    date timestamp not null,
    currency_code varchar(3) not null,
    PRIMARY KEY (currency_code,date),
    FOREIGN KEY (currency_code) REFERENCES country_currency(currency_code)
);
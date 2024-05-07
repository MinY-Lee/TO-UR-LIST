create table exchange_rate
(
    currency_code char(3) not null unique,
    date timestamp not null,
    exchange_rate double not null,
    PRIMARY KEY (currency_code,date)
#     FOREIGN KEY (country_code,currency_code) REFERENCES country_currency(country_code,currency_code)
);
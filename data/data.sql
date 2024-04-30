create table country
(
    country_code char(3) not null primary key,
    country_name_eng varchar(50) not null,
    country_name_kor varchar(50) not null,
    climate varchar(150),
    language varchar(20),
    utc double,
    plug_type varchar(20),
    voltage varchar(20)
);

create table city
(
    city_id       int  not null auto_increment primary key,
    country_code  char(3)  not null,
    city_name_eng varchar(50) not null,
    city_name_kor varchar(50) not null,
    FOREIGN KEY (country_code) REFERENCES country(country_code)
);

create table country_currency
(
    currency_code varchar(3)  not null,
    country_code  char(3)  not null,
    currency_sign varchar(5)  not null,
    FOREIGN KEY (country_code) REFERENCES country(country_code)
);
GRANT FILE ON *.* TO 'test'@'%';
FLUSH PRIVILEGES;

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

LOAD DATA INFILE '/country_info_final_updated.csv'
    INTO TABLE country
    FIELDS
    TERMINATED BY ','
    OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
    LINES
    TERMINATED BY '\n'
    ignore 1 rows;

create table if not exists  city
(
    city_id       int  not null auto_increment primary key,
    country_code  char(3)  not null,
    city_name_eng varchar(50) not null,
    city_name_kor varchar(50) not null,
    FOREIGN KEY (country_code) REFERENCES country(country_code)
    );

LOAD DATA INFILE '/city_info_final.csv'
    INTO TABLE city
    FIELDS
    TERMINATED BY ','
    OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
    LINES
    TERMINATED BY '\n'
    ignore 1 rows;

create table if not exists country_currency
(
    country_code  char(3)  not null,
    currency_code char(3)  not null,
    currency_sign varchar(5)  not null,
    PRIMARY KEY (country_code,currency_code),
    FOREIGN KEY (country_code) REFERENCES country(country_code)
    );

LOAD DATA LOCAL INFILE '/currency_info_final.csv'
    into table country_currency
    FIELDS
    TERMINATED BY ','
    OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
    LINES
    TERMINATED BY '\n'
    ignore 1 rows;

create table exchange_rate
(
    currency_code char(3) not null unique,
    date timestamp not null,
    exchange_rate double not null,
    PRIMARY KEY (currency_code,date)
);
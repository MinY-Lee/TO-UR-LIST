create table if not exists  city
(
    city_id       int  not null auto_increment primary key,
    country_code  char(3)  not null,
    city_name_eng varchar(50) not null,
    city_name_kor varchar(50) not null,
    FOREIGN KEY (country_code) REFERENCES country(country_code)
);

LOAD DATA INFILE '/docker-entrypoint-initdb.d/city_info_final.csv'
    INTO TABLE city
    FIELDS
    TERMINATED BY ','
    OPTIONALLY ENCLOSED BY '"' ESCAPED BY '"'
    LINES
    TERMINATED BY '\r\n'
    ignore 1 rows;